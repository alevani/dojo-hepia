import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgramCreateComponent } from './program-create.component';

describe('ProgramCreateComponent', () => {
  let component: ProgramCreateComponent;
  let fixture: ComponentFixture<ProgramCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgramCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgramCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
