import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DeleteArtworkComponent} from './delete-artwork.component';

describe('DeleteArtworkComponent', () => {
  let component: DeleteArtworkComponent;
  let fixture: ComponentFixture<DeleteArtworkComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DeleteArtworkComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteArtworkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
