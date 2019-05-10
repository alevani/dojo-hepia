import {Component, OnInit} from '@angular/core';
import {CreateProgramService} from '../../services/program/create-program.service';
import {Router} from '@angular/router';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';

@Component({
  selector: 'app-program-create',
  templateUrl: './program-create.component.html',
  styleUrls: ['./program-create.component.scss']
})
export class ProgramCreateComponent implements OnInit {
  currentUser: User;

  constructor(private createProgramService: CreateProgramService,
              public router: Router, private auth: AuthenticationService) {
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
      katas: []
    })).subscribe(() => {
      if (newkata) {
        this.router.navigate(['/kata_create/' + this.programToKata + '/' + this.programLanguage + '']);
      } else {
        this.router.navigate(['/']);
      }
    });

  }

  ngOnInit() {
    this.programToKata = +new Date();
  }

}
