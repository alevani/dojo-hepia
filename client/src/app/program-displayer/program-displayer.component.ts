import {Component, OnInit} from '@angular/core';
import {FetchProgramService} from '../fetch-program.service';
import {Program} from './program';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-program-displayer',
  templateUrl: './program-displayer.component.html',
  styleUrls: ['./program-displayer.component.scss']
})
export class ProgramDisplayerComponent implements OnInit {


  programs: Program[];
  programReceivedFailed = false;

  constructor(private fetchProgramService: FetchProgramService, private ngxLoader: NgxUiLoaderService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.fetchProgramService.getPrograms().subscribe((data: Program[]) => {
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
