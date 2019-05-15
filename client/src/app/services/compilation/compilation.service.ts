import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CompilationService {

  compilationServer(obj: string) {
    return this.http.post('http://localhost:7000/run/', obj);
  }

  constructor(private http: HttpClient) {
  }
}
