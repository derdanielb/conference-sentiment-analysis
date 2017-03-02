import {Component, OnInit} from '@angular/core';
import {ConferenceService} from "./conference.service";
import {Conference} from "../conference";

@Component({
  selector: 'app-conference',
  templateUrl: './conference.component.html',
  styleUrls: ['./conference.component.css'],
  providers: [ConferenceService]
})
export class ConferenceComponent extends OnInit {

  private conferences : Conference[] = [];

  constructor(private chatService: ConferenceService) {
    super();
  }

  ngOnInit(): void {
    this.chatService.listConferences().then(confs => {
      console.log("here");
      this.conferences = confs;
    });
  }

}
