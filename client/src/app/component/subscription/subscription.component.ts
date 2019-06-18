import {Component, OnInit} from '@angular/core';
import {Program} from '../program-displayer/program';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {ProgramSubscriptionService} from '../../services/program/subs/program-subscription.service';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.css']
})
export class SubscriptionComponent implements OnInit {

  // @ts-ignore
  programs: Program[];

  // @ts-ignore
  programsDone: Program[];

  // @ts-ignore
  programsOngoing: Program[];
  programReceivedFailed = false;

  constructor(private programSubscription: ProgramSubscriptionService, private ngxLoader: NgxUiLoaderService, private auth: AuthenticationService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.programSubscription.getSubscription(this.auth.currentUserValue.id).subscribe((data: Program[]) => {
      this.programs = data;

      if (this.programs.length === 0) {
        this.programReceivedFailed = true;
      }
      this.programsDone = this.programs.filter((x) => x.nbKataDone === x.nbKata);
      this.programsOngoing = this.programs.filter((x) => x.nbKataDone !== x.nbKata);

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
