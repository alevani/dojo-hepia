import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ToggleSubscriptionService {
  toggle(obj: string) {
    return this.http.post('http://localhost:7000/program/togglesubscription/', obj);
  }

  constructor(private http: HttpClient) {
  }
}
