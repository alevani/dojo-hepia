import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProgramService {

  createProgram(obj: string) {
    return this.http.post('http://localhost:7000/program/create', obj);
  }

  getPrograms() {
    return this.http.get('http://localhost:7000/program/getdetails');
  }

  getDetails(id: string) {
    return this.http.get('http://localhost:7000/program/getdetails/' + id + '');
  }

  deleteProgram(id: string) {
    return this.http.post('http://localhost:7000/program/delete', JSON.stringify({programid: id}));
  }

  constructor(private http: HttpClient) {
  }
}
