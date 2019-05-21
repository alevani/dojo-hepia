import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class KataService {

  publish(obj: string) {
    return this.http.post('http://localhost:7000/kata/create', obj);
  }

  getKatasDetails(program: string, userid: string) {
    return this.http.get('http://localhost:7000/kata/details/' + program + '/' + userid);
  }

  getKata(kataid: string) {
    return this.http.get('http://localhost:7000/kata/' + kataid + '');
  }

  constructor(private http: HttpClient) {
  }
}
