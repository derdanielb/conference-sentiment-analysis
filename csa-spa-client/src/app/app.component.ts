import { Component } from '@angular/core';
import { Conference } from "./conference";
import {ConferencesTweetsService} from "./conferences-tweets.service";

@Component({
  selector: 'app-root',
  providers: [ConferencesTweetsService],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Conference Sentiment Analysis';

}
