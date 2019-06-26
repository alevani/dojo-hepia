import {Component, EventEmitter, Inject, OnInit} from '@angular/core';
import {KataShowCase} from './kataShowCase';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';
import {ProgramSubscription} from '../../interfaces/subscriptions/ProgramSubscription';
import {v4 as uuid} from 'uuid';
import {ProgramSubscriptionService} from '../../services/program/subs/program-subscription.service';
import {ProgramService} from '../../services/program/program.service';
import {KataService} from '../../services/kata/kata.service';
import {MAT_DIALOG_DATA, MatBottomSheet, MatSnackBar} from '@angular/material';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Program} from '../program-displayer/program';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';


export interface MoreActionsKata {
  title: string;
  id: string;
  programid: string;
  isActivated: boolean;
  language: string;
  goal: boolean;
}

export interface DuplicateProgram {
  title: string;
  programid: string;
  currentUser: User;
}

export interface PromptPassword {
  title: string;
  programid: string;
}

@Component({
  selector: 'app-kata-displayer',
  styleUrls: ['./kata-displayer.component.scss'],
  templateUrl: './kata-displayer.component.html'
})


export class KataDisplayerComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private kataService: KataService,
    private programService: ProgramService,
    private ngxLoader: NgxUiLoaderService,
    private auth: AuthenticationService,
    private programSubscription: ProgramSubscriptionService,
    private router: Router,
    private snackBar: MatSnackBar,
    public dialog: MatDialog
  ) {
    this.katas = [] as KataShowCase[];
    this.currentUser = this.auth.currentUserValue;
    this.subscription = null;
  }

  katas: KataShowCase[];
  programid = '';

  // @ts-ignore
  program: Program;

  error = false;
  isSubscribed = false;
  currentUser: User;
  inforreceived = false;
  subscription: ProgramSubscription | null;
  subvalue = 'Unsubscribe';

  // Tells if the users has already a registered subscription
  // The subscription could exist even if the user is un subscribed (it keeps the programs data
  // if once the user has been subscribe)
  nullsubs = false;
  isOwner = false;

  openDialogDuplicate(): void {
    this.dialog.open(DuplicateProgramDialogComponent, {
      width: '400px',
      data: {title: this.program.title, currentUser: this.currentUser, programid: this.programid}
    });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(DeleteProgramDialogComponent, {
      width: '400px',
      data: this.program.title
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.programService.delete(this.programid).subscribe(() => this.router.navigate(['program/mine']));
      }
    });
  }

  reloadKatas() {
    this.kataService.getKatasDetails(this.programid, this.currentUser.id).subscribe((datas: KataShowCase[]) => {
      this.katas = datas;
    });
  }

  getIsOwner() {
    this.programService.isOwner(this.programid, this.currentUser.id).subscribe((data: boolean) => {
      if (data) {
        this.isOwner = true;
        this.isSubscribed = true;
      }
      this.getSubs();
    });
  }

  subscriptionWorkflow(): void {
    if (this.nullsubs) {
      this.programSubscription.create(this.currentUser.id, JSON.stringify({
        id: uuid(),
        idprogram: this.programid,
        status: true,
        nbKataDone: 0,
        katas: []
      })).subscribe(() => {
        this.isSubscribed = true;
        this.nullsubs = false;
        this.subvalue = 'Unsubscribe';
        this.snackBar.open('Subscribed to ' + this.program.title, '', {
          duration: 2000
        });
        // tslint:disable-next-line:max-line-length
        this.programSubscription.getSubs(this.programid, this.currentUser.id).subscribe((data: ProgramSubscription) => this.subscription = data);
      });
    } else {
      this.isSubscribed = !this.isSubscribed;
      this.programSubscription.toggle(this.currentUser.id, this.programid).subscribe(() => {
        if (this.isSubscribed) {
          this.subvalue = 'Unsubscribe';
          this.snackBar.open('Subscribed to ' + this.program.title, '', {
            duration: 2000
          });
        } else {
          this.subvalue = 'Subscribe';
          this.snackBar.open('Unsubscribed from ' + this.program.title, '', {
            duration: 2000
          });
        }
      });
    }
  }

  subscribe() {

    if (this.nullsubs || !this.isSubscribed) {
      if (this.program.password !== '') {
        const dialogRef = this.dialog.open(PromptPasswordDialogComponent, {
          width: '400px',
          data: {title: this.program.title, programid: this.programid}
        });

        dialogRef.afterClosed().subscribe(result => {

          if (dialogRef.componentInstance.istrue) {
            this.subscriptionWorkflow();

          }
        });
      } else {
        this.subscriptionWorkflow();
      }
    } else {
      this.subscriptionWorkflow();
    }


  }

  deactivate(id: string): void {
    this.kataService.toggleActivation(id, this.programid).subscribe(() => {
      this.reloadKatas();

    });
  }

  delete(id: string): void {
    this.kataService.delete(id, this.programid).subscribe(() => {
      this.reloadKatas();
    });
  }

  edit(id: string): void {
    this.router.navigate(['kata/edit/' + this.programid + '/' + id + '/' + this.program.language]);
  }

  getSubs() {
    this.programSubscription.getSubs(this.programid, this.currentUser.id).subscribe((data: ProgramSubscription) => {
      this.subscription = data;
      if (this.subscription.id != null) {
        this.isSubscribed = this.subscription.status;
        if (!this.isSubscribed) {
          this.subvalue = 'Subscribe';
        }
        this.nullsubs = false;
      } else {
        this.isSubscribed = false;
        this.subvalue = 'Subscribe';
        this.nullsubs = true;
      }

      this.kataService.getKatasDetails(this.programid, this.currentUser.id).subscribe((datas: KataShowCase[]) => {
        this.katas = datas;
        this.inforreceived = true;
      });
    }, error1 => {
      console.log(error1);
    });
  }

  getKatas() {
    this.ngxLoader.start();
    this.programService.getById(this.programid).subscribe((data: Program) => {

      this.program = data;
      if (this.program.id === null) {
        this.error = true;
        this.inforreceived = true;
      } else {
        this.ngxLoader.stop();
        this.getIsOwner();
      }
    }, (error1 => {
      console.log(error1);
      this.error = true;
      this.ngxLoader.stop();
    }));
  }


  ngOnInit() {
    this.programid = this.route.snapshot.paramMap.get('id') as string;
    this.getKatas();
  }

}

