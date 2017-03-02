import { Component, OnInit } from '@angular/core';
import {ConferenceTweets} from "./conference-tweets";
import {ConferenceTweetsService} from "./conference-tweets.service";

@Component({
  selector: 'app-conference-tweets',
  templateUrl: './conference-tweets.component.html',
  styleUrls: ['./conference-tweets.component.css'],
  providers: [ConferenceTweetsService]
})
export class ConferenceTweetsComponent implements OnInit {

  private searchQuery : string = "";
  private conferenceTweets : ConferenceTweets[] = [];

  constructor(private conferenceTweetsService: ConferenceTweetsService) { }

  ngOnInit() {
  }

  public search(){
    let rep = this.conferenceTweetsService.getTweets(this.searchQuery);
    rep.then(cts => {
      console.log(cts);
      this.conferenceTweets = cts;
    });
  }
}
