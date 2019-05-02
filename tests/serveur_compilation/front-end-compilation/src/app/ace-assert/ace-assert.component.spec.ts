import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AceAssertComponent } from './ace-assert.component';

describe('AceAssertComponent', () => {
  let component: AceAssertComponent;
  let fixture: ComponentFixture<AceAssertComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AceAssertComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AceAssertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
