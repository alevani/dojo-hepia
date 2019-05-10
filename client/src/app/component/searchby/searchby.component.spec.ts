import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchbyComponent } from './searchby.component';

describe('SearchbyComponent', () => {
  let component: SearchbyComponent;
  let fixture: ComponentFixture<SearchbyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchbyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchbyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
