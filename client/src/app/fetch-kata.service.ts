import {Injectable} from '@angular/core';

import {Kata} from './kata/kata';
import {KATAS} from './kata/kataMock';

@Injectable({
  providedIn: 'root'
})
export class FetchKataService {

  getKata(kataId: number): Kata[] {
    return KATAS;
  }

  constructor() {
  }

}
