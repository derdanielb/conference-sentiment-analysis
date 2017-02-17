"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var conference_1 = require("../conference");
var person_1 = require("../person");
var group_1 = require("../group");
var organisation_1 = require("../organisation");
var ConferenceComponent = (function () {
    function ConferenceComponent(conferenceTwitterService) {
        this.conferenceTwitterService = conferenceTwitterService;
        //SearchResults Conferences
        this.resultConferences = [];
        //NgModel searchString
        this.search = "";
        //Last submitted searchString
        this.currentSearch = "";
        //NgModel searchFilter
        this.filter = "";
        this.searched = false;
        this.searchError = false;
        //Controls the TestDataFormular-View
        this.createTestDataVisible = false;
        //NgModel count for the amount of TestData to be created
        this.count = "";
        //Response from creating TestData
        this.returnCount = "";
        this.testDataError = false;
        //Controls the newConferenceFormular-View
        this.newConferenceVisible = false;
        //Empty new Conference
        this.organizerSelection = "";
        this.firstNameOrganizer = "";
        this.lastNameOrganizer = "";
        this.groupOrganisationNameOrganizer = "";
        this.sponsorSelection = "";
        this.firstNameSponsor = "";
        this.lastNameSponsor = "";
        this.groupOrganisationNameSponsor = "";
        this.newConference = new conference_1.Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
        this.saveError = false;
        this.organizerError = false;
        this.sponsorError = false;
        this.conferenceClicked = false;
        //Responses from deleting Conferences
        this.returnDelete = "";
        this.returnDeleteAll = "";
    }
    ConferenceComponent.prototype.ngOnInit = function () {
    };
    ConferenceComponent.prototype.getResults = function () {
        var _this = this;
        this.returnDelete = "";
        this.searchError = false;
        if (this.search == "") {
            this.searchError = true;
            return;
        }
        //If '#' is at the beginning of the searchString, it will be removed
        if (this.search.charAt(0) == "#") {
            var split = this.search.split("#");
            this.search = split[1];
        }
        this.currentSearch = this.search;
        this.resultConferences = [];
        this.conferenceTwitterService.getConferenceResults(this.search, this.filter).then(function (conferences) {
            _this.resultConferences = conferences;
        });
        this.searched = true;
    };
    //Loads the ResultEntry into the newConferenceFormular
    ConferenceComponent.prototype.clickResultEntry = function (conference) {
        this.returnDelete = "";
        this.conferenceClicked = true;
        this.returnDeleteAll = "";
        this.returnConference = null;
        this.returnCount = "";
        this.createTestDataVisible = false;
        this.newConferenceVisible = true;
        this.newConference = conference;
    };
    //Displays the newConferenceFormular
    ConferenceComponent.prototype.newConferenceFormular = function () {
        this.conferenceClicked = false;
        this.returnDeleteAll = "";
        this.createTestDataVisible = false;
        this.returnCount = "";
        this.returnConference = null;
        this.newConferenceVisible = true;
    };
    ConferenceComponent.prototype.addOrganizer = function () {
        this.organizerError = false;
        if (this.organizerSelection == "person") {
            if (this.firstNameOrganizer == "" || this.lastNameOrganizer == "") {
                this.organizerError = true;
                return;
            }
            var person = new person_1.Person(this.firstNameOrganizer, this.lastNameOrganizer);
            this.newConference.organizerList.push(person);
            this.organizerSelection = "";
        }
        else if (this.organizerSelection == "group") {
            if (this.groupOrganisationNameOrganizer == "") {
                this.organizerError = true;
                return;
            }
            var group = new group_1.Group(this.groupOrganisationNameOrganizer);
            this.newConference.organizerList.push(group);
            this.organizerSelection = "";
        }
        else if (this.organizerSelection == "organisation") {
            if (this.groupOrganisationNameOrganizer == "") {
                this.organizerError = true;
                return;
            }
            var organisation = new organisation_1.Organisation(this.groupOrganisationNameOrganizer);
            this.organizerSelection = "";
        }
    };
    ConferenceComponent.prototype.deleteOrganizer = function (index) {
        this.newConference.organizerList.splice(index, 1);
    };
    ConferenceComponent.prototype.addSponsor = function () {
        this.sponsorError = false;
        if (this.sponsorSelection == "person") {
            if (this.firstNameSponsor == "" || this.lastNameSponsor == "") {
                this.sponsorError = true;
                return;
            }
            var person = new person_1.Person(this.firstNameSponsor, this.lastNameSponsor);
            this.newConference.sponsorsList.push(person);
            this.sponsorSelection = "";
        }
        else if (this.sponsorSelection == "group") {
            if (this.groupOrganisationNameSponsor == "") {
                this.sponsorError = true;
                return;
            }
            var group = new group_1.Group(this.groupOrganisationNameSponsor);
            this.newConference.sponsorsList.push(group);
            this.sponsorSelection = "";
        }
        else if (this.sponsorSelection == "organisation") {
            if (this.groupOrganisationNameSponsor == "") {
                this.sponsorError = true;
                return;
            }
            var organisation = new organisation_1.Organisation(this.groupOrganisationNameSponsor);
            this.sponsorSelection = "";
        }
    };
    ConferenceComponent.prototype.deleteSponsor = function (index) {
        this.newConference.sponsorsList.splice(index, 1);
    };
    ConferenceComponent.prototype.saveConference = function () {
        var _this = this;
        this.returnConference = null;
        this.saveError = false;
        if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
            this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
            this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerList == null || this.newConference.sponsorsList == null) {
            this.saveError = true;
            return;
        }
        //If '#' is at the beginning of the twitterHashTag, it will be removed
        if (this.newConference.twitterHashTag.charAt(0) == "#") {
            var split = this.newConference.twitterHashTag.split("#");
            this.newConference.twitterHashTag = split[1];
        }
        this.conferenceTwitterService.saveConference(this.newConference).then(function (returnConference) {
            _this.returnConference = returnConference;
        });
        this.newConferenceVisible = false;
        this.newConference = new conference_1.Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
    };
    ConferenceComponent.prototype.updateConference = function () {
        var _this = this;
        this.returnConference = null;
        this.saveError = false;
        if (this.newConference.conferenceName == "" || this.newConference.from == "" || this.newConference.to == "" || this.newConference.locationName == "" ||
            this.newConference.street == "" || this.newConference.houseNumber == "" || this.newConference.postcode == "" || this.newConference.city == "" ||
            this.newConference.country == "" || this.newConference.twitterHashTag == "" || this.newConference.organizerList == null || this.newConference.sponsorsList == null) {
            this.saveError = true;
            return;
        }
        //If '#' is at the beginning of the twitterHashTag, it will be removed
        if (this.newConference.twitterHashTag.charAt(0) == "#") {
            var split = this.newConference.twitterHashTag.split("#");
            this.newConference.twitterHashTag = split[1];
        }
        this.newConference.organizerList = null;
        this.newConference.sponsorsList = null;
        this.conferenceTwitterService.updateConference(this.newConference).then(function (returnConference) {
            _this.returnConference = returnConference;
        });
        this.newConferenceVisible = false;
        this.newConference = new conference_1.Conference(null, "", "", "", "", "", "", "", "", "", null, "", [], []);
    };
    ConferenceComponent.prototype.deleteConference = function (conference) {
        var _this = this;
        this.returnDelete = "";
        var conferenceToDelete = conference;
        conferenceToDelete.organizerList = null;
        conferenceToDelete.sponsorsList = null;
        this.conferenceTwitterService.deleteConference(conferenceToDelete).then(function (returnDelete) {
            _this.returnDelete = returnDelete;
        });
        this.resultConferences = [];
        this.conferenceTwitterService.getConferenceResults(this.currentSearch, this.filter).then(function (conferences) {
            _this.resultConferences = conferences;
        });
    };
    ConferenceComponent.prototype.deleteAllConferences = function () {
        var _this = this;
        this.returnDeleteAll = "";
        this.returnConference = null;
        this.returnCount = "";
        this.conferenceTwitterService.deleteAllConferences().then(function (returnDeleteAll) {
            _this.returnDeleteAll = returnDeleteAll;
        });
    };
    //Displays the TestDataFormular
    ConferenceComponent.prototype.createTestDataFormular = function () {
        this.returnDeleteAll = "";
        this.newConferenceVisible = false;
        this.returnConference = null;
        this.returnCount = "";
        this.createTestDataVisible = true;
    };
    ConferenceComponent.prototype.createTestData = function () {
        var _this = this;
        this.testDataError = false;
        if (this.count == "") {
            this.testDataError = true;
            return;
        }
        this.conferenceTwitterService.createTestData(this.count).then(function (returnCount) {
            _this.returnCount = returnCount;
        });
        this.count = "";
        this.createTestDataVisible = false;
    };
    ConferenceComponent = __decorate([
        core_1.Component({
            selector: 'app-conference',
            templateUrl: './conference.component.html',
            styleUrls: ['./conference.component.css']
        })
    ], ConferenceComponent);
    return ConferenceComponent;
}());
exports.ConferenceComponent = ConferenceComponent;
