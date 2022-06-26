import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CommissionEditComponent} from './commission-edit.component';

describe('CommissionEditComponent', () => {
  let component: CommissionEditComponent;
  let fixture: ComponentFixture<CommissionEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommissionEditComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
