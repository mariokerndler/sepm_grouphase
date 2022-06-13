import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionTimelineComponent } from './commission-timeline.component';

describe('CommissionTimelineComponent', () => {
  let component: CommissionTimelineComponent;
  let fixture: ComponentFixture<CommissionTimelineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommissionTimelineComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionTimelineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
