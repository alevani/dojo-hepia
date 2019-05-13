import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';
import {v4 as uuid} from 'uuid';
import {ProgramSubscriptionService} from '../../services/program/subs/program-subscription.service';
import {ProgramService} from '../../services/program/program.service';

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
              private programSubscription: ProgramSubscriptionService) {
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
    this.programService.createProgram(JSON.stringify({
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
      this.programSubscription.createSubscription(JSON.stringify({
        id: uuid(),
        iduser: this.currentUser.id,
        idprogram: this.programToKata,
        status: true,
        nbKataDone: 0,
        katas: []
      })).subscribe(() => {
          if (newkata) {
            this.router.navigate(['/kata_create/' + this.programToKata + '/' + this.programLanguage + '']);
          } else {
            this.router.navigate(['/program/mine']);
          }
        }
      );

    });

  }

  ngOnInit() {
    this.programToKata = uuid();
  }

}
