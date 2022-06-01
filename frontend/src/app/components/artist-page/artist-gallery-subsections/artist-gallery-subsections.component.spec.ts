import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ArtistGallerySubsectionsComponent} from './artist-gallery-subsections.component';

describe('ArtistGallerySubsectionsComponent', () => {
  let component: ArtistGallerySubsectionsComponent;
  let fixture: ComponentFixture<ArtistGallerySubsectionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArtistGallerySubsectionsComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistGallerySubsectionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
