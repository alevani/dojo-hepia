import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {KataShowCase} from '../../component/kata-displayer/kataShowCase';
import {Kata} from '../../component/kata/kata';

@Injectable({
  providedIn: 'root'
})
export class KataService {

  publish(kata: string, programid: string, goal: boolean) {
    return this.http.post('http://localhost:7000/kata/create', {kata, programid, goal});
  }

  getKatasDetails(program: string, userid: string): Observable<KataShowCase[]> {
    return this.http.get<KataShowCase[]>('http://localhost:7000/kata/details/' + program + '/' + userid);
  }

  getKata(kataid: string, programid: string): Observable<Kata>{
    return this.http.get<Kata>('http://localhost:7000/kata/' + kataid + '/' + programid);
  }

  delete(kataid: string, programid: string) {
    return this.http.post('http://localhost:7000/kata/delete/', {kid: kataid, pid: programid});
  }

  toggleActivation(kataid: string, programid: string) {
    return this.http.post('http://localhost:7000/kata/toggleactivation', {kid: kataid, pid: programid});
  }

  isOwner(kataid: string, userid: string, programid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/kata/isowner/' + programid + '/' + kataid + '/' + userid);
  }

  update(obj: string, progid: string): Observable<string> {
    return this.http.post<string>('http://localhost:7000/kata/update', {kata: obj, programid: progid});
  }

  isActivated(kataid: string, programid: string): Observable<boolean>{
    return this.http.get<boolean>('http://localhost:7000/kata/isactivated/' + kataid + '/' + programid);
  }

  upload(file: FormData, pid: string): Observable<string> {
    return this.http.post<string>('http://localhost:7000/kata/upload/' + pid, file);
  }

  getDocument(did: string, pid: string): Observable<string>{
    return this.http.get<string>('http://localhost:7000/kata/upload/' + pid + '/' + did);
  }

  constructor(private http: HttpClient) {
  }
}
