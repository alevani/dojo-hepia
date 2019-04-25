import {Injectable} from '@angular/core';
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class CompilationService {
  compile(type: string, stream: string): string {
    let response = '';

    $.ajax({
      url: 'http://localhost:7000/run/',
      type: 'get',
      async: false,
      data: {
        language: type,
        code: stream
      },
      success(data) {
        response = data;
      }

    });

    return response;
  }

  constructor() {
  }
}
