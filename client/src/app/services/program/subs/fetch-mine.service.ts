import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FetchMineService {

  getMine(userid: string) {
    return this.http.get('http://localhost:7000/subscription/mine/' + userid + '');
  }

  constructor(private http: HttpClient) {
  }
}
