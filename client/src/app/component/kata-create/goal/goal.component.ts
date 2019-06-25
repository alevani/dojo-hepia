import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {ProgramService} from '../../../services/program/program.service';
import {AuthenticationService} from '../../../services/auth/authentication.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {v4 as uuid} from 'uuid';
import {KataService} from '../../../services/kata/kata.service';
import {Program} from '../../program-displayer/program';


@Component({
  selector: 'app-goal',
  templateUrl: './goal.component.html',
  styleUrls: ['../kata-create.component.scss']
})
export class GoalComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private location: Location,
              private programService: ProgramService,
              private auth: AuthenticationService,
              private ngxLoader: NgxUiLoaderService,
              private kataService: KataService) {
    this.GoalForm = this.formBuilder.group({
      instruction: ['', Validators.required]
    });
  }

  GoalForm: FormGroup;
  submitted = false;
  programid = '';
  error = false;
  inforreceived = false;

  // @ts-ignore
  program: Program;

  get f() {
    return this.GoalForm.controls;
  }

  publish(): void {

    this.submitted = true;

    if (this.GoalForm.invalid) {
      return;
    }


    this.kataService.publish(JSON.stringify({
      id: uuid(),
      title: 'GOALS',
      difficulty: 'Introduction',
      rules: this.f.instruction.value,
      activated: true

    }), this.programid, true).subscribe(data => this.router.navigate(['/kata-displayer/' + this.programid + '']));
  }

  getProg() {

    this.ngxLoader.start();
    this.programService.isOwner(this.programid, this.auth.currentUserValue.id).subscribe((data: boolean) => {
      if (data) {
        this.programService.getById(this.programid).subscribe((datas: Program) => {

          this.program = datas;
          if (this.program) {
            this.inforreceived = true;
          } else {
            this.error = true;
            this.ngxLoader.stop();
          }

        }, ((error1: any) => {
          console.log(error1);
          this.error = true;
          this.ngxLoader.stop();
        }));
      } else {
        this.error = true;
        this.ngxLoader.stop();
      }
    });

  }

  ngOnInit() {
    this.programid = this.route.snapshot.paramMap.get('id') as string;
    this.getProg();
  }

}
