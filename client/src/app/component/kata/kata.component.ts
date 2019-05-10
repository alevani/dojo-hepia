import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {FetchKataService} from '../../services/kata/fetch-kata.service';
import {Kata} from './kata';
import {CompilationService} from '../../services/compilation/compilation.service';
import {FetchProgramIdService} from '../../services/program/fetch-program-id.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {Canva} from '../../languages_canvas';
import {LANGService} from '../../services/LANG/lang.service';
import {AlertService} from 'ngx-alerts';


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

  nbAttempt = 0;

  filename = '';
  assertname = '';

  katareceived = false;
  assert = true;

  LANG: Canva;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataService: FetchKataService,
    private compilationService: CompilationService,
    private fetchProgramDetailsService: FetchProgramIdService,
    private ngxLoader: NgxUiLoaderService,
    private langservice: LANGService,
    private alertService: AlertService
  ) {
  }


  getLANG(id: string): void {
    this.LANG = this.langservice.getLANG(id)[0];
    this.assertname = this.LANG.assertname;
    this.filename = this.LANG.filename;
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
          this.assert = !datas.keepAssert;
          this.getLANG(this.kata.language);
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

  Surrender() {
    alert('Oops.. this functionality has not been implemented yet :( !');
  }

  OnNewEvent(event: any): void {
    this.kata.canva = event.toString();
  }

  compile(language: string, stream: string, assert: string): void {
    let response;
    this.nbAttempt++;
    this.compilationService.compilationServer(JSON.stringify({
      language: this.kata.language,
      stream,
      assert
    })).subscribe((data: string) => {
      console.log(data);
      response = data;
      if (response.exit === 0) {
        this.alertService.success('Executed in : ' + response.time + 'ms');
        this.status = 0;
        this.result = response.output + 'Exercise passed';
      } else {
        this.status = 1;
        this.result = response.error;
        this.alertService.danger('Run failed !');
      }
    });
  }

  ngOnInit() {
    this.getKata();
  }

}
