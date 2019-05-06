import {Injectable} from '@angular/core';
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class CompilationService {
  compilationServer(obj: string): string {
    let response = '';

    $.ajax({
      url: 'http://localhost:7000/run/',
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
