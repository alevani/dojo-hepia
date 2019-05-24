import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {KataService} from '../../services/kata/kata.service';
import {CompilationService} from '../../services/compilation/compilation.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Kata} from '../kata/kata';
import {AuthenticationService} from '../../services/auth/authentication.service';

@Component({
  selector: 'app-kata-edit',
  templateUrl: './kata-edit.component.html',
  styleUrls: ['../kata-create/kata-create.component.scss']
})
export class KataEditComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private auth: AuthenticationService,
    private location: Location,
    private kataService: KataService,
    public router: Router,
    private compilationService: CompilationService,
    private formBuilder: FormBuilder) {
  }

  kata: Kata;
  EditForm: FormGroup;
  submitted = false;

  compiling = false;
  kataid: string;
  language: string;

  returnUrl: string;

  status = 2;
  result: string;

  get f() {
    return this.EditForm.controls;
  }

  OnNewEventAssert(event: any): void {
    this.kata.cassert = event.toString();
  }

  OnNewEventCanva(event: any): void {
    this.kata.canva = event.toString();
  }

  OnNewEventSolution(event: any): void {
    this.kata.solution = event.toString();
  }

  getKata(): void {
    this.kataService.isOwner(this.kataid, this.auth.currentUserValue.id).subscribe((data: boolean) => {
      if (data) {
        this.kataService.getKata(this.kataid).subscribe((kata: Kata) => {
          this.kata = kata;
        });
      } else {
        this.router.navigate(['/']);
      }
    });
  }

  try(): void {
    this.compiling = true;
    let response;

    this.compilationService.compilationServer(JSON.stringify({
      language: this.language,
      stream: this.kata.solution,
      assert: this.kata.cassert
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

  save(): void {
    this.submitted = true;

    if (this.EditForm.invalid) {
      return;
    }

    if (this.f.number.value < 0) {
      return;
    }

    this.kataService.update(JSON.stringify({
      id: this.kataid,
      title: this.f.title.value,
      language: this.language,
      canva: this.kata.canva,
      cassert: this.kata.cassert,
      solution: this.kata.solution,
      rules: this.f.instruction.value,
      keepAssert: this.f.assert.value,
      nbAttempt: this.f.number.value,
      difficulty: 'Ceinture blanche',
      activated: this.kata.activated
    })).subscribe(() => {
      this.router.navigate([this.returnUrl]);
    });

  }

  ngOnInit() {
    this.kataid = this.route.snapshot.paramMap.get('id');
    this.language = this.route.snapshot.paramMap.get('language');

    this.getKata();
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.EditForm = this.formBuilder.group({
      title: ['', Validators.required],
      assert: ['', null],
      number: ['', Validators.required],
      instruction: ['', Validators.required],
    });
  }


}
