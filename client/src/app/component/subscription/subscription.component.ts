import {Component, OnInit} from '@angular/core';
import {Program} from '../program-displayer/program';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {FetchSubscriptionService} from '../../services/program/subs/fetch-subscription.service';
import {AuthenticationService} from '../../services/auth/authentication.service';

@Component({
  selector: 'app-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.css']
})
export class SubscriptionComponent implements OnInit {

  programs: Program[];
  programReceivedFailed = false;

  constructor(private fetchSubscriptionService: FetchSubscriptionService, private ngxLoader: NgxUiLoaderService, private auth: AuthenticationService) {
  }

  getProgram(): void {

    this.ngxLoader.start();
    this.fetchSubscriptionService.getSubscription(this.auth.currentUserValue.id).subscribe((data: Program[]) => {
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