@Component({
  selector: 'app-dialog-overview-example-dialog',
  templateUrl: 'program-dialog-delete.html',
})
export class DeleteProgramDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<DeleteProgramDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: string) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

}


@Component({
  selector: 'app-dialog-overview-example-dialog',
  styleUrls: ['../program-create/program-create.component.scss'],
  templateUrl: 'program-dialog-duplicate.html'
})
export class DuplicateProgramDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<DeleteProgramDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DuplicateProgram,
    private formBuilder: FormBuilder,
    private programService: ProgramService,
    private programSubscription: ProgramSubscriptionService,
    private router: Router) {

    this.DuplicateForm = this.formBuilder.group({
      title: ['', Validators.required],
    });
  }

  submitted = false;

  DuplicateForm: FormGroup;

  get f() {
    return this.DuplicateForm.controls;
  }

  duplicate() {
    this.submitted = true;

    if (this.DuplicateForm.invalid) {
      return;
    }

    const newId = uuid();
    this.programService.duplicate(this.data.programid, newId, this.f.title.value).subscribe(() => {
      this.programSubscription.create(this.data.currentUser.id, JSON.stringify({
        id: uuid(),
        idprogram: newId,
        status: true,
        nbKataDone: 0,
        katas: []
      })).subscribe(() => {
        this.router.navigate(['program/mine']);
        this.dialogRef.close();
      });
    });
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}


@Component({
  selector: 'app-dialog-overview-example-dialog',
  templateUrl: 'program-dialog-prompt-password.html',
  styleUrls: ['../program-create/program-create.component.scss']
})
export class PromptPasswordDialogComponent {

  constructor(
    public dialogRef: MatDialogRef<PromptPasswordDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: PromptPassword,
    private programService: ProgramService,
    private formBuilder: FormBuilder) {
    this.PromptPassword = this.formBuilder.group({
      password: ['', Validators.required],
    });
  }

  error = false;
  loading = false;
  istrue = false;

  submitted = false;

  PromptPassword: FormGroup;

  get f() {
    return this.PromptPassword.controls;
  }

  check(): void {

    this.submitted = true;
    if (this.PromptPassword.invalid) {
      return;
    }

    this.loading = true;

    this.programService.check(this.data.programid, this.f.password.value).subscribe((data: boolean) => {
      if (data) {
        this.error = false;
        this.loading = false;
        this.istrue = true;
        this.dialogRef.close();
      } else {
        this.loading = false;
        this.error = true;
      }
    });

  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
