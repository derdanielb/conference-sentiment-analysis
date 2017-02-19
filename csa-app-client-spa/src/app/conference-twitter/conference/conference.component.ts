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

  //Nav
  menu: string = "newConference";

  //Content
  messageListContent: string[] = [];

  //searchConference
  resultConferences: Conference[] = [];
  searched: boolean = false;
  //NgModel searchFilter
  filter: string = "getByConferenceName";
  //NgModel searchString
  search: string = "";
  //Last submitted searchString
  currentSearch: string = "";

  //createTestData
  //NgModel count for the amount of TestData to be created
  count: string = "";

  //newConference
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

  //Button-methods to change the displayed Content

  searchConferenceForm() {
    this.messageListContent = [];
    this.menu = "searchConference"
  }

  createConferenceForm() {
    this.messageListContent = [];
    this.menu = "newConference";
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
  }

  createTestDataForm() {
    this.messageListContent = [];
    this.menu = "createTestData";
  }

  deleteAllConferencesForm() {
    this.messageListContent = [];
    this.menu = "deleteAll"
  }

  //Loads the ResultEntry into the newConferenceFormular
  clickResultEntry(conference: Conference) {
    this.messageListContent = [];
    this.menu = "newConference";
    this.newConference = conference;
  }

  //searchConference-methods
  getResults() {
    this.messageListContent = [];
    if(this.search == "") {
      this.messageListContent.push("Bitte geben Sie ein Suchkriterium ein.");
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
    this.searched = true;
  }

  deleteConference(conference: Conference) {
    this.messageListContent  = [];
    let conferenceToDelete: Conference = conference;
    this.conferenceTwitterService.deleteConference(conferenceToDelete).then(response=> {
      this.messageListContent.push(response);
    });
    this.resultConferences = [];
    this.conferenceTwitterService.getConferenceResults(this.currentSearch, this.filter).then(conferences=> {
      this.resultConferences = conferences;
    });
    if(this.resultConferences.length == 0) {
      this.messageListContent.push("Keine Suchergebnisse.")
    }
  }

  //newConference-methods
  addOrganizer() {
    this.messageListContent = [];
    if(this.organizerSelection == "person") {
      if(this.firstNameOrganizer == "" || this.lastNameOrganizer == "") {
        this.messageListContent.push("Geben Sie Vor- und Nachname an.");
        return;
      }
      let person = new Person(this.firstNameOrganizer, this.lastNameOrganizer);
      this.newConference.organizerList.push(person);
      this.organizerSelection = "";

    } else if(this.organizerSelection == "group") {
      if(this.groupOrganisationNameOrganizer == "") {
        this.messageListContent.push("Geben Sie einen Gruppennamen an.");
        return;
      }
      let group = new Group(this.groupOrganisationNameOrganizer);
      this.newConference.organizerList.push(group);
      this.organizerSelection = "";

    } else if(this.organizerSelection == "organisation") {
      if(this.groupOrganisationNameOrganizer == "") {
        this.messageListContent.push("Geben Sie einen Organisationsnamen an.");
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
    this.messageListContent = [];
    if(this.sponsorSelection == "person") {
      if(this.firstNameSponsor == "" || this.lastNameSponsor == "") {
        this.messageListContent.push("Geben Sie Vor- und Nachname an.");
        return;
      }
      let person = new Person(this.firstNameSponsor, this.lastNameSponsor);
      this.newConference.sponsorsList.push(person);
      this.sponsorSelection = "";

    } else if(this.sponsorSelection == "group") {
      if(this.groupOrganisationNameSponsor == "") {
        this.messageListContent.push("Geben Sie einen Gruppennamen an.");
        return;
      }
      let group = new Group(this.groupOrganisationNameSponsor);
      this.newConference.sponsorsList.push(group);
      this.sponsorSelection = "";

    } else if(this.sponsorSelection == "organisation") {
      if(this.groupOrganisationNameSponsor == "") {
        this.messageListContent.push("Geben Sie einen Organisationsnamen an.");
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
    this.messageListContent = [];
    if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
      this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
      this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerList == null || this.newConference.sponsorsList == null) {
      this.messageListContent.push("Bitte füllen Sie alle Felder aus.");
      return;
    }
    //If '#' is at the beginning of the twitterHashTag, it will be removed
    if(this.newConference.twitterHashTag.charAt(0) == "#") {
      let split: string[] = this.newConference.twitterHashTag.split("#");
      this.newConference.twitterHashTag = split[1];
    }
    this.conferenceTwitterService.saveConference(this.newConference).then(response=> {
      this.messageListContent.push(response);
    });
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
  }

  updateConference() {
    this.messageListContent = [];
    if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
      this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
      this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerList == null || this.newConference.sponsorsList == null) {
      this.messageListContent.push("Bitte füllen Sie alle Felder aus.");
      return;
    }
    //If '#' is at the beginning of the twitterHashTag, it will be removed
    if(this.newConference.twitterHashTag.charAt(0) == "#") {
      let split: string[] = this.newConference.twitterHashTag.split("#");
      this.newConference.twitterHashTag = split[1];
    }
    this.conferenceTwitterService.updateConference(this.newConference).then(response=> {
      this.messageListContent.push(response);
    });
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
  }

  //deleteAll-methods
  deleteAll() {
    this.conferenceTwitterService.deleteAllConferences().then(response=> {
      this.messageListContent.push(response);
    });
    this.menu = "newConference";
  }

  cancelDeleteAll() {
    this.menu = "newConference";
  }

  //createTestData-method
  createTestData() {
    this.messageListContent = [];
    if(this.count == "") {
      this.messageListContent.push("Bitte geben Sie eine Zahl ein.");
      return;
    }
    this.conferenceTwitterService.createTestData(this.count).then(response=>{
      this.messageListContent.push(response);
  });
    this.count = "";
  }

}
