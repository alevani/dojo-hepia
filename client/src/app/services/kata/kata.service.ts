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
    return this.http.post('http://localhost:7000/program/' + programid + '/kata', {kata, goal});
  }

  getKatasDetails(program: string, userid: string): Observable<KataShowCase[]> {
    return this.http.get<KataShowCase[]>('http://localhost:7000/program/' + program + '/user/' + userid + '/subscription');
  }

  get(kataid: string, programid: string): Observable<Kata> {
    return this.http.get<Kata>('http://localhost:7000/program/' + programid + '/kata/' + kataid);
  }

  delete(kataid: string, programid: string) {
    return this.http.delete('http://localhost:7000/program/' + programid + '/kata/' + kataid);
  }

  toggleActivation(kataid: string, programid: string) {
    return this.http.put('http://localhost:7000/program/' + programid + '/kata/' + kataid + '/toggleactivation', '');
  }

  toggleIsClosed(kataid: string, programid: string) {
    return this.http.put('http://localhost:7000/program/' + programid + '/kata/' + kataid + '/toggleisclosed', '');
  }

  isOwner(kataid: string, userid: string, programid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/program/' + programid + '/kata/' + kataid + '/user/' + userid + '/isowner');
  }

  update(obj: string, programid: string): Observable<string> {
    return this.http.put<string>('http://localhost:7000/program/' + programid + '/kata', {kata: obj});
  }

  isActivated(kataid: string, programid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/program/' + programid + '/kata/' + kataid + '/isactivated');
  }

  isClosed(kataid: string, programid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/program/' + programid + '/kata/' + kataid + '/isclosed');
  }

  upload(file: FormData, programid: string): Observable<string> {
    return this.http.post<string>('http://localhost:7000/program/' + programid + '/kata/upload/', file);
  }

  getDocument(documentid: string, programid: string): Observable<string> {
    return this.http.get<string>('http://localhost:7000/program/' + programid + '/kata/upload/' + documentid);
  }

  constructor(private http: HttpClient) {
  }
}
