import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Program} from '../../component/program-displayer/program';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {

  create(obj: string) {
    return this.http.post('http://localhost:7000/program/create', obj);
  }

  get() {
    return this.http.get('http://localhost:7000/program/details');
  }

  getById(id: string) {
    return this.http.get('http://localhost:7000/program/details/' + id + '');
  }

  delete(id: string) {
    return this.http.post('http://localhost:7000/program/delete', JSON.stringify({programid: id}));
  }

  isOwner(programid: string, userid: string) {
    return this.http.get('http://localhost:7000/program/isowner/' + userid + '/' + programid);
  }

  update(programid: string, program: Program) {
    return this.http.post('http://localhost:7000/program/update', {programid, program});
  }

  constructor(private http: HttpClient) {
  }
}
