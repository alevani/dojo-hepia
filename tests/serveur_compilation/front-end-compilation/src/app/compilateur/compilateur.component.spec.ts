import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompilateurComponent } from './compilateur.component';

describe('CompilateurComponent', () => {
  let component: CompilateurComponent;
  let fixture: ComponentFixture<CompilateurComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompilateurComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompilateurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
