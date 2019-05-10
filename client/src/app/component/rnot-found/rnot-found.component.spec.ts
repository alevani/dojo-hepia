import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RNotFoundComponent } from './rnot-found.component';

describe('RNotFoundComponent', () => {
  let component: RNotFoundComponent;
  let fixture: ComponentFixture<RNotFoundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RNotFoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RNotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
