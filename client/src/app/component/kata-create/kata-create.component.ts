import {Component, OnInit} from '@angular/core';
import {Canva} from '../../languages_canvas';
import {LANGService} from '../../services/LANG/lang.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {CompilationService} from '../../services/compilation/compilation.service';
import {v4 as uuid} from 'uuid';
import {KataService} from '../../services/kata/kata.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-kata-create',
  templateUrl: './kata-create.component.html',
  styleUrls: ['./kata-create.component.scss']
})
export class KataCreateComponent implements OnInit {

  constructor(private langservice: LANGService,
              private route: ActivatedRoute,
              private location: Location,
              private kataService: KataService,
              public router: Router,
              private compilationService: CompilationService,
              private formBuilder: FormBuilder,
  ) {
  }

  language = '';
  assert = '';

  status = 2;
  title = '';

  result = '';
  canva = '';
  solution = '';

  programId = '';

  LANG: Canva;

  CreateForm: FormGroup;
  submitted = false;

  compiling = false;


  get f() {
    return this.CreateForm.controls;
  }

  getLANG(id: string): void {
    this.LANG = this.langservice.getLANG(id)[0];
    this.assert = this.LANG.assertCanva;
    this.solution = this.LANG.codeCanva;
    this.canva = this.LANG.codeCanva;
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


  publish(): void {

    this.submitted = true;

    if (this.CreateForm.invalid) {
      return;
    }

    if (this.f.number.value < 0) {
      return;
    }

    this.kataService.publish(JSON.stringify({
      id: uuid(),
      title: this.f.title.value,
      language: this.language,
      canva: this.canva,
      cassert: this.assert,
      solution: this.solution,
      rules: this.f.instruction.value,
      keepAssert: this.f.assert.value,
      nbAttempt: this.f.number.value,
      difficulty: 'Ceinture blanche',
      activated: true

    }), this.programId).subscribe(data => this.router.navigate(['/kata-displayer/' + this.programId + '']));
  }

  try(): void {
    this.compiling = true;
    let response;

    this.compilationService.compilationServer(JSON.stringify({
      language: this.language,
      stream: this.solution,
      assert: this.assert
    })).subscribe((data: string) => {
      response = data;
      if (response.exit === 0) {
        this.status = 0;
        this.result = response.output + '\nTests passed';
      } else {
        this.status = 1;
        this.result = response.error;
      }
      this.compiling = false;
      this.result += '\nExecuted in : ' + response.time + 'ms';
    });
  }

  ngOnInit() {
    this.programId = this.route.snapshot.paramMap.get('id');
    this.language = this.route.snapshot.paramMap.get('language');
    this.getLANG(this.language);

    this.CreateForm = this.formBuilder.group({
      title: ['', Validators.required],
      assert: ['', Validators.required],
      number: ['', Validators.min(0)],
      instruction: ['', Validators.required],
    });

  }

}
