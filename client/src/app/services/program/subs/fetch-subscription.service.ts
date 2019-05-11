import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FetchSubscriptionService {
  getSubscription(userid: string) {
    return this.http.get('http://localhost:7000/subscription/get/' + userid);
  }

  constructor(private http: HttpClient) {
  }
}
