import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';
import {v4 as uuid} from 'uuid';
import {ProgramSubscriptionService} from '../../services/program/subs/program-subscription.service';
import {ProgramService} from '../../services/program/program.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-program-create',
  templateUrl: './program-create.component.html',
  styleUrls: ['./program-create.component.scss']
})
export class ProgramCreateComponent implements OnInit {
  currentUser: User;

  constructor(private programService: ProgramService,
              public router: Router,
              private auth: AuthenticationService,
              private programSubscription: ProgramSubscriptionService,
              private formBuilder: FormBuilder) {
    this.currentUser = this.auth.currentUserValue;
  }

  CreateForm: FormGroup;

  submitted = false;
  programToKata = 0;

  get f() {
    return this.CreateForm.controls;
  }

  createProgram(newkata: boolean): void {
    this.submitted = true;

    if (this.CreateForm.invalid) {
      return;
    }

    this.programService.create(JSON.stringify({
      id: this.programToKata,
      sensei: this.currentUser.username,
      language: this.f.language.value,
      nbKata: 0,
      title: this.f.title.value,
      description: this.f.description.value,
      tags: this.f.tags.value.split(','),
      idsensei: this.currentUser.id,
      katas: []
    })).subscribe(() => {
      this.programSubscription.create(this.currentUser.id, JSON.stringify({
        id: uuid(),
        idprogram: this.programToKata,
        status: true,
        nbKataDone: 0,
        katas: []
      })).subscribe(() => {
          if (newkata) {
            this.router.navigate(['/kata_create/' + this.programToKata + '/' + this.f.language.value + '']);
          } else {
            this.router.navigate(['/program/mine']);
          }
        }
      );

    });

  }

  ngOnInit() {
    this.programToKata = uuid();

    this.CreateForm = this.formBuilder.group({
      title: ['', Validators.required],
      language: ['', null],
      description: ['', Validators.required],
      tags: ['', Validators.required],
    });
  }

}
