import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImageFeedComponent } from './image-feed.component';

describe('ImageFeedComponent', () => {
  let component: ImageFeedComponent;
  let fixture: ComponentFixture<ImageFeedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImageFeedComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImageFeedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
