import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramService {

  getPrograms() {
    return this.http.get('http://localhost:7000/program/getdetails');
  }


  constructor(private http: HttpClient) {
  }
}
