import {Component, OnInit} from '@angular/core';
import {CreateProgramService} from '../create-program.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-program-create',
  templateUrl: './program-create.component.html',
  styleUrls: ['./program-create.component.scss']
})
export class ProgramCreateComponent implements OnInit {

  constructor(private createProgramService: CreateProgramService,
              public router: Router) {
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
      sensei: 'Shodai',
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
