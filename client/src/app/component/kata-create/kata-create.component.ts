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
import {MatSnackBar} from '@angular/material';
import {CompilationServiceResponse} from '../../services/compilation/compilationServiceResponse';

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
              private auth: AuthenticationService,
              private snackBar: MatSnackBar
  ) {
    this.CreateForm = this.formBuilder.group({
      title: ['', Validators.required],
      assert: ['', Validators.required],
      number: ['', Validators.min(0)],
      instruction: ['', Validators.required],
      document: ['', null]
    });
    this.program = null;
    this.fileData = new File([], '');
  }

  language = '';
  assert = '';

  status = 2;
  title = '';

  result = '';
  canva = '';
  solution = '';

  programId = '';

  CreateForm: FormGroup;
  submitted = false;

  compiling = false;

  program: Program | null;
  inforreceived = false;
  error = false;

  choiceMK = true;
  fileData: File;

  get f() {
    return this.CreateForm.controls;
  }

  onFileChange(event: any) {
    if (event.target.files.length > 0) {
      this.fileData = event.target.files[0];
    }
  }

  toggleChoice() {
    this.choiceMK = !this.choiceMK;
    const instruction = this.CreateForm.get('instruction') as FormGroup;
    const document = this.CreateForm.get('document') as FormGroup;

    if (this.choiceMK) {
      instruction.setValidators([Validators.required]);
      document.setValidators(null);
    } else {
      instruction.setValidators(null);
      document.setValidators([Validators.required]);
    }
    instruction.updateValueAndValidity();
    document.updateValueAndValidity();
  }

  getLANG(id: string): void {
    const LANG = this.langservice.getLANG(id)[0] as Canva;
    this.assert = LANG.assertCanva;
    this.solution = LANG.codeCanva;
    this.canva = LANG.codeCanva;
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

      const exts = this.fileData.name.split('.');
      const ext = exts[exts.length - 1];

      if (!(ext === 'pdf' || ext === 'png' || ext === 'jpg')) {
        this.snackBar.open('ERROR: Document should be PDF (preferred), PNG or JPG', '', {
          duration: 4000
        });
        return;
      } else if (this.fileData.size > 15000000) {
        this.snackBar.open('ERROR: Document should be less than 15 MB', '', {
          duration: 4000
        });
        return;
      }


      const formData = new FormData();
      formData.append('file', this.fileData);
      this.kataService.upload(formData, this.programId).subscribe((data: string) => {
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
    })).subscribe((data: CompilationServiceResponse) => {
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
        }, ((error1: any) => {
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

    this.programId = this.route.snapshot.paramMap.get('id') as string;
    this.getProg();
  }

}
