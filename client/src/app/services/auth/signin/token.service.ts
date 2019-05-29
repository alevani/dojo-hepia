import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  getToken(level: string, time: string) {
    return this.http.get('http://localhost:7000/token/generate/' + level + '/' + time);
  }

  constructor(private http: HttpClient) {
  }
}
