import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestJwtComponent } from './test-jwt.component';

describe('TestJwtComponent', () => {
  let component: TestJwtComponent;
  let fixture: ComponentFixture<TestJwtComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestJwtComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestJwtComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
