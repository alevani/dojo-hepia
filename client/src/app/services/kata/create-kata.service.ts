import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class CreateKataService {
  publish(obj: string) {
    return this.http.post('http://localhost:7000/kata/create', obj);
  }

  constructor(private http: HttpClient) {
  }
}
