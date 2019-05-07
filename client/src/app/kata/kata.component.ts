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
  styleUrls: ['./kata.component.scss']
})
export class KataComponent implements OnInit {

  kata: Kata;
  idKata: number;
  status = 2;
  result = '';
  programID: number;

  programTitle: string;
  programSensei: string;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataService: FetchKataService,
    private compilationService: CompilationService
  ) {
  }

  getKata(): void {
    this.idKata = +this.route.snapshot.paramMap.get('id');
    this.programID = +this.route.snapshot.paramMap.get('prid');
    this.programSensei = this.route.snapshot.paramMap.get('sensei');
    this.programTitle = this.route.snapshot.paramMap.get('prgtitle');
    this.fetchKataService.getKata(this.programID, this.idKata).subscribe((data: Kata) => {
      this.kata = data;
    });

  }

  OnNewEvent(event: any): void {
    this.kata.canva = event.toString();
  }

  compile(language: string, stream: string, assert: string): void {
    let response;

    this.compilationService.compilationServer(JSON.stringify({
      language: this.kata.language,
      stream: stream,
      assert: assert
    })).subscribe((data: string) => {
      console.log(data);
      response = data;
      if (response.exit === 0) {
        this.status = 0;
        this.result = response.output + '\nExercise passed';
      } else {
        this.status = 1;
        this.result = response.error;
      }

      this.result += '\nExecuted in : ' + response.time + 'ms';
    });
  }

  ngOnInit() {
    this.getKata();
  }

}
