import {Component, Inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {Kata} from './kata';
import {CompilationService} from '../../services/compilation/compilation.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {LANGService} from '../../services/LANG/lang.service';
import {AlertService} from 'ngx-alerts';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {KataSubscription} from '../../interfaces/subscriptions/KataSubscription';
import {v4 as uuid} from 'uuid';
import {KataSubscriptionService} from '../../services/kata/kata-subscription.service';
import {ProgramService} from '../../services/program/program.service';
import {KataService} from '../../services/kata/kata.service';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from '@angular/material';
import {Program} from '../program-displayer/program';
import {DomSanitizer, SafeResourceUrl} from '@angular/platform-browser';
import {CompilationServiceResponse} from '../../services/compilation/compilationServiceResponse';


@Component({
  selector: 'app-kata',
  templateUrl: './kata.component.html',
  styleUrls: ['./kata.component.scss']
})
export class KataComponent implements OnInit {


  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private kataService: KataService,
    private compilationService: CompilationService,
    private programService: ProgramService,
    private ngxLoader: NgxUiLoaderService,
    private langservice: LANGService,
    private alertService: AlertService,
    private kataSubscriptionService: KataSubscriptionService,
    private auth: AuthenticationService,
    private router: Router,
    public dialog: MatDialog,
    private sanitizer: DomSanitizer
  ) {
  }

  // @ts-ignore
  kata: Kata;
  kataid = '';
  status = 2;
  result = '';
  programid = '';

  isClosed = false;

  // @ts-ignore
  program: Program;

  documentretrieved = false;

  document = '';
  documentType = '';
  error = false;
  nbAttempt = 0;

  kataStatus = '';

  // @ts-ignore
  kataInfo: KataSubscription;
  compiling = false;
  isResolved = false;
  filename = '';
  assertname = '';

  newTry = false;
  katareceived = false;
  isGoal = false;

  getLANG(id: string): void {
    const LANG = this.langservice.getLANG(id)[0];
    this.assertname = LANG.assertname;
    this.filename = LANG.filename;
  }

  sanitzie(url: string): SafeResourceUrl {
    return this.sanitizer.bypassSecurityTrustResourceUrl('data:' + this.documentType + ';base64,' + url);
  }

  getKata(): void {
    this.ngxLoader.start();
    this.programService.getById(this.programid).subscribe((data: Program) => {
      this.program = data;
      if (this.program) {
        this.kataService.get(this.kataid, this.programid).subscribe((datas: Kata) => {
            this.kata = datas;

            if (this.kata.id !== null) {
              if (this.kata.hasfile) {
                this.kataService.getDocument(this.kata.filename, this.programid).subscribe((document: string) => {

                  const ext = this.kata.filename.split('.')[1];
                  switch (ext) {
                    case 'png':
                      this.documentType = 'image/png';
                      break;
                    case 'jpg':
                      this.documentType = 'image/jpg';
                      break;
                    case 'pdf':
                      this.documentType = 'application/pdf';
                      break;
                    default:
                      this.documentType = 'image/png';
                      break;
                  }
                  this.document = document;
                  this.documentretrieved = true;
                });
              }
              if (!(this.kata.title === 'GOALS')) {
                this.kata.keepAssert = !datas.keepAssert;
                this.getLANG(this.kata.language);
              } else {
                this.isGoal = true;
              }
              this.ngxLoader.stop();

            } else {
              this.ngxLoader.stop();
              this.error = true;
            }
          },
          (error1 => {

            this.ngxLoader.stop();
            this.error = true;
            console.log(error1);

          }));
      } else {
        this.ngxLoader.stop();
        this.error = true;
      }
    }, error1 => {
      console.log(error1);
      this.ngxLoader.stop();
      this.error = true;
    });
  }

  newtry() {
    this.newTry = true;
    this.alertService.info('This won\'t affect kata\'s state');
  }

  openDialog(): void {
    if (this.nbAttempt >= this.kata.nbAttempt) {
      const dialogRef = this.dialog.open(KataSurrenderDialogComponent, {
        width: '400px'
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.kataSubscriptionService.update(JSON.stringify({
            kataid: this.kataid,
            programid: this.programid,
            userid: this.auth.currentUserValue.id,
            sol: this.kata.solution,
            status: 'FAILED'
          })).subscribe(() => {
            this.kata.canva = this.kata.solution;
            this.kataStatus = 'FAILED';
            this.isResolved = true;
          });
        }
      });
    } else {
      this.alertService.warning('Oh.. Looks like you did not try enough !\nThe surrender options is unlocked at ' + this.kata.nbAttempt + ' tries for this kata.');
    }
  }

  OnNewEvent(event: any): void {
    this.kata.canva = event.toString();
  }

  compile(language: string, stream: string, assert: string): void {
    this.compiling = true;
    let response: CompilationServiceResponse;
    this.compilationService.compilationServer(JSON.stringify({
      language: this.kata.language,
      stream,
      assert
    })).subscribe((data: CompilationServiceResponse) => {
      response = data;
      if (!this.isResolved && !this.newTry) {
        this.nbAttempt++;
        this.kataSubscriptionService.increment(JSON.stringify({
          kataid: this.kataid,
          programid: this.programid,
          userid: this.auth.currentUserValue.id
        })).subscribe(() => {

          if (response.exit === 0) {
            this.kataStatus = 'RESOLVED';
            this.kataSubscriptionService.update(JSON.stringify({
              kataid: this.kataid,
              programid: this.programid,
              userid: this.auth.currentUserValue.id,
              sol: stream,
              status: this.kataStatus
            })).subscribe(() => {
              this.alertService.success('Executed in : ' + response.time + 'ms');
              this.status = 0;
              this.result = response.output + 'Exercise passed';
              this.isResolved = true;
            });
          } else {
            this.status = 1;
            this.result = response.error;
            this.alertService.danger('Run failed !');
            if (this.nbAttempt === this.kata.nbAttempt) {
              this.alertService.info('Solution unlocked ! you can now surrender peasant.');
            }
          }
          this.compiling = false;
        });
      } else {
        if (response.exit === 0) {
          this.alertService.success('Executed in : ' + response.time + 'ms');
          this.status = 0;
          this.result = response.output + 'Exercise passed';
        } else {
          this.status = 1;
          this.result = response.error;
          this.alertService.danger('Run failed !');
        }
        this.compiling = false;
      }


    });
  }

  getSubscription() {

    this.kataSubscriptionService.isSubscribed(this.auth.currentUserValue.id, this.programid).subscribe((isSubscribed: boolean) => {
      this.kataService.isActivated(this.kataid, this.programid).subscribe((data: boolean) => {
        this.kataService.isClosed(this.kataid, this.programid).subscribe((isClosed: boolean) => {

          this.isClosed = isClosed;
          if (!isSubscribed || !data) {
            this.router.navigate([/kata-displayer/ + this.programid]);
          } else {
            // tslint:disable-next-line:max-line-length
            this.kataSubscriptionService.get(this.kataid, this.programid, this.auth.currentUserValue.id).subscribe((kata: KataSubscription) => {
              this.kataInfo = kata;
              if (this.kataInfo.id === null) {
                let newStatus = 'ONGOING';

                if (this.isGoal) {
                  newStatus = 'DONE';
                }

                this.kataSubscriptionService.create(JSON.stringify({
                  id: uuid(),
                  status: newStatus,
                  kataid: this.kataid,
                  programid: this.programid,
                  userid: this.auth.currentUserValue.id
                })).subscribe(() => {
                  this.nbAttempt = 0;
                  this.kataStatus = 'ONGOING';
                  if (this.isGoal) {
                    this.kataSubscriptionService.update(JSON.stringify({
                      kataid: this.kataid,
                      programid: this.programid,
                      userid: this.auth.currentUserValue.id,
                      sol: 'read.',
                      status: newStatus
                    })).subscribe(() => {
                      this.katareceived = true;
                    });
                  }
                  this.katareceived = true;
                });
              } else {
                this.nbAttempt = this.kataInfo.nbAttempt;
                this.kataStatus = this.kataInfo.status;
                if (this.kataInfo.status === 'RESOLVED' || this.kataInfo.status === 'FAILED') {
                  this.isResolved = true;
                  this.kata.canva = this.kataInfo.mysol;
                }
                this.katareceived = true;
              }
            }, (error1: any) => {
              console.log(error1);
            });
          }
        });
      });
    });

  }

  ngOnInit() {
    this.programid = this.route.snapshot.paramMap.get('prid') as string;
    this.kataid = this.route.snapshot.paramMap.get('id') as string;

    this.getKata();
    this.getSubscription();
  }
}

@Component({
  selector: 'app-dialog-overview-example-dialog',
  templateUrl: 'kata-dialog-surrender.html',
})
export class KataSurrenderDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<KataSurrenderDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}

