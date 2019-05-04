import {Component, OnInit} from '@angular/core';
import {KataShowCase} from './kataShowCase';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {FetchKataShowCaseService} from '../fetch-kata-show-case.service';

@Component({
  selector: 'app-kata-displayer',
  templateUrl: './kata-displayer.component.html',
  styleUrls: ['./kata-displayer.component.css']
})
export class KataDisplayerComponent implements OnInit {

  katas: KataShowCase[];
  idProgram: number;
  programTitle: string;
  programLanguage: string;
  programSensei: string;
  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataShowCaseService: FetchKataShowCaseService
  ) {
  }

  getKatas(): void {
    this.idProgram = +this.route.snapshot.paramMap.get('id');
    this.programSensei = this.route.snapshot.paramMap.get('sensei');
    this.programLanguage = this.route.snapshot.paramMap.get('language');
    this.programTitle = this.route.snapshot.paramMap.get('title');
    this.katas = this.fetchKataShowCaseService.getPrograms(this.idProgram);
  }

  ngOnInit() {
    this.getKatas();
  }

}
