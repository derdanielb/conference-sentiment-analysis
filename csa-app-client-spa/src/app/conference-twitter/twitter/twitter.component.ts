import {Component, OnInit} from '@angular/core';
import {ConferenceTwitterService} from "../conference-twitter.service";
import {Tweet} from "../tweet";

@Component({
  selector: 'app-twitter',
  templateUrl: './twitter.component.html',
  styleUrls: ['./twitter.component.css']
})
export class TwitterComponent implements OnInit {

  hashTag: string = "";
  tweets: Tweet[] = [];
  messageList: string[] = [];
  searched: boolean = false;

  constructor(private conferenceTwitterService: ConferenceTwitterService) {
  }

  ngOnInit() {
  }

  search() {
    this.messageList = [];
    if(this.hashTag.length == 0) {
      this.messageList.push("Geben Sie ein Suchkriterium ein.");
      return;
    }
    this.tweets = [];
    //If '#' is at the beginning of the searchString, it will be removed
    let searchString: string = this.hashTag;
    if(this.hashTag.charAt(0) == "#") {
      let split: string[] = this.hashTag.split("#");
      searchString = split[1];
    }
    this.conferenceTwitterService.getTwitterResults(searchString).then(tweets=> {
      this.tweets = tweets
    });
    this.searched = true;
  }

}
