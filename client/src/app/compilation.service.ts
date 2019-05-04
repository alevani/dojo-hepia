import {Injectable} from '@angular/core';
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class CompilationService {
  compilationServer(type: string, stream: string, assert: string): string {
    let response = '';

    $.ajax({
      url: 'http://localhost:7000/run/',
      type: 'get',
      async: false,
      data: {
        language: type,
        code: stream,
        test: assert
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
