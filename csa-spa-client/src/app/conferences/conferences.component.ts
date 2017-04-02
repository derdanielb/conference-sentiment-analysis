import { Component, OnInit } from '@angular/core';
import {Conference} from "../conference";
import {ConferencesTweetsService} from "../conferences-tweets.service";

@Component({
  selector: 'app-conferences',
  templateUrl: './conferences.component.html',
  styleUrls: ['./conferences.component.css']
})
export class ConferencesComponent implements OnInit {

  searchtext: string = "";
  message: string = "";
  conferences: Conference[] = [];

  readonly searchoptions = [
    {name: "Name", option: "name"},
    {name: "Hashtag", option: "hashtag"}
    ]

  searchfilter: string = this.searchoptions[0].option;

  private results: Promise<Conference[]>;

  constructor(private conferencesTweetsService : ConferencesTweetsService) { }

  getConferences() {

    this.conferences = [];

    if(this.searchtext == "") {

      this.results = this.conferencesTweetsService.getAllConferences();

    } else {

      if (this.searchfilter == 'hashtag') {
        if (this.searchtext.charAt(0) == '#') this.searchtext = this.searchtext.substr(1, this.searchtext.length - 1);
      }

      this.results = this.conferencesTweetsService.getConferences(this.searchtext, this.searchfilter)
    }

    this.results.then(r => {
      if(r.length == 0) console.log("No results.");
      else {
        console.log("Results: ");
        console.log(r);
      }
      this.conferences = r;

      console.log("length: " + this.conferences.length);

      if(this.conferences.length == 1) this.message = "Eine Konferenz gefunden.";
      if(this.conferences.length > 1) this.message = this.conferences.length + " Konferenzen gefunden.";
      if(this.conferences.length == 0) this.message = "Keine Konferenzen gefunden." ;
    });


  }

  ngOnInit() {
  }

}
