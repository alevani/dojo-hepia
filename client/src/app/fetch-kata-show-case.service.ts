import {Injectable} from '@angular/core';
import {KataShowCase} from './kata-displayer/kataShowCase';
import * as $ from 'jquery';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FetchKataShowCaseService {

  constructor(private http: HttpClient) {
  }

  getKatasDetails(program: number) {
    return this.http.get('http://localhost:7000/program/getkatas/details/' + program + '');
  }
}
