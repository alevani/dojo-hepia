import {Injectable} from '@angular/core';
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class CreateProgramService {

  createProgram(obj: string): string {
    let response = '';

    $.ajax({
      url: 'http://localhost:7000/program/create',
      type: 'POST',
      async: false,
      data: obj,
      contentType: 'application/json',
      success(data) {
        response = data;
      }

    });

    return response;
  }

  constructor() {
  }
}

