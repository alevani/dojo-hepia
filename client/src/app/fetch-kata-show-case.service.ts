import { Injectable } from '@angular/core';
import {KataShowCase} from './kata-displayer/kataShowCase';
import {KATAS} from './kata-displayer/kataShowCaseMock';

@Injectable({
  providedIn: 'root'
})
export class FetchKataShowCaseService {

  constructor() { }
  getPrograms(program: number): KataShowCase[] {
    return KATAS.filter(x => x.idProgram === program);
  }
}
