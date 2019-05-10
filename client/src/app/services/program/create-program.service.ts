import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})


export class CreateProgramService {


  createProgram(obj: string) {
    return this.http.post('http://localhost:7000/program/create', obj);
  }

  constructor(private http: HttpClient) {
  }
}

