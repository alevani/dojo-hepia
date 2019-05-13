import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

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

  get(kataid: string, programid: string, userid: string) {
    return this.http.get('http://localhost:7000/kata/get/subscriptioninfos/' + userid + '/' + programid + '/' + kataid);
  }

  update(obj: string) {
    return this.http.post('http://localhost:7000/kata/update/subscription', obj);
  }

  constructor(private http: HttpClient) {
  }
}
