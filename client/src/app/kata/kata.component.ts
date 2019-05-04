import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {FetchKataService} from '../fetch-kata.service';
import {Kata} from './kata';
import * as $ from 'jquery';
import {CompilationService} from '../compilation.service';

@Component({
  selector: 'app-kata',
  templateUrl: './kata.component.html',
  styleUrls: ['./kata.component.css']
})
export class KataComponent implements OnInit {

  kata: Kata;
  idKata: number;
  status = 2;
  result = '';

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataService: FetchKataService,
    private compilationService: CompilationService
  ) {
  }

  getKata(): void {
    this.idKata = +this.route.snapshot.paramMap.get('id');
    this.kata = this.fetchKataService.getKata(this.idKata)[0];
  }

  OnNewEvent(event: any): void {
    this.kata.canva = event.toString();
  }

  compile(language: string, stream: string, assert: string): void {
    const response = $.parseJSON(this.compilationService.compilationServer(language, stream, assert));

    if (response.exit === 0) {
      this.status = 0;
      this.result = response.output + '\nExercise passed';
    } else {
      this.status = 1;
      this.result = response.error;
    }

    this.result += '\nExecuted in : ' + response.time + 'ms';
  }

  ngOnInit() {
    this.getKata();
  }

}
