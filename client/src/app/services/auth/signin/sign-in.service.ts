import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SignInService {

  createUser(obj: string) {
    return this.http.post('http://localhost:7000/user/signin', obj);
  }

  constructor(private http: HttpClient) {
  }
}
