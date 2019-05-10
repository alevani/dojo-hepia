import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminalCodeComponent } from './terminal-code.component';

describe('TerminalCodeComponent', () => {
  let component: TerminalCodeComponent;
  let fixture: ComponentFixture<TerminalCodeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TerminalCodeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TerminalCodeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
