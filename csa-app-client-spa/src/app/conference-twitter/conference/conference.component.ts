import {Component, OnInit} from '@angular/core';
import {ConferenceTwitterService} from "../conference-twitter.service";
import {Conference} from "../conference";
import {Person} from "../person";
import {Group} from "../group";
import {Organisation} from "../organisation";

@Component({
  selector: 'app-conference',
  templateUrl: './conference.component.html',
  styleUrls: ['./conference.component.css']
})
export class ConferenceComponent implements OnInit {

  messageListFormular: string[] = [];
  messageListSearch: string[] = [];

  //SearchResults Conferences
  resultConferences: Conference[] = [];

  //NgModel searchString
  search: string = "";

  //Last submitted searchString
  currentSearch: string = "";

  //NgModel searchFilter
  filter: string = "";

  //Controls the TestDataFormular-View
  createTestDataVisible: boolean = false;

  //NgModel count for the amount of TestData to be created
  count: string = "";

  //Controls the newConferenceFormular-View
  newConferenceVisible: boolean = false;

  //Empty new Conference
  organizerSelection: string = "";
  firstNameOrganizer: string = "";
  lastNameOrganizer: string = "";
  groupOrganisationNameOrganizer: string = "";
  sponsorSelection: string = "";
  firstNameSponsor: string = "";
  lastNameSponsor: string = "";
  groupOrganisationNameSponsor: string = "";
  newConference: Conference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);

  conferenceClicked: boolean = false;

  constructor(private conferenceTwitterService: ConferenceTwitterService) {
  }

  ngOnInit() {
  }

  getResults() {
    if(this.search == "") {
      this.messageListSearch.push("Bitte geben Sie ein Suchkriterium ein.");
      return;
    }
    //If '#' is at the beginning of the searchString, it will be removed
    if(this.search.charAt(0) == "#") {
      let split: string[] = this.search.split("#");
      this.search = split[1];
    }
    this.currentSearch = this.search;
    this.resultConferences = [];
    this.conferenceTwitterService.getConferenceResults(this.search, this.filter).then(conferences=> {
      this.resultConferences = conferences;
    });
    if(this.resultConferences.length == 0) {
      this.messageListSearch.push("Keine Suchergebnisse.");
    }
  }

  //Loads the ResultEntry into the newConferenceFormular
  clickResultEntry(conference: Conference) {
    this.conferenceClicked = true;
    this.createTestDataVisible = false;
    this.newConferenceVisible = true;
    this.newConference = conference;
  }

  //Displays the newConferenceFormular
  newConferenceFormular() {
    this.messageListFormular = [];
    this.conferenceClicked = false;
    this.createTestDataVisible = false;
    this.newConferenceVisible = true;
  }

  addOrganizer() {
    this.messageListFormular = [];
    if(this.organizerSelection == "person") {
      if(this.firstNameOrganizer == "" || this.lastNameOrganizer == "") {
        this.messageListFormular.push("Geben Sie Vor- und Nachname an.");
        return;
      }
      let person = new Person(this.firstNameOrganizer, this.lastNameOrganizer);
      this.newConference.organizerList.push(person);
      this.organizerSelection = "";

    } else if(this.organizerSelection == "group") {
      if(this.groupOrganisationNameOrganizer == "") {
        this.messageListFormular.push("Geben Sie einen Gruppennamen an.");
        return;
      }
      let group = new Group(this.groupOrganisationNameOrganizer);
      this.newConference.organizerList.push(group);
      this.organizerSelection = "";

    } else if(this.organizerSelection == "organisation") {
      if(this.groupOrganisationNameOrganizer == "") {
        this.messageListFormular.push("Geben Sie einen Organisationsnamen an.");
        return;
      }
      let organisation = new Organisation(this.groupOrganisationNameOrganizer);
      this.newConference.organizerList.push(organisation);
      this.organizerSelection = "";

    }
  }

  deleteOrganizer(index: number) {
    this.newConference.organizerList.splice(index, 1);
  }

  addSponsor() {
    this.messageListFormular = [];
    if(this.sponsorSelection == "person") {
      if(this.firstNameSponsor == "" || this.lastNameSponsor == "") {
        this.messageListFormular.push("Geben Sie Vor- und Nachname an.");
        return;
      }
      let person = new Person(this.firstNameSponsor, this.lastNameSponsor);
      this.newConference.sponsorsList.push(person);
      this.sponsorSelection = "";

    } else if(this.sponsorSelection == "group") {
      if(this.groupOrganisationNameSponsor == "") {
        this.messageListFormular.push("Geben Sie einen Gruppennamen an.");
        return;
      }
      let group = new Group(this.groupOrganisationNameSponsor);
      this.newConference.sponsorsList.push(group);
      this.sponsorSelection = "";

    } else if(this.sponsorSelection == "organisation") {
      if(this.groupOrganisationNameSponsor == "") {
        this.messageListFormular.push("Geben Sie einen Organisationsnamen an.");
        return;
      }
      let organisation = new Organisation(this.groupOrganisationNameSponsor);
      this.newConference.sponsorsList.push(organisation);
      this.sponsorSelection = "";

    }
  }

  deleteSponsor(index: number) {
    this.newConference.sponsorsList.splice(index, 1);
  }

  saveConference() {
    this.messageListFormular = [];
    if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
      this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
      this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerList == null || this.newConference.sponsorsList == null) {
      this.messageListFormular.push("Bitte füllen Sie alle Felder aus.");
      return;
    }
    //If '#' is at the beginning of the twitterHashTag, it will be removed
    if(this.newConference.twitterHashTag.charAt(0) == "#") {
      let split: string[] = this.newConference.twitterHashTag.split("#");
      this.newConference.twitterHashTag = split[1];
    }
    this.conferenceTwitterService.saveConference(this.newConference).then(response=> {
      this.messageListFormular.push(response);
    });
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
  }

  updateConference() {
    this.messageListFormular = [];
    if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
      this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
      this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerList == null || this.newConference.sponsorsList == null) {
      this.messageListFormular.push("Bitte füllen Sie alle Felder aus.");
      return;
    }
    //If '#' is at the beginning of the twitterHashTag, it will be removed
    if(this.newConference.twitterHashTag.charAt(0) == "#") {
      let split: string[] = this.newConference.twitterHashTag.split("#");
      this.newConference.twitterHashTag = split[1];
    }
    this.conferenceTwitterService.updateConference(this.newConference).then(response=> {
      this.messageListFormular.push(response);
    });
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
  }

  deleteConference(conference: Conference) {
    this.messageListSearch  = [];
    let conferenceToDelete: Conference = conference;
    this.conferenceTwitterService.deleteConference(conferenceToDelete).then(response=> {
      this.messageListSearch.push(response);
    });
    this.resultConferences = [];
    this.conferenceTwitterService.getConferenceResults(this.currentSearch, this.filter).then(conferences=> {
      this.resultConferences = conferences;
    });
    if(this.resultConferences.length == 0) {
      this.messageListSearch.push("Keine Suchergebnisse.")
    }
  }

  deleteAllConferences() {
    this.messageListFormular = [];
    this.conferenceTwitterService.deleteAllConferences().then(response=> {
      this.messageListFormular.push(response);
    });
  }

  //Displays the TestDataFormular
  createTestDataFormular() {
    this.messageListFormular = [];
    this.newConferenceVisible = false;
    this.createTestDataVisible = true;
  }

  createTestData() {
    this.messageListFormular = [];
    if(this.count == "") {
      this.messageListFormular.push("Bitte geben Sie eine Zahl ein.");
      return;
    }
    this.conferenceTwitterService.createTestData(this.count).then(response=>{
      this.messageListFormular.push(response);
  });
    this.count = "";
  }

}
