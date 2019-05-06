import {Injectable} from '@angular/core';
import {Kata} from './kata/kata';
import * as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})
export class CreateKataService {

  publish(obj: string): void {

    $.ajax({
      url: 'http://localhost:7000/kata/create',
      type: 'POST',
      async: false,
      data: obj,
      contentType: 'application/json'
    });

  }

  constructor() {
  }
}
