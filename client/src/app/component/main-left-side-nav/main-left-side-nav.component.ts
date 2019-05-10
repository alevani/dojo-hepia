import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AuthenticationService} from '../../services/auth/authentication.service';


@Component({
  selector: 'app-main-left-side-nav',
  templateUrl: './main-left-side-nav.component.html',
  styleUrls: ['./main-left-side-nav.component.scss']
})
export class MainLeftSideNavComponent {

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  logout() {
    this.auth.logout();
    location.reload();
    // TODO unset globlas
  }

  constructor(private breakpointObserver: BreakpointObserver, private auth: AuthenticationService) {
  }

}
