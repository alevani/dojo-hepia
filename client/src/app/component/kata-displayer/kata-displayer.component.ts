import {Component, OnInit} from '@angular/core';
import {KataShowCase} from './kataShowCase';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {FetchKataShowCaseService} from '../../services/kata/fetch-kata-show-case.service';
import {FetchProgramIdService} from '../../services/program/fetch-program-id.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';

@Component({
  selector: 'app-kata-displayer',
  templateUrl: './kata-displayer.component.html',
  styleUrls: ['./kata-displayer.component.scss']
})
export class KataDisplayerComponent implements OnInit {

  katas: KataShowCase[];
  idProgram: string;
  programTitle: string;
  programLanguage: string;
  programSensei: string;
  error = false;

  inforreceived = false;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataShowCaseService: FetchKataShowCaseService,
    private fetchProgramDetailsService: FetchProgramIdService,
    private ngxLoader: NgxUiLoaderService
  ) {
  }

  getKatas(): void {
    this.idProgram = this.route.snapshot.paramMap.get('id');
    this.ngxLoader.start();
    this.fetchProgramDetailsService.getDetails(this.idProgram).subscribe((data: string[]) => {
      this.programTitle = data[0];
      this.programLanguage = data[1];
      this.programSensei = data[2];
      this.inforreceived = true;
      this.fetchKataShowCaseService.getKatasDetails(this.idProgram).subscribe((datas: KataShowCase[]) => {
        this.katas = datas;
        this.ngxLoader.stop();
      });
    }, (error1 => {
      if (error1.status === 404) {
        this.error = true;
        this.ngxLoader.stop();
      }
    }));
  }

  ngOnInit() {
    this.getKatas();
  }

}
