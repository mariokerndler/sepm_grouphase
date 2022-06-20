import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CommissionTimeslotsComponent} from './commission-timeslots.component';

describe('CommissionTimeslotsComponent', () => {
  let component: CommissionTimeslotsComponent;
  let fixture: ComponentFixture<CommissionTimeslotsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommissionTimeslotsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionTimeslotsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
