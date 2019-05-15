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


  programs: Program[];
  programReceivedFailed = false;

  constructor(private programService: ProgramService, private ngxLoader: NgxUiLoaderService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.programService.getPrograms().subscribe((data: Program[]) => {
      this.programs = data;
      this.ngxLoader.stop();
    }, error1 => {
      if (error1.status === 404) {
        this.programReceivedFailed = true;
        this.ngxLoader.stop();
      }
    });
  }

  ngOnInit() {
    this.getProgram();
  }

}
