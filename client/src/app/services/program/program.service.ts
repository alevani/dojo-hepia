import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Program} from '../../component/program-displayer/program';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {

  create(obj: string) {
    return this.http.post('http://localhost:7000/program/', obj);
  }

  get(): Observable<Program[]> {
    return this.http.get<Program[]>('http://localhost:7000/program/');
  }

  getById(id: string): Observable<Program> {
    return this.http.get<Program>('http://localhost:7000/program/' + id + '');
  }

  delete(progamid: string) {
    return this.http.delete('http://localhost:7000/program/' + progamid);
  }

  isOwner(programid: string, userid: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/program/' + programid + '/user/' + userid + '/isowner');
  }

  update(programid: string, program: Program) {
    return this.http.put('http://localhost:7000/program/update', {programid, program});
  }

  duplicate(id: string, newId: string, title: string) {
    return this.http.post('http://localhost:7000/program/duplicate', JSON.stringify({programid: id, newprogramid: newId, newtitle: title}));
  }

  check(programid: string, password: string): Observable<boolean> {
    return this.http.get<boolean>('http://localhost:7000/subscription/' + programid + '/checkpassword/' + password);
  }

  constructor(private http: HttpClient) {
  }
}
