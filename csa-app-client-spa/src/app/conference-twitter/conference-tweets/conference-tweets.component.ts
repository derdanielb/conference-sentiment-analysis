import { Component, OnInit } from '@angular/core';
import {ConferenceTwitterService} from "../conference-twitter.service";
import {Conference} from "../conference";
import {Tweet} from "../tweet";
import {waitForMap} from "@angular/router/src/utils/collection";

@Component({
  selector: 'app-conference-tweets',
  templateUrl: './conference-tweets.component.html',
  styleUrls: ['./conference-tweets.component.css']
})
export class ConferenceTweetsComponent implements OnInit {

  messageList: string[] = [];

  //NgModel searchString
  hashTag: string = "";

  //SearchResults Conferences
  conferences: Conference[] = [];

  //SearchResults Tweets
  tweets: Tweet[] = [];

  searched: boolean = false;

  constructor(private conferenceTwitterService: ConferenceTwitterService) { }

  ngOnInit() {
  }

  search() {
    this.messageList = [];
    if(this.hashTag.length == 0) {
      this.messageList.push("Bitte Geben Sie ein Suchkriterium an.");
      return;
    }
    this.tweets = [];
    this.conferences = [];
    //If '#' is at the beginning of the searchString, it will be removed
    let searchString: string = this.hashTag;
    if(this.hashTag.charAt(0) == "#") {
      let split: string[] = this.hashTag.split("#");
      searchString = split[1];
    }
    this.conferenceTwitterService.getConferenceTweetResults(searchString).then(conferenceTweets=> {
     this.conferences = conferenceTweets[0]["conferences"];
      this.tweets = conferenceTweets[0]["tweets"];
    });
    this.searched = true;
  }
}
