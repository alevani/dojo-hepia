import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  getToken(level: string, time: string): Observable<string> {
    return this.http.get<string>('http://localhost:7000/token/generate/' + level + '/' + time);
  }

  constructor(private http: HttpClient) {
  }
}
