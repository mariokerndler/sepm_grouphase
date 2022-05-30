import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionDetailsComponent } from './commission-details.component';

describe('CommissionDetailsComponent', () => {
  let component: CommissionDetailsComponent;
  let fixture: ComponentFixture<CommissionDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommissionDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
