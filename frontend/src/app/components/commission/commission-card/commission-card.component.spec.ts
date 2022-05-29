import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionCardComponent } from './commission-card.component';

describe('CommissionCardComponent', () => {
  let component: CommissionCardComponent;
  let fixture: ComponentFixture<CommissionCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommissionCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
