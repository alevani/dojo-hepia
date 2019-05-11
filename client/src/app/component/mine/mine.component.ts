import {Component, OnInit} from '@angular/core';
import {Program} from '../program-displayer/program';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {FetchMineService} from '../../services/program/subs/fetch-mine.service';

@Component({
  selector: 'app-mine',
  templateUrl: './mine.component.html',
  styleUrls: ['./mine.component.css']
})
export class MineComponent implements OnInit {

  programs: Program[];
  programReceivedFailed = false;

  constructor(private fetchMineService: FetchMineService, private ngxLoader: NgxUiLoaderService, private auth: AuthenticationService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.fetchMineService.getMine(this.auth.currentUserValue.id).subscribe((data: Program[]) => {
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
