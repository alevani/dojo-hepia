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
    return this.http.post('http://localhost:7000/subscription', {userid, obj});
  }

  getMine(userid: string): Observable<Program[]> {
    return this.http.get<Program[]>('http://localhost:7000/program/user/' + userid);
  }

  getSubscription(userid: string): Observable<Program[]> {
    return this.http.get<Program[]>('http://localhost:7000/subscription/' + userid);
  }

  getSubs(programid: string, userid: string): Observable<ProgramSubscription> {
    return this.http.get<ProgramSubscription>('http://localhost:7000/subscription/' + programid + '/user/ ' + userid);
  }

  toggle(userid: string, programid: string) {
    return this.http.post('http://localhost:7000/subscription/' + programid + '/user/' + userid + '/toggle', '');
  }

  constructor(private http: HttpClient) {
  }
}
