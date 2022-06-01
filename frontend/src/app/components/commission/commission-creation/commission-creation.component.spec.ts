import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommissionCreationComponent } from './commission-creation.component';

describe('CommissionCreationComponent', () => {
  let component: CommissionCreationComponent;
  let fixture: ComponentFixture<CommissionCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommissionCreationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CommissionCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
