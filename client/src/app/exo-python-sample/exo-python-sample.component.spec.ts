import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExoPythonSampleComponent } from './exo-python-sample.component';

describe('ExoPythonSampleComponent', () => {
  let component: ExoPythonSampleComponent;
  let fixture: ComponentFixture<ExoPythonSampleComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExoPythonSampleComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExoPythonSampleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
