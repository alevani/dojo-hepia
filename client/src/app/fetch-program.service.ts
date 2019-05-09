import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramService {

  getPrograms() {
    const headers = new Headers();
    headers.append('Authorization', this.cookieService.get('Test'));
    return this.http.get('http://localhost:7000/program/getdetails', headers);
  }


  constructor(private http: HttpClient, private cookieService: CookieService) {
  }
}
