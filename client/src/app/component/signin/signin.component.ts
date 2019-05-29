import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SignInService} from '../../services/auth/signin/sign-in.service';
import {v4 as uuid} from 'uuid';
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
  }

  signinForm: FormGroup;

  loading = false;
  submitted = false;
  error = '';
  checked = false;


  toggleChecked() {
    const token = this.signinForm.get('token');
    this.checked = !this.checked;
    if (!this.checked) {
      token.setValidators([null]);
    } else {
      token.setValidators([Validators.required]);
    }
  }

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
    this.signinForm = this.formBuilder.group({
      // firstname: ['', Validators.required],
      // lastname: ['', Validators.required],
      username: ['', Validators.required, Validators.pattern('[A-Za-z0-9-]*')],
      token: ['', null],
      password: ['', Validators.required]
    });

  }

}
