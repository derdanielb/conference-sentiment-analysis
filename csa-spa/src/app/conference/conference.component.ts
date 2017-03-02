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
  private currentConference : Conference = new Conference();

  constructor(private chatService: ConferenceService) {
    super();
  }

  ngOnInit(): void {
    this.chatService.listConferences().then(confs => {
      this.conferences = confs;
    });
  }

  public setCurrent(conf : Conference) : void {
    this.currentConference = new Conference(conf);
    window.scrollTo(0, 0);
  }

  public reset() : void {
    this.currentConference = new Conference();
  }

  public update() : void {
    this.chatService.updateConference(this.currentConference).then(conf => {
      let index = this.conferences.findIndex(c => c.uuid == conf.uuid);
      this.conferences[index] = conf;
      this.reset();
    });
  }

  public delete() : void {
    this.chatService.deleteConference(this.currentConference).then(() => {
      this.conferences = this.conferences.filter(c => c.uuid != this.currentConference.uuid);
      this.reset();
    });
  }
}
