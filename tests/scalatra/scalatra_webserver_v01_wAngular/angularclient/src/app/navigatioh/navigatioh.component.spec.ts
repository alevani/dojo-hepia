import { LayoutModule } from '@angular/cdk/layout';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatIconModule,
  MatListModule,
  MatSidenavModule,
  MatToolbarModule,
} from '@angular/material';

import { NavigatiohComponent } from './navigatioh.component';

describe('NavigatiohComponent', () => {
  let component: NavigatiohComponent;
  let fixture: ComponentFixture<NavigatiohComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NavigatiohComponent],
      imports: [
        NoopAnimationsModule,
        LayoutModule,
        MatButtonModule,
        MatIconModule,
        MatListModule,
        MatSidenavModule,
        MatToolbarModule,
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavigatiohComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should compile', () => {
    expect(component).toBeTruthy();
  });
});
