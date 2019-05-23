import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Program} from '../../component/program-displayer/program';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {

  createProgram(obj: string) {
    return this.http.post('http://localhost:7000/program/create', obj);
  }

  getPrograms() {
    return this.http.get('http://localhost:7000/program/details');
  }

  getDetails(id: string) {
    return this.http.get('http://localhost:7000/program/details/' + id + '');
  }

  deleteProgram(id: string) {
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
