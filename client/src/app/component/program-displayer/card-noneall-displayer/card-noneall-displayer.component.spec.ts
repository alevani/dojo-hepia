import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CardNoneallDisplayerComponent } from './card-noneall-displayer.component';

describe('CardNoneallDisplayerComponent', () => {
  let component: CardNoneallDisplayerComponent;
  let fixture: ComponentFixture<CardNoneallDisplayerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CardNoneallDisplayerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardNoneallDisplayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
