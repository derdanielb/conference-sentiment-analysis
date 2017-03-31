import { Component, OnInit } from '@angular/core';
import {ConferencesTweetsService} from "../conferences-tweets.service";
import {Conference} from "../conference";

@Component({
  selector: 'app-conferences-and-tweets',
  templateUrl: './conferences-and-tweets.component.html',
  styleUrls: ['./conferences-and-tweets.component.css']
})
export class ConferencesAndTweetsComponent implements OnInit {

  searchtext: string = "";
  message: string = "";
  conferences: Conference[] = [];
  tweets: string[] = [];

  constructor(private conferencesTweetsService : ConferencesTweetsService) { }

  getConferencesAndTweets() {

    this.tweets = [];

    if(this.searchtext == "") {
      this.message = "Sie müssen einen Hashtag eingeben.";
      return;
    }

    if(this.searchtext.charAt(0) == '#') this.searchtext = this.searchtext.substr(1, this.searchtext.length-1);

    this.conferencesTweetsService.getConferencesAndTweets(this.searchtext).then(results => {
      if(results.conferences.length == 0) console.log("No results.");
      else {
        console.log("Results: ");
        console.log(results);
      }
      this.tweets = results.tweets;
      this.conferences = results.conferences;

      console.log("length: " + this.tweets.length);

      if(this.conferences.length == 1) this.message = "Eine Konferenz gefunden.";
      if(this.conferences.length > 1) this.message = this.conferences.length + " Konferenzen gefunden.";
      if(this.conferences.length == 0) this.message = "Keine Konferenzen gefunden." ;


    });

  }
  ngOnInit() {
  }

}
