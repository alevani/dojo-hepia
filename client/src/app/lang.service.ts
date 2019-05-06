import {Injectable} from '@angular/core';
import {Canva, LANG} from './languages_canvas';

@Injectable({
  providedIn: 'root'
})
export class LANGService {

  getLANG(id: string): Canva[] {
    return LANG.filter(x => x.id === id);
  }

  constructor() {
  }
}
