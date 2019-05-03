import {Injectable} from '@angular/core';
import {Program} from './program-displayer/program';
import {PROGRAMS} from './program-displayer/programMock';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramService {

  getPrograms(sensei: string): Program[] {
    return PROGRAMS.filter(x => x.sensei === sensei);
  }

  constructor() {
  }
}
