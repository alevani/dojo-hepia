import {Component, OnInit} from '@angular/core';
import {CreateProgramService} from '../../services/program/create-program.service';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';
import {CreateSubscriptionService} from '../../services/program/subs/create-subscription.service';
import {v4 as uuid} from 'uuid';

@Component({
  selector: 'app-program-create',
  templateUrl: './program-create.component.html',
  styleUrls: ['./program-create.component.scss']
})
export class ProgramCreateComponent implements OnInit {
  currentUser: User;

  constructor(private createProgramService: CreateProgramService,
              public router: Router,
              private auth: AuthenticationService,
              private createSubService: CreateSubscriptionService) {
    this.currentUser = this.auth.currentUserValue;
  }

  programTitle = '';
  programDescr = '';
  programTags = '';
  programLanguage = 'python';
  programToKata = 0;

  update(event: any) {
    this.programLanguage = event.target.value;
  }

  createProgram(newkata: boolean): void {
    this.createProgramService.createProgram(JSON.stringify({
      id: this.programToKata,
      sensei: this.currentUser.username,
      language: this.programLanguage,
      nbKata: 0,
      title: this.programTitle,
      description: this.programDescr,
      tags: this.programTags.split(','),
      idsensei: this.currentUser.id,
      katas: []
    })).subscribe(() => {
      this.createSubService.createSubscription(JSON.stringify({
        iduser: this.currentUser.id,
        idprogram: this.programToKata,
        status: true,
        nbKataDone: 0,
        katas: []
      })).subscribe(() => {
          if (newkata) {
            this.router.navigate(['/kata_create/' + this.programToKata + '/' + this.programLanguage + '']);
          } else {
            this.router.navigate(['/']);
          }
        }
      );

    });

  }

  ngOnInit() {
    this.programToKata = uuid();
  }

}
