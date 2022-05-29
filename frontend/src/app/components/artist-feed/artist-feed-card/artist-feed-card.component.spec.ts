import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ArtistFeedCardComponent} from './artist-feed-card.component';

describe('ArtistFeedCardComponent', () => {
  let component: ArtistFeedCardComponent;
  let fixture: ComponentFixture<ArtistFeedCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ArtistFeedCardComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ArtistFeedCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
