import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RequestJwtService {

  constructor(private http: HttpClient) {
  }

  getJWT() {

    return this.http.get('http://localhost:7000/jwt/request/Shodai/Shodai');

  }
}
