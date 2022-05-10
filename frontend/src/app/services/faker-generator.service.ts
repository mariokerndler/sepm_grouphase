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

@Injectable({
  providedIn: 'root'
})
export class FakerGeneratorService {

  constructor() { }

  private static getRandomFromRange(min: number, max: number): number {
    return Math.random() * (max - min) + min;
  }

  private static fakeUser(id: number): User {
    return  {
      id,
      firstName: faker.name.firstName(),
      lastName: faker.name.lastName(),
      userName: faker.internet.userName(),
      email: faker.internet.email(),
      password: faker.internet.password()
    };
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

    return {
      id,
      firstName: user.firstName,
      lastName: user.lastName,
      userName: user.userName,
      email: user.email,
      password: user.password,
      artworkIds,
      description: faker.lorem.paragraph(3),
      reviewScore: this.getRandomFromRange(0,5),
      galleryId: fakeGallery.id,
      commissionIds: [], // TODO: Add commissions
      reviewIds: []      // TODO: ADD reviews
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

  private static fakeArtwork(id: number): Artwork{
    const fakeTags: Tag[] = this.fakeTags(3);
    const sketches: Sketch[] = this.fakeSketches(3);

    return {
      id,
      name: faker.animal.cat(),
      description: faker.lorem.paragraph(3),
      tagIds: fakeTags.map((tag) => tag.id),
      image: faker.image.food(),
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

  // TODO: Finish implementing commisions
  private static fakeCommission(id: number, artistId: number, userId: number, artworkId: number): Commission{
    return {
      artistId: 0,
      comArtworkId: 0,
      endDate: undefined,
      feedback: [],
      feedbackSend: 0,
      id: 0,
      instructions: 'a',
      price: 0,
      referenceImageIds: [],
      sketchesShown: 0,
      startDate: undefined,
      userId: 0
    };
  }


  generateFakeUser(id: number): Observable<User> {
    return of(FakerGeneratorService.fakeUser(id));
  }

  generateFakeUserByAmount(amount: number): Observable<User[]> {
    const users: User[] = [];

    for (let i = 0; i < amount; i++) {
      users.push(FakerGeneratorService.fakeUser(i));
    }

    return of(users);
  }
}
