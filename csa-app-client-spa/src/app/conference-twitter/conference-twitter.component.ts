import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-conference-twitter',
  templateUrl: './conference-twitter.component.html',
  styleUrls: ['./conference-twitter.component.css']
})
export class ConferenceTwitterComponent implements OnInit {

  /*
  Controls the AppSelection-View.
  'conference': Conference-App is displayed.
  'twitter': Tiwtter-App is displayed.
  'conferenceTweets': ConferenceTweets-App is displayed.
   */
  select: string;

  constructor() { }

  ngOnInit() {
  }

}
