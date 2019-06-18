import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CompilationServiceResponse} from './compilationServiceResponse';

@Injectable({
  providedIn: 'root'
})
export class CompilationService {

  compilationServer(obj: string): Observable<CompilationServiceResponse> {
    return this.http.post<CompilationServiceResponse>('http://localhost:7000/kata/run/', obj);
  }

  constructor(private http: HttpClient) {
  }
}
