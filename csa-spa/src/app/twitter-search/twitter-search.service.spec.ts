/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { TwitterSearchService } from './twitter-search.service';

describe('TwitterSearchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TwitterSearchService]
    });
  });

  it('should ...', inject([TwitterSearchService], (service: TwitterSearchService) => {
    expect(service).toBeTruthy();
  }));
});
