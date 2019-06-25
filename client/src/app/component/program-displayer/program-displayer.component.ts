import {Component, OnInit} from '@angular/core';
import {Program} from './program';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {ProgramService} from '../../services/program/program.service';

@Component({
  selector: 'app-program-displayer',
  templateUrl: './program-displayer.component.html',
  styleUrls: ['./program-displayer.component.scss']
})
export class ProgramDisplayerComponent implements OnInit {


  // @ts-ignore
  programs: Program[];
  programReceivedFailed = false;

  constructor(private programService: ProgramService, private ngxLoader: NgxUiLoaderService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.programService.get().subscribe((data: Program[]) => {
      this.programs = data;
      if (this.programs.length === 0) {
        this.programReceivedFailed = true;
      }
      this.ngxLoader.stop();
    }, (error1: any) => {
      console.log(error1);
    });
  }

  ngOnInit() {
    this.getProgram();
  }

}
