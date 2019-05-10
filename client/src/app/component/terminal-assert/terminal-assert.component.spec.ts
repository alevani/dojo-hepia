import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminalAssertComponent } from './terminal-assert.component';

describe('TerminalAssertComponent', () => {
  let component: TerminalAssertComponent;
  let fixture: ComponentFixture<TerminalAssertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TerminalAssertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TerminalAssertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
