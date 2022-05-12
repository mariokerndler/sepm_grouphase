import { TestBed } from '@angular/core/testing';

import { FakerGeneratorService } from './faker-generator.service';

describe('FakerGeneratorService', () => {
  let service: FakerGeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FakerGeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
