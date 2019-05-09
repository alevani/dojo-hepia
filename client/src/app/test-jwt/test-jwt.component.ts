import {Component, OnInit} from '@angular/core';
import {RequestJwtService} from '../request-jwt.service';
import {CookieService} from 'ngx-cookie-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-test-jwt',
  templateUrl: './test-jwt.component.html',
  styleUrls: ['./test-jwt.component.css']
})
export class TestJwtComponent implements OnInit {

  cookieValue = 'UNKNOWN';

  constructor(private jwt: RequestJwtService, private cookieService: CookieService, private router: Router) {
  }

  token: string;

  ngOnInit() {
    // this.cookieService.delete('Test');
    this.cookieValue = this.cookieService.get('Test');

    if (!this.cookieValue) {
      this.jwt.getJWT().subscribe((data: string) => {
        this.token = data;
        this.cookieService.set('TOKEN', this.token['jwt']);
      });

    } else {
      this.router.navigate(['/programs']);
    }
  }
}
