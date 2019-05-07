import {Component, OnInit} from '@angular/core';
import {Canva} from '../languages_canvas';
import {LANGService} from '../lang.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {CreateKataService} from '../create-kata.service';
import {CompilationService} from '../compilation.service';

@Component({
  selector: 'app-kata-create',
  templateUrl: './kata-create.component.html',
  styleUrls: ['./kata-create.component.css']
})
export class KataCreateComponent implements OnInit {

  constructor(private langservice: LANGService,
              private route: ActivatedRoute,
              private location: Location,
              private kateCreateService: CreateKataService,
              public router: Router,
              private compilationService: CompilationService
  ) {
  }

  code = '';
  language = ''; // TODO get from todo 3
  assert = '';

  status = 2;
  title = '';

  result = '';
  canva = '';
  solution = '';
  rules = '';

  programId = 0;

  LANG: Canva;

  keepAssertForKata = true;
  numberOfAttempt = 0;

  getLANG(id: string): void {
    this.LANG = this.langservice.getLANG(id)[0];
    this.assert = this.LANG.assertCanva;
  }

  OnNewEventAssert(event: any): void {
    this.assert = event.toString();
  }

  OnNewEventCanva(event: any): void {
    this.canva = event.toString();
  }

  OnNewEventSolution(event: any): void {
    this.solution = event.toString();
  }

  UpdateChoice(event: any): void {
    this.keepAssertForKata = event.target.value;
  }

  publish(): void {

    this.kateCreateService.publish(JSON.stringify({
      id: +new Date(),
      title: this.title,
      language: this.language,
      canva: this.canva,
      cassert: this.assert,
      solution: this.solution,
      rules: this.rules,
      keepAssert: this.keepAssertForKata,
      nbAttempt: this.numberOfAttempt,
      programID: this.programId,
      difficulty: 'Ceinture blanche'
    })).subscribe();

    this.router.navigate(['/kata-displayer/' + this.programId + '']);
  }

  try(): void {
    let response;

    this.compilationService.compilationServer(JSON.stringify({
      language: this.language,
      stream: this.solution,
      assert: this.assert
    })).subscribe((data: string) => {
      console.log(data);
      response = data;
      if (response.exit === 0) {
        this.status = 0;
        this.result = response.output + '\nTests passed';
      } else {
        this.status = 1;
        this.result = response.error;
      }

      this.result += '\nExecuted in : ' + response.time + 'ms';
    });
  }

  ngOnInit() {
    this.programId = +this.route.snapshot.paramMap.get('id');
    this.language = this.route.snapshot.paramMap.get('language');
    this.getLANG(this.language);
  }

}
