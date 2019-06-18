import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {KataSubscription} from '../../interfaces/subscriptions/KataSubscription';

@Injectable({
  providedIn: 'root'
})
export class KataSubscriptionService {

  create(obj: string) {
    return this.http.post('http://localhost:7000/kata/create/subscription', obj);
  }

  increment(obj: string) {
    return this.http.post('http://localhost:7000/kata/inc/subscription', obj);

  }

  get(kataid: string, programid: string, userid: string): Observable<KataSubscription>{
    return this.http.get<KataSubscription>('http://localhost:7000/kata/get/subscriptioninfos/' + userid + '/' + programid + '/' + kataid);
  }

  update(obj: string) {
    return this.http.post('http://localhost:7000/kata/update/subscription', obj);
  }

  isSubscribed(userid: string, programid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/program/issubscribed/' + userid + '/' + programid);
  }

  constructor(private http: HttpClient) {
  }
}
