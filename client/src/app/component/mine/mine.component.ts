import {Component, OnInit} from '@angular/core';
import {Program} from '../program-displayer/program';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {ProgramSubscriptionService} from '../../services/program/subs/program-subscription.service';


@Component({
  selector: 'app-mine',
  templateUrl: './mine.component.html',
  styleUrls: ['./mine.component.css']
})
export class MineComponent implements OnInit {

  programs: Program[];
  programReceivedFailed = false;

  constructor(private programSubscription: ProgramSubscriptionService, private ngxLoader: NgxUiLoaderService, private auth: AuthenticationService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.programSubscription.getMine(this.auth.currentUserValue.id).subscribe((data: Program[]) => {
      this.programs = data;
      if (this.programs.length === 0) {
        this.programReceivedFailed = true;
      }
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
