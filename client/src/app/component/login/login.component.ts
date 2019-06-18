import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

// @ts-ignore
import * as sha1 from 'js-sha1';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private router: Router,
              private formBuilder: FormBuilder,
              private authenticationService: AuthenticationService) {

    if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/']);
    }
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  loginForm: FormGroup;

  loading = false;
  submitted = false;
  error = '';


  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authenticationService.login(this.f.username.value, sha1(this.f.password.value))
      .pipe(first())
      .subscribe(
        () => {
          location.reload();
        },
        error => {
          this.error = error.error;
          this.loading = false;
        });
  }

  get f() {
    return this.loginForm.controls;
  }

  ngOnInit() {
  }

}
