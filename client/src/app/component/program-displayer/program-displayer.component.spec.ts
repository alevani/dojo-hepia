import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramDisplayerComponent } from './program-displayer.component';

describe('ProgramDisplayerComponent', () => {
  let component: ProgramDisplayerComponent;
  let fixture: ComponentFixture<ProgramDisplayerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgramDisplayerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgramDisplayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
