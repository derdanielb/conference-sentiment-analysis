import { Component, OnInit } from '@angular/core';
import {ConferencesTweetsService} from "../conferences-tweets.service";

@Component({
  selector: 'app-tweets',
  templateUrl: './tweets.component.html',
  styleUrls: ['./tweets.component.css']
})
export class TweetsComponent implements OnInit {


  searchtext: string = "";
  message: string = "";
  tweets: string[] = [];

  constructor(private conferencesTweetsService : ConferencesTweetsService) { }

  getTweets() {

    this.tweets = [];

    if(this.searchtext == "") {
      this.message = "Sie müssen einen Hashtag eingeben.";
      return;
    }

    if(this.searchtext.charAt(0) == '#') this.searchtext = this.searchtext.substr(1, this.searchtext.length-1);

    this.conferencesTweetsService.getTweets(this.searchtext).then(results => {
      if(results.length == 0) console.log("No results.");
      else {
        console.log("Results: ");
        console.log(results);
      }
      this.tweets = results;

      console.log("length: " + this.tweets.length);

      if(this.tweets.length == 1) this.message = "Einen Tweet gefunden.";
      if(this.tweets.length > 1) this.message = this.tweets.length + " Tweets gefunden.";
      if(this.tweets.length == 0) this.message = "Keine Tweets gefunden." ;


    });

  }

  ngOnInit() {
  }

}
