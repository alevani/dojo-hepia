import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfilCardComponent } from './profil-card.component';

describe('ProfilCardComponent', () => {
  let component: ProfilCardComponent;
  let fixture: ComponentFixture<ProfilCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProfilCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfilCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
