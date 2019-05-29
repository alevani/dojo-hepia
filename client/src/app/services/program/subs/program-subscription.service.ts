import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProgramSubscriptionService {

  create(userid: string, obj: string) {
    return this.http.post('http://localhost:7000/program/createsubscription', {userid, obj});
  }

  getMine(userid: string) {
    return this.http.get('http://localhost:7000/program/' + userid + '');
  }

  getSubscription(userid: string) {
    return this.http.get('http://localhost:7000/program/subscription/' + userid);
  }

  getSubs(programid: string, userid: string) {
    return this.http.get('http://localhost:7000/program/subscription/' + programid + '/' + userid + '');
  }

  toggle(obj: string) {
    return this.http.post('http://localhost:7000/program/togglesubscription/', obj);
  }

  constructor(private http: HttpClient) {
  }
}
