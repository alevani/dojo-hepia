import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramIdService {

  getDetails(id: string) {
    return this.http.get('http://localhost:7000/program/getdetails/' + id + '');
  }

  constructor(private http: HttpClient) {
  }
}
