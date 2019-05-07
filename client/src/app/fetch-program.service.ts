import {Injectable} from '@angular/core';
import {Program} from './program-displayer/program';
import {PROGRAMS} from './program-displayer/programMock';
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramService {

  getPrograms(): Program[] {
    let response = '';

    $.ajax({
      url: 'http://localhost:7000/program/getall',
      type: 'get',
      async: false,
      success(data) {
        response = data;
      }

    });

    const obj: Program[] = response as Program;
    return obj;
  }

  constructor() {
  }
}
