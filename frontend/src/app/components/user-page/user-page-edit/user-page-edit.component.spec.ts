import {ComponentFixture, TestBed} from '@angular/core/testing';

import {UserPageEditComponent} from './user-page-edit.component';

describe('UserPageEditComponent', () => {
  let component: UserPageEditComponent;
  let fixture: ComponentFixture<UserPageEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserPageEditComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserPageEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
