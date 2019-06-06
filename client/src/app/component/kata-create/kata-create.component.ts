import {Component, OnInit} from '@angular/core';
import {Canva} from '../../languages_canvas';
import {LANGService} from '../../services/LANG/lang.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {CompilationService} from '../../services/compilation/compilation.service';
import {v4 as uuid} from 'uuid';
import {KataService} from '../../services/kata/kata.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Program} from '../program-displayer/program';
import {ProgramService} from '../../services/program/program.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {AuthenticationService} from '../../services/auth/authentication.service';

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
              private programService: ProgramService,
              private ngxLoader: NgxUiLoaderService,
              private auth: AuthenticationService
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

  program: Program;
  inforreceived = false;
  error = false;

  choiceMK = true;
  fileData: File;

  get f() {
    return this.CreateForm.controls;
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      this.fileData = event.target.files[0];
    }
  }

  toggleChoice() {
    this.choiceMK = !this.choiceMK;
    const instruction = this.CreateForm.get('instruction');
    const document = this.CreateForm.get('document');

    if (this.choiceMK) {
      document.setValidators([null]);
      instruction.setValidators([Validators.required]);
    } else {
      instruction.setValidators([null]);
      document.setValidators([Validators.required]);
    }
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
    let fpath = '';

    if (!this.choiceMK) {
      const formData = new FormData();
      formData.append('file', this.fileData);
      this.kataService.upload(formData).subscribe((data: string) => {
        fpath = data;
        this.pubWorkflow(fpath);
      });
    } else {
      this.pubWorkflow(fpath);
    }
  }

  pubWorkflow(fpath: string) {
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
      difficulty: 'White belt',
      activated: true,
      hasfile: !this.choiceMK,
      filename: fpath

    }), this.programId, false).subscribe(() => {
      this.router.navigate(['/kata-displayer/' + this.programId + '']);
    });
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

  getProg() {

    this.ngxLoader.start();
    this.programService.isOwner(this.programId, this.auth.currentUserValue.id).subscribe((data: boolean) => {
      if (data) {
        this.programService.getById(this.programId).subscribe((datas: Program) => {

          this.program = datas;
          this.language = this.program.language;
          this.getLANG(this.language);
          this.inforreceived = true;
        }, (error1 => {
          if (error1.status === 404) {
            this.error = true;
            this.ngxLoader.stop();
          }
        }));
      } else {
        this.error = true;
        this.ngxLoader.stop();
      }
    });

  }

  ngOnInit() {
    this.programId = this.route.snapshot.paramMap.get('id');
    this.getProg();
    this.CreateForm = this.formBuilder.group({
      title: ['', Validators.required],
      assert: ['', Validators.required],
      number: ['', Validators.min(0)],
      instruction: ['', null],
      document: ['', null]
    });

  }

}
