import {Component, OnInit} from '@angular/core';
import {ConferenceTwitterService} from "../conference-twitter.service";
import {Conference} from "../conference";

@Component({
  selector: 'app-conference',
  templateUrl: './conference.component.html',
  styleUrls: ['./conference.component.css']
})
export class ConferenceComponent implements OnInit {

  //SearchResults Conferences
  resultConferences: Conference[] = [];

  //NgModel searchString
  search: string = "";

  //Last submitted searchString
  currentSearch: string = "";

  //NgModel searchFilter
  filter: string = "";

  searched: boolean = false;
  searchError: boolean = false;

  //Controls the TestDataFormular-View
  createTestDataVisible: boolean = false;

  //NgModel count for the amount of TestData to be created
  count: string = "";

  //Response from creating TestData
  returnCount: string = "";
  testDataError: boolean = false;

  //Controls the newConferenceFormular-View
  newConferenceVisible: boolean = false;

  //Empty new Conference
  organizerString: string = "";
  sponsorString: string = "";
  newConference: Conference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], [], null, null);

  //Response from adding or updating a Conference
  returnConference: Conference;
  saveError: boolean = false;
  organizerError: boolean = false;
  sponsorError: boolean = false;

  conferenceClicked: boolean = false;

  //Responses from deleting Conferences
  returnDelete: string = "";
  returnDeleteAll: string = "";

  constructor(private conferenceTwitterService: ConferenceTwitterService) {
  }

  ngOnInit() {
  }

  getResults() {
    this.returnDelete = "";
    this.searchError = false;
    if(this.search == "") {
      this.searchError = true;
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
      this.resultConferences = conferences
    });
    this.searched = true;
  }

  //Loads the ResultEntry into the newConferenceFormular
  clickResultEntry(conference: Conference) {
    this.returnDelete = "";
    this.conferenceClicked = true;
    this.returnDeleteAll = "";
    this.returnConference = null;
    this.returnCount = "";
    this.createTestDataVisible = false;
    this.newConferenceVisible = true;
    this.newConference = conference;
  }

  //Displays the newConferenceFormular
  newConferenceFormular() {
    this.conferenceClicked = false;
    this.returnDeleteAll = "";
    this.createTestDataVisible = false;
    this.returnCount = "";
    this.returnConference = null;
    this.newConferenceVisible = true;
  }

  addOrganizer() {
    this.organizerError = false;
    if(this.organizerString == "") {
      this.organizerError = true;
      return;
    }
    //Organizer Input-Validation
    let split: string[] = this.organizerString.split(";");
    if(!(split[0] == "Person" || split[0] == "Group" || split[0] == "Organisation")) {
      this.organizerError = true;
      return;
    }
    if(split[0] == "Person") {
      if((!split[1] || !split[2]) || split[3]) {
        this.organizerError = true;
        return;
      }
    }
    if(split[0] == "Group" || split[0] == "Organisation") {
      if(!split[1] || split[2]) {
        this.organizerError = true;
        return;
      }
    }
    this.newConference.organizerStringArray.push(this.organizerString);
    this.organizerString = "";
  }

  deleteOrganizer(index: number) {
    this.newConference.organizerStringArray.splice(index, 1);
  }

  addSponsor() {
    this.sponsorError = false;
    if(this.sponsorString == "") {
      this.sponsorError = true;
      return;
    }
    //Sponsor Input-Validation
    let split: string[] = this.sponsorString.split(";");
    if(!(split[0] == "Person" || split[0] == "Group" || split[0] == "Organisation")) {
      this.sponsorError = true;
      return;
    }
    if(split[0] == "Person") {
      if((!split[1] || !split[2]) || split[3]) {
        this.sponsorError = true;
        return;
      }
    }
    if(split[0] == "Group" || split[0] == "Organisation") {
      if(!split[1] || split[2]) {
        this.sponsorError = true;
        return;
      }
    }
    this.newConference.sponsorStringArray.push(this.sponsorString);
    this.sponsorString = "";
  }

  deleteSponsor(index: number) {
    this.newConference.sponsorStringArray.splice(index, 1);
  }

  saveConference() {
    this.returnConference = null;
    this.saveError = false;
    if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
      this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
      this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerStringArray.length == 0 || this.newConference.sponsorStringArray.length == 0) {
      this.saveError = true;
      return;
    }
    //If '#' is at the beginning of the twitterHashTag, it will be removed
    if(this.newConference.twitterHashTag.charAt(0) == "#") {
      let split: string[] = this.newConference.twitterHashTag.split("#");
      this.newConference.twitterHashTag = split[1];
    }
    this.conferenceTwitterService.saveConference(this.newConference).then(returnConference=> {
      this.returnConference = returnConference;
    });
    this.newConferenceVisible = false;
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], [], null, null);
  }

  updateConference() {
    this.returnConference = null;
    this.saveError = false;
    if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
      this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
      this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerStringArray.length == 0 || this.newConference.sponsorStringArray.length == 0) {
      this.saveError = true;
      return;
    }
    //If '#' is at the beginning of the twitterHashTag, it will be removed
    if(this.newConference.twitterHashTag.charAt(0) == "#") {
      let split: string[] = this.newConference.twitterHashTag.split("#");
      this.newConference.twitterHashTag = split[1];
    }
    this.newConference.organizerList = null;
    this.newConference.sponsorsList = null;
    this.conferenceTwitterService.updateConference(this.newConference).then(returnConference=> {
      this.returnConference = returnConference;
    });
    this.newConferenceVisible = false;
    this.newConference = new Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], [], null, null);
  }

  deleteConference(conference: Conference) {
    this.returnDelete = "";
    let conferenceToDelete: Conference = conference;
    conferenceToDelete.organizerList = null;
    conferenceToDelete.sponsorsList = null;
    this.conferenceTwitterService.deleteConference(conferenceToDelete).then(returnDelete=> {
      this.returnDelete = returnDelete;
    });
    this.resultConferences = [];
    this.conferenceTwitterService.getConferenceResults(this.currentSearch, this.filter).then(conferences=> {
      this.resultConferences = conferences;
    });
  }

  deleteAllConferences() {
    this.returnDeleteAll = "";
    this.returnConference = null;
    this.returnCount = "";
    this.conferenceTwitterService.deleteAllConferences().then(returnDeleteAll=> {
      this.returnDeleteAll = returnDeleteAll;
    });
  }

  //Displays the TestDataFormular
  createTestDataFormular() {
    this.returnDeleteAll = "";
    this.newConferenceVisible = false;
    this.returnConference = null;
    this.returnCount = "";
    this.createTestDataVisible = true;
  }

  createTestData() {
    this.testDataError = false;
    if(this.count == "") {
      this.testDataError = true;
      return;
    }
    this.conferenceTwitterService.createTestData(this.count).then(returnCount=>{
      this.returnCount = returnCount;
    });
    this.count = "";
    this.createTestDataVisible = false;
  }

}
