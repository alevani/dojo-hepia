import {Component, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import * as $ from 'jquery';


@Component({
  selector: 'app-navigatioh',
  templateUrl: './navigatioh.component.html',
  styleUrls: ['./navigatioh.component.css']
})
export class NavigatiohComponent implements OnInit {

  test_var = 'VARIABLE DE TEST';

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches)
    );

  constructor(private breakpointObserver: BreakpointObserver) {
  }

  menu = undefined;

  ngOnInit(): void {
    $(document).ready(() => {
      $.get('http://localhost:7000/test', (data) => {
        menu = data;
      });
    });
  }
}
