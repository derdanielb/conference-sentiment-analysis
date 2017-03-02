import { Component } from '@angular/core';
import {TwitterSearchService} from "./twitter-search.service";

@Component({
  selector: 'app-twitter-search',
  templateUrl: './twitter-search.component.html',
  styleUrls: ['./twitter-search.component.css'],
  providers: [TwitterSearchService]
})
export class TwitterSearchComponent {

  private hashtag : string = "";
  private tweets : string[] = [];

  constructor(private searchService : TwitterSearchService) { }

  private search() : void {
    if(this.hashtag == "")
      this.tweets = [];
    else
      this.searchService.searchTweets(this.hashtag).then(tweets => this.tweets = tweets);
  }
}
