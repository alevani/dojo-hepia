import {Injectable} from '@angular/core';
import * as $ from 'jquery';
import {stringify} from 'querystring';
import {Kata} from './kata/kata';

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
