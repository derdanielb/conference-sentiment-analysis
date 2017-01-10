/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ConferenceTwitterService } from './conference-twitter.service';

describe('ConferenceTwitterService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConferenceTwitterService]
    });
  });

  it('should ...', inject([ConferenceTwitterService], (service: ConferenceTwitterService) => {
    expect(service).toBeTruthy();
  }));
});
