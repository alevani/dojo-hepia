import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {FetchKataService} from '../fetch-kata.service';
import {Kata} from './kata';
import {CompilationService} from '../compilation.service';
import {FetchProgramIdService} from '../fetch-program-id.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';

@Component({
  selector: 'app-kata',
  templateUrl: './kata.component.html',
  styleUrls: ['./kata.component.scss']
})
export class KataComponent implements OnInit {

  kata: Kata;
  idKata: string;
  status = 2;
  result = '';
  programID: string;
  programTitle: string;
  programSensei: string;
  error = false;

  katareceived = false;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataService: FetchKataService,
    private compilationService: CompilationService,
    private fetchProgramDetailsService: FetchProgramIdService,
    private ngxLoader: NgxUiLoaderService
  ) {
  }

  getKata(): void {
    this.ngxLoader.start();
    this.programID = this.route.snapshot.paramMap.get('prid');
    this.fetchProgramDetailsService.getDetails(this.programID).subscribe((data: string[]) => {
      this.idKata = this.route.snapshot.paramMap.get('id');
      this.programSensei = data[2];
      this.programTitle = data[0];
      this.fetchKataService.getKata(this.programID, this.idKata).subscribe((datas: Kata) => {
          this.kata = datas;
          this.ngxLoader.stop();
          this.katareceived = true;
        },
        (error1 => {
          if (error1.status === 404) {
            this.ngxLoader.stop();
            this.error = true;
          }
        }));
    }, error1 => {
      if (error1.status === 404) {
        this.ngxLoader.stop();
        this.error = true;
      }
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
