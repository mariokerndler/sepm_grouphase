import { Injectable } from '@angular/core';
import faker from '@faker-js/faker';
import {User} from '../dtos/user';
import {Observable, of} from 'rxjs';
import {Tag} from '../dtos/tag';
import {Artwork} from '../dtos/artwork';
import {Gallery} from '../dtos/gallery';
import {Artist} from '../dtos/artist';
import {Sketch} from '../dtos/sketch';
import {Commission} from '../dtos/commission';
import {Review} from '../dtos/review';

@Injectable({
  providedIn: 'root'
})
export class FakerGeneratorService {

  constructor() { }

  private static getRandomFromRange(min: number, max: number): number {
    return Math.random() * (max - min) + min;
  }

  private static getRandomFeedback(amount: number): string[] {
    const feedback: string[] = [];

    for (let i = 0; i < amount; i++) {
      feedback.push(faker.lorem.lines(2));
    }

    return feedback;
  }

  private static fakeUser(id: number): User {
    return  {
      id,
      firstName: faker.name.firstName(),
      lastName: faker.name.lastName(),
      username: faker.internet.userName(),
      email: faker.internet.email(),
      password: faker.internet.password()
    };
  }

  private static fakeUsers(amount: number): User[] {
    const users: User[] = [];

    for (let i = 0; i < amount; i++) {
      users.push(this.fakeUser(i));
    }

    return users;
  }

  private static fakeTag(id: number): Tag {
    return {
      id,
      name: faker.animal.type()
    };
  }

  private static fakeTags(amount: number): Tag[] {
    const tags: Tag[] = [];

    for (let i = 0; i < amount; i++) {
      tags.push(this.fakeTag(i));
    }

    return tags;
  }

  private static fakeGallery(id: number, artistId: number, artworkIds: number[]): Gallery {
    return {
      id,
      artistId,
      artworkIds
    };
  }

  private static fakeArtist(id: number, galleryId: number, artworkAmount: number): Artist {
    const user: User = this.fakeUser(id);

    const artworks: Artwork[] = this.fakeArtworks(artworkAmount);
    const artworkIds = artworks.map((x) => x.id);
    const fakeGallery: Gallery = this.fakeGallery(galleryId, id, artworkIds);
    const fakeCommissions: Commission[] = this.fakeCommissions(3);
    const fakeCommissionIds= fakeCommissions.map((x) => x.id);
    const fakeReviews: Review[] = this.fakeReviews(3, id);
    const fakeReviewIds = fakeReviews.map((x) => x.id);

    return {
      id,
      firstName: user.firstName,
      lastName: user.lastName,
      username: user.username,
      email: user.email,
      password: user.password,
      artworkIds,
      description: faker.lorem.paragraph(3),
      reviewScore: this.getRandomFromRange(0,5),
      galleryId: fakeGallery.id,
      commissionIds: fakeCommissionIds,
      reviewIds: fakeReviewIds
    };
  }

  private static fakeSketch(id: number): Sketch {
    return {
      id,
      image: faker.image.animals()
    };
  }

  private static fakeSketches(amount: number): Sketch[] {
    const sketches: Sketch[] = [];

    for (let i = 0; i < amount; i++) {
      sketches.push(this.fakeSketch(i));
    }
    return sketches;
  }

  private  static fakeReview(id: number, commissionId: number): Review{
    return {
      id,
      reviewText: faker.lorem.paragraph(3),
      commissionId,
      rating: this.getRandomFromRange(0,5)
    };
  }

  private static fakeReviews(amount: number, comissionId: number): Review[]{
    const reviews: Review[] = [];

    for (let i = 0; i < amount; i++) {
      reviews.push(this.fakeReview(i, comissionId));
    }

    return reviews;
  }


  private static fakeArtwork(id: number): Artwork{
    const fakeTags: Tag[] = this.fakeTags(3);
    const sketches: Sketch[] = this.fakeSketches(3);

    return {
      id,
      name: faker.animal.cat(),
      description: faker.lorem.paragraph(3),
      tagIds: fakeTags.map((tag) => tag.id),
      image: faker.image.cats(Math.round(this.getRandomFromRange(400, 600)), Math.round(this.getRandomFromRange(400, 600)), true),
      sketchIds: sketches.map((sketch) => sketch.id)
    };
  }

