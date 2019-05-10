import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class FetchKataService {

  getKata(programID: string, kataID: string) {
    return this.http.get('http://localhost:7000/program/getkata/' + programID + '/' + kataID + '');
  }

  constructor(private http: HttpClient) {
  }

}
