import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Program} from '../../../component/program-displayer/program';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramByTypeService {

  getPrograms(type: string, res: string): Observable<Program[]> {
    return this.http.get<Program[]>('http://localhost:7000/program/search/' + type + '/' + res);
  }


  constructor(private http: HttpClient) {
  }
}
