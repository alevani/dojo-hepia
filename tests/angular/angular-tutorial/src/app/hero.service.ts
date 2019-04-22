import {Injectable} from '@angular/core';
import {Hero} from './hero';
import *  as $ from 'jquery';

@Injectable({
  providedIn: 'root'
})

export class HeroService {

  getHeroes(): Hero[] {
    let heroes = new Array<Hero>();

    $.ajax({
      url: 'http://localhost:7000/test',
      type: 'get',
      async: false,
      success(data) {
        Object.entries(data).forEach(
          ([key, value]) => heroes.push(new Hero(Number(key), value as string)));
      }

    });

    return heroes;
  }

  constructor() {
  }
}
