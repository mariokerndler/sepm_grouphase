import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArtistGalleryComponent } from './artist-gallery.component';

describe('ArtistGalleryComponent', () => {
  let component: ArtistGalleryComponent;
  let fixture: ComponentFixture<ArtistGalleryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ArtistGalleryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistGalleryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
