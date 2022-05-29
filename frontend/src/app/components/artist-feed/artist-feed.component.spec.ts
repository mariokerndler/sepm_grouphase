import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ArtistFeedComponent} from './artist-feed.component';

describe('ArtistFeedComponent', () => {
  let component: ArtistFeedComponent;
  let fixture: ComponentFixture<ArtistFeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArtistFeedComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
