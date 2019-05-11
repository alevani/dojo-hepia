import {Component, OnInit} from '@angular/core';
import {KataShowCase} from './kataShowCase';
import {ActivatedRoute, Router} from '@angular/router';
import {Location} from '@angular/common';
import {FetchKataShowCaseService} from '../../services/kata/fetch-kata-show-case.service';
import {FetchProgramIdService} from '../../services/program/fetch-program-id.service';
import {NgxUiLoaderService} from 'ngx-ui-loader';
import {GetSubscriptionService} from '../../services/program/subs/get-subscription.service';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';
import {ProgramSubscription} from '../../interfaces/subscriptions/ProgramSubscription';
import {CreateSubscriptionService} from '../../services/program/subs/create-subscription.service';
import {ToggleSubscriptionService} from '../../services/program/subs/toggle-subscription.service';

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
  programSenseiID: string;
  error = false;
  isSubscribed = false;
  currentUser: User;
  inforreceived = false;
  subscription: ProgramSubscription;
  subvalue = 'Unsubscribe';
  // Tells if the users has already a registered subscription
  // The subscription could exist even if the user is un subscribed (it keeps the programs data
  // if once the user has been subscribe)
  nullsubs: boolean;

  isOwner = false;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private fetchKataShowCaseService: FetchKataShowCaseService,
    private fetchProgramDetailsService: FetchProgramIdService,
    private ngxLoader: NgxUiLoaderService,
    private subsService: GetSubscriptionService,
    private auth: AuthenticationService,
    private createSubsService: CreateSubscriptionService,
    private toggleSubsService: ToggleSubscriptionService
  ) {
  }

  getIsOwner() {
    if (this.currentUser.id === this.programSenseiID) {
      this.isOwner = true;
      this.isSubscribed = true;
    }
  }

  subscribe() {

    if (this.nullsubs) {
      this.createSubsService.createSubscription(JSON.stringify({
        iduser: this.currentUser.id,
        idprogram: this.idProgram,
        status: true,
        nbKataDone: 0,
        katas: []
      })).subscribe(() => {
        this.isSubscribed = true;
        this.nullsubs = false;
        this.subvalue = 'Unsubscribe';
        this.subsService.getSubs(this.idProgram, this.currentUser.id).subscribe((data: ProgramSubscription) => this.subscription = data);
      });
    } else {
      this.isSubscribed = !this.isSubscribed;
      this.toggleSubsService.toggle(JSON.stringify({programid: this.idProgram, userid: this.currentUser.id})).subscribe(() => {
        if (this.isSubscribed) {
          this.subvalue = 'Unsubscribe';
        } else {
          this.subvalue = 'Subscribe';
        }
      });
    }
  }

  getSubs() {
    this.subsService.getSubs(this.idProgram, this.currentUser.id).subscribe((data: ProgramSubscription) => {
      this.subscription = data;
      this.isSubscribed = this.subscription.status;
      if (!this.isSubscribed) {
        this.subvalue = 'Subscribe';
      }
      this.nullsubs = false;
    }, error1 => {
      if (error1.status === 404) {
        this.isSubscribed = false;
        this.subvalue = 'Subscribe';
        this.nullsubs = true;
      }

    });
  }

  getKatas() {

    this.ngxLoader.start();
    this.fetchProgramDetailsService.getDetails(this.idProgram).subscribe((data: string[]) => {
      this.programTitle = data[0];
      this.programLanguage = data[1];
      this.programSensei = data[2];
      console.log(data[3]);
      this.programSenseiID = data[3];

      this.getIsOwner();
      this.getSubs();

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
    this.idProgram = this.route.snapshot.paramMap.get('id');
    this.currentUser = this.auth.currentUserValue;
    this.getKatas();
  }

}
