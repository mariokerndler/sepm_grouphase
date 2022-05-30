import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CommissionFeedComponent} from './commission-feed.component';

describe('CommissionFeedComponent', () => {
  let component: CommissionFeedComponent;
  let fixture: ComponentFixture<CommissionFeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommissionFeedComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
