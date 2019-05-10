import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardDisplayerComponent } from './card-displayer.component';

describe('CardDisplayerComponent', () => {
  let component: CardDisplayerComponent;
  let fixture: ComponentFixture<CardDisplayerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardDisplayerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardDisplayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
