import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KataCreateComponent } from './kata-create.component';

describe('KataCreateComponent', () => {
  let component: KataCreateComponent;
  let fixture: ComponentFixture<KataCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KataCreateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KataCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
