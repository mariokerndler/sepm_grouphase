import {TestBed} from '@angular/core/testing';

import {MessageService} from './message.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {ReactiveFormsModule} from '@angular/forms';

describe('MessageService', () => {
  beforeEach(() => TestBed.configureTestingModule({
    imports: [HttpClientTestingModule, RouterTestingModule, ReactiveFormsModule],
  }));

  it('should be created', () => {
    const service: MessageService = TestBed.inject(MessageService);
    expect(service).toBeTruthy();
  });
});
