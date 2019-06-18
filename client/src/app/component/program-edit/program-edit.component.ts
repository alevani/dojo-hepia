import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ProgramService} from '../../services/program/program.service';
import {ActivatedRoute, Router} from '@angular/router';
import {Program} from '../program-displayer/program';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';

@Component({
  selector: 'app-program-edit',
  templateUrl: './program-edit.component.html',
  styleUrls: ['../program-create/program-create.component.scss']
})
export class ProgramEditComponent implements OnInit {

  constructor(private formBuilder: FormBuilder,
              private programService: ProgramService,
              private router: Router,
              private auth: AuthenticationService,
              private route: ActivatedRoute,
              private ngxLoader: NgxUiLoaderService) {
    this.UpdateForm = this.formBuilder.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      tags: ['', Validators.required],
      password: ['', null],
    });
  }

  UpdateForm: FormGroup;
  // @ts-ignore
  program: Program;
  submitted = false;
  programid = '';
  checked = false;

  get f() {
    return this.UpdateForm.controls;
  }

  toggleChecked() {
    const password = this.UpdateForm.get('password') as FormGroup;
    this.checked = !this.checked;
    if (!this.checked) {
      password.setValidators(null);
    } else {
      password.setValidators([Validators.required]);
    }
  }

  save() {
    this.program.description = this.f.description.value;
    this.program.title = this.f.title.value;
    this.program.tags = this.f.tags.value.toString().split(',');
    if (this.checked) {
      this.program.password = this.f.password.value;
    } else {
      this.program.password = '';
    }
    this.programService.update(this.programid, this.program).subscribe(() => this.router.navigate(['/kata-displayer/' + this.programid + '']));
  }

  ngOnInit() {
    this.ngxLoader.start();
    this.programid = this.route.snapshot.paramMap.get('id') as string;
    this.programService.isOwner(this.programid, this.auth.currentUserValue.id).subscribe((data: boolean) => {
      if (!data) {
        this.router.navigate(['/']);
      } else {
        this.programService.getById(this.programid).subscribe((program: Program) => {
          this.program = program;
          if (this.program.password !== '') {
            this.checked = true;
          }
          this.ngxLoader.stop();
        });
      }
    });
  }

}
