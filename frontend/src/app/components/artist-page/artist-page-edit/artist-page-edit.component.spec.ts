import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ArtistPageEditComponent} from './artist-page-edit.component';

describe('ArtistPageEditComponent', () => {
  let component: ArtistPageEditComponent;
  let fixture: ComponentFixture<ArtistPageEditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArtistPageEditComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistPageEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
