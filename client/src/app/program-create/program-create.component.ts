import {Component, OnInit} from '@angular/core';
import {CreateProgramService} from '../create-program.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-program-create',
  templateUrl: './program-create.component.html',
  styleUrls: ['./program-create.component.css']
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

    this.programToKata = +this.createProgramService.createProgram(JSON.stringify({
      sensei: 'Orestis Pileas Malaspinas',
      language: this.programLanguage,
      nbKata: 0,
      title: this.programTitle,
      descr: this.programDescr,
      tags: this.programTags,
      kata: []
    }));

    if (newkata) {
      this.router.navigate(['/kata_create/' + this.programToKata + '/' + this.programLanguage + '']);
    } else {
      this.router.navigate(['/']);
    }

  }

  ngOnInit() {
  }

}
