import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class KataService {

  publish(kata: string, programid: string) {
    return this.http.post('http://localhost:7000/kata/create', {kata, programid});
  }

  getKatasDetails(program: string, userid: string) {
    return this.http.get('http://localhost:7000/kata/details/' + program + '/' + userid);
  }

  getKata(kataid: string) {
    return this.http.get('http://localhost:7000/kata/' + kataid + '');
  }

  delete(kataid: string) {
    return this.http.post('http://localhost:7000/kata/delete/', kataid);
  }

  deactivate(kataid: string) {
    return this.http.post('http://localhost:7000/kata/toggleactivation', kataid);
  }

  isOwner(kataid: string, userid: string) {
    return this.http.get('http://localhost:7000/kata/isowner/' + kataid + '/' + userid);
  }

  update(obj: string) {
    return this.http.post('http://localhost:7000/kata/update', obj);
  }

  constructor(private http: HttpClient) {
  }
}
