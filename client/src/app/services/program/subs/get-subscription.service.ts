import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GetSubscriptionService {

  constructor(private http: HttpClient) {
  }

  getSubs(programid: string, userid: string) {
    return this.http.get('http://localhost:7000/program/getsubscription/' + programid + '/' + userid + '');
  }
}
