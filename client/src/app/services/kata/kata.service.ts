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

  getKata(kataid: string, programid: string) {
    return this.http.get('http://localhost:7000/kata/' + kataid + '/' + programid);
  }

  delete(kataid: string, programid: string) {
    return this.http.post('http://localhost:7000/kata/delete/', {kid: kataid, pid: programid});
  }

  toggleActivation(kataid: string, programid: string) {
    return this.http.post('http://localhost:7000/kata/toggleactivation', {kid: kataid, pid: programid});
  }

  isOwner(kataid: string, userid: string, programid: string) {
    return this.http.get('http://localhost:7000/kata/isowner/' + programid + '/' + kataid + '/' + userid);
  }

  update(obj: string, progid: string) {
    return this.http.post('http://localhost:7000/kata/update', {kata: obj, programid: progid});
  }

  isActivated(kataid: string, programid: string) {
    return this.http.get('http://localhost:7000/kata/isactivated/' + kataid + '/' + programid);
  }

  constructor(private http: HttpClient) {
  }
}
