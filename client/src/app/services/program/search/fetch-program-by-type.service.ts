import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FetchProgramByTypeService {

  getPrograms(type: string, res: string) {
    return this.http.get('http://localhost:7000/search/' + type + '/' + res);
  }


  constructor(private http: HttpClient) {
  }
}
