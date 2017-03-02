/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ConferenceTweetsService } from './conference-tweets.service';

describe('ConferenceTweetsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConferenceTweetsService]
    });
  });

  it('should ...', inject([ConferenceTweetsService], (service: ConferenceTweetsService) => {
    expect(service).toBeTruthy();
  }));
});