  private static fakeArtworks(amount: number): Artwork[] {
    const artworks: Artwork[] = [];

    for (let i = 0; i < amount; i++) {
      artworks.push(this.fakeArtwork(i));
    }

    return artworks;
  }

  private static fakeCommission(id: number, artistId: number, userId: number): Commission {
    const fakeArtworks: Sketch[] = this.fakeSketches(3);

    return {
      id,
      artistId,
      userId,
      sketchesShown: 3,
      feedbackSend: 3,
      comArtworkId: 0,
      feedback: this.getRandomFeedback(3),
      price: this.getRandomFromRange(15, 1500),
      instructions: faker.lorem.paragraph(2),
      startDate: faker.date.soon(),
      endDate: faker.date.future(),
      referenceImageIds: fakeArtworks.map((artwork) => artwork.id),
    };
  }

  private static fakeCommissions(amount: number): Commission[] {
    const commissions: Commission[] = [];

    for (let i = 0; i < amount; i++) {
      commissions.push(this.fakeCommission(i, 1, 1));
    }

    return commissions;
  }

  generateFakeUser(id: number): Observable<User> {
    return of(FakerGeneratorService.fakeUser(id));
  }

  generateFakeUserByAmount(amount: number): Observable<User[]> {
    const users: User[] = FakerGeneratorService.fakeUsers(amount);

    return of(users);
  }

  generateFakeTag(id: number): Observable<Tag> {
    return of(FakerGeneratorService.fakeTag(id));
  }

  generateFakeTagByAmount(amount: number): Observable<Tag[]> {
    return of(FakerGeneratorService.fakeTags(amount));
  }

  generateFakeGallery(id: number, galleryId: number, artistId: number): Observable<Gallery> {
    const artworks: Artwork[] = FakerGeneratorService.fakeArtworks(FakerGeneratorService.getRandomFromRange(2, 5));
    const artworkIds = artworks.map((x) => x.id);
    const gallery: Gallery = FakerGeneratorService.fakeGallery(galleryId, artistId, artworkIds);

    return of(gallery);
  }

  generateFakeGalleryByAmount(amount: number): Observable<Gallery[]> {
    const galleries: Gallery[] = [];

    for (let i = 0; i < amount; i++) {
      this.generateFakeGallery(i, i + 1, i + 2).subscribe({
        next: (gallery) => galleries.push(gallery)
      });
    }

    return of(galleries);
  }

  generateFakeArtist(id: number, galleryId: number, artworkAmount: number): Observable<Artist> {
    const artist: Artist = FakerGeneratorService.fakeArtist(id, galleryId, artworkAmount);

    return of(artist);
  }

  generateFakeArtistByAmount(amount: number): Observable<Artist[]> {
    const artists: Artist[] = [];

    for (let i = 0; i < amount; i++) {
      this.generateFakeArtist(i, i + 1, 3).subscribe({
        next: (artist) => artists.push(artist)
      });
    }

    return of(artists);
  }

  generateFakeSketch(id: number): Observable<Sketch> {
    return of(FakerGeneratorService.fakeSketch(id));
  }

  generateFakeSketchByAmount(amount: number): Observable<Sketch[]> {
    return of(FakerGeneratorService.fakeSketches(amount));
  }

  generateFakeReview(id: number, comissionId: number): Observable<Review> {
    return of(FakerGeneratorService.fakeReview(id, comissionId));
  }

  generateFakeReviewsByAmount(amount: number): Observable<Review[]> {
    return of(FakerGeneratorService.fakeReviews(amount, 1));
  }

  generateFakeArtwork(id: number): Observable<Artwork> {
    return of(FakerGeneratorService.fakeArtwork(id));
  }

  generateFakeArtworkByAmount(amount: number): Observable<Artwork[]> {
    return of(FakerGeneratorService.fakeArtworks(amount));
  }

  generateFakeCommission(id: number, artistId: number, userId: number): Observable<Commission> {
    return of(FakerGeneratorService.fakeCommission(id, artistId, userId));
  }

  generateFakeCommissionByAmount(amount: number): Observable<Commission[]> {
    return of(FakerGeneratorService.fakeCommissions(amount));
  }
}
