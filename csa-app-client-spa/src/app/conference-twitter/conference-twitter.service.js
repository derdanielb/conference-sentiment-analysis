"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('@angular/core');
var http_1 = require("@angular/http");
require("rxjs/add/operator/toPromise");
var ConferenceTwitterService = (function () {
    function ConferenceTwitterService(http) {
        this.http = http;
        this.conferenceBaseURL = "http://localhost:8080/conference/";
        this.twitterBaseURL = "http://localhost:8070/twitter/search/";
        this.conferenceTweetsBaseURL = "http://localhost:8090/combinedSearch/";
    }
    ConferenceTwitterService.prototype.getConferenceResults = function (search, filter) {
        var headers = new http_1.Headers({ 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.get(this.conferenceBaseURL + filter + "/" + search, options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.saveConference = function (conference) {
        var headers = new http_1.Headers({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.post(this.conferenceBaseURL + "add", JSON.stringify(conference), options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.updateConference = function (conference) {
        var headers = new http_1.Headers({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.post(this.conferenceBaseURL + "update", JSON.stringify(conference), options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.deleteConference = function (conference) {
        var headers = new http_1.Headers({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.post(this.conferenceBaseURL + "delete", JSON.stringify(conference), options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.deleteAllConferences = function () {
        var headers = new http_1.Headers({ 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.post(this.conferenceBaseURL + "deleteAll", options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.createTestData = function (count) {
        var headers = new http_1.Headers({ 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.post(this.conferenceBaseURL + "createTestdata/" + count, options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.getTwitterResults = function (search) {
        var headers = new http_1.Headers({ 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.get(this.twitterBaseURL + search, options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.getConferenceTweetResults = function (search) {
        var headers = new http_1.Headers({ 'Accept': 'application/json' });
        var options = new http_1.RequestOptions({ headers: headers });
        return this.http.get(this.conferenceTweetsBaseURL + search, options)
            .toPromise()
            .then(this.extractData)
            .catch(this.handleError);
    };
    ConferenceTwitterService.prototype.extractData = function (res) {
        var body = res.json();
        return body;
    };
    ConferenceTwitterService.prototype.handleError = function (error) {
        // In a real world app, we might use a remote logging infrastructure
        var errMsg;
        if (error instanceof http_1.Response) {
            var body = error.json() || '';
            var err = body.error || JSON.stringify(body);
            errMsg = error.status + " - " + (error.statusText || '') + " " + err;
        }
        else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error(errMsg);
        return Promise.reject(errMsg);
    };
    ConferenceTwitterService = __decorate([
        core_1.Injectable()
    ], ConferenceTwitterService);
    return ConferenceTwitterService;
}());
exports.ConferenceTwitterService = ConferenceTwitterService;
