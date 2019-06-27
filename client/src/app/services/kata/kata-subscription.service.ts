import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {KataSubscription} from '../../interfaces/subscriptions/KataSubscription';

@Injectable({
  providedIn: 'root'
})
export class KataSubscriptionService {

  create(obj: string) {
    return this.http.post('http://localhost:7000/subscription/kata', obj);
  }

  increment(obj: string) {
    return this.http.put('http://localhost:7000/subscription/kata/inc', obj);

  }

  get(kataid: string, programid: string, userid: string): Observable<KataSubscription> {
    return this.http.get<KataSubscription>('http://localhost:7000/subscription/' + programid + '/kata/' + kataid + '/user/' + userid);
  }

  update(obj: string) {
    return this.http.put('http://localhost:7000/subscription/kata', obj);
  }

  isSubscribed(userid: string, programid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/subscription/' + programid + '/user/' + userid + '/issubscribed');
  }

  constructor(private http: HttpClient) {
  }
}
