import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserPageInformationComponent } from './user-page-information.component';

describe('UserInformationComponent', () => {
  let component: UserPageInformationComponent;
  let fixture: ComponentFixture<UserPageInformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserPageInformationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPageInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
