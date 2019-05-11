import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CreateSubscriptionService {

  createSubscription(obj: string) {
    return this.http.post('http://localhost:7000/program/createsubscription', obj);
  }

  constructor(private http: HttpClient) {
  }
}
