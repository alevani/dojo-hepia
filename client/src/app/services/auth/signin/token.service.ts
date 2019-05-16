import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  getToken() {
    return this.http.get('http://localhost:7000/token/generate');
  }

  constructor(private http: HttpClient) {
  }
}
