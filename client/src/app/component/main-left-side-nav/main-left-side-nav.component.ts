import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AuthenticationService} from '../../services/auth/authentication.service';
import {User} from '../../_helper/_models/user';
import {Role} from '../../_helper/_models/roles';


@Component({
  selector: 'app-main-left-side-nav',
  templateUrl: './main-left-side-nav.component.html',
  styleUrls: ['./main-left-side-nav.component.scss']
})
export class MainLeftSideNavComponent {

  currentUser: User;
  Role = Role;
  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  logout() {
    this.auth.logout();
    location.reload();
  }

  constructor(private breakpointObserver: BreakpointObserver, private auth: AuthenticationService) {
    this.currentUser = this.auth.currentUserValue;

  }

}
