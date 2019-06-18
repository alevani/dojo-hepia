import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ProgramSubscription} from '../../../interfaces/subscriptions/ProgramSubscription';
import {Program} from '../../../component/program-displayer/program';

@Injectable({
  providedIn: 'root'
})
export class ProgramSubscriptionService {

  create(userid: string, obj: string) {
    return this.http.post('http://localhost:7000/program/createsubscription', {userid, obj});
  }

  getMine(userid: string): Observable<Program[]> {
    return this.http.get<Program[]>('http://localhost:7000/program/' + userid + '');
  }

  getSubscription(userid: string): Observable<Program[]> {
    return this.http.get<Program[]>('http://localhost:7000/program/subscription/' + userid);
  }

  getSubs(programid: string, userid: string): Observable<ProgramSubscription> {
    return this.http.get<ProgramSubscription>('http://localhost:7000/program/subscription/' + programid + '/' + userid + '');
  }

  toggle(obj: string) {
    return this.http.post('http://localhost:7000/program/togglesubscription/', obj);
  }

  constructor(private http: HttpClient) {
  }
}
