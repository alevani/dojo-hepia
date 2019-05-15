import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProgramSubscriptionService {

  createSubscription(obj: string) {
    return this.http.post('http://localhost:7000/program/createsubscription', obj);
  }

  getMine(userid: string) {
    return this.http.get('http://localhost:7000/subscription/mine/' + userid + '');
  }

  getSubscription(userid: string) {
    return this.http.get('http://localhost:7000/subscription/get/' + userid);
  }

  getSubs(programid: string, userid: string) {
    return this.http.get('http://localhost:7000/program/getsubscription/' + programid + '/' + userid + '');
  }

  toggle(obj: string) {
    return this.http.post('http://localhost:7000/program/togglesubscription/', obj);
  }

  constructor(private http: HttpClient) {
  }
}
