import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SignInService} from '../../services/auth/signin/sign-in.service';
import {v4 as uuid} from 'uuid';
// @ts-ignore
import * as sha1 from 'js-sha1';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.css']
})
export class SigninComponent implements OnInit {

  constructor(private authenticationService: AuthenticationService,
              private router: Router,
              private formBuilder: FormBuilder,
              private signinService: SignInService) {
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/']);
    }
    this.signinForm = this.formBuilder.group({
      username: ['', Validators.required, Validators.pattern('[A-Za-z0-9-]*')],
      token: ['', Validators.required],
      password: ['', Validators.required],
      passwordconf: ['', Validators.required]
    });
  }

  signinForm: FormGroup;
  password = '';
  loading = false;
  submitted = false;
  error = '';

  get f() {
    return this.signinForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.signinForm.invalid) {
      return;
    }

    this.signinService.createUser(JSON.stringify({
      id: uuid(),
      username: this.f.username.value,
      password: sha1(this.f.password.value),
      token: this.f.token.value
    })).subscribe(data => {
        this.router.navigate(['/login']);
      },
      error => {
        this.error = error.error;
      });

  }


  ngOnInit() {
  }

}
