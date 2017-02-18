import { Injectable } from '@angular/core';
import {Conference} from "./conference";
import {Http, RequestOptions, Headers, Response} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {Tweet} from "./tweet";

@Injectable()
export class ConferenceTwitterService {

  conferenceBaseURL: string = "http://localhost:8080/conference/";
  twitterBaseURL: string = "http://localhost:8070/twitter/search/";
  conferenceTweetsBaseURL: string = "http://localhost:8090/combinedSearch/";

  constructor(private http: Http) { }

  getConferenceResults(search:string, filter:string): Promise<Conference[]> {
    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.get(this.conferenceBaseURL + filter + "/" + search, options)
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  saveConference(conference: Conference): Promise<string> {
    let headers = new Headers({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.conferenceBaseURL + "add", JSON.stringify(conference), options)
      .toPromise()
      .then(this.extractMessage)
      .catch(this.handleError);
  }

  updateConference(conference: Conference): Promise<string> {
    let headers = new Headers({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.conferenceBaseURL + "update", JSON.stringify(conference), options)
      .toPromise()
      .then(this.extractMessage)
      .catch(this.handleError);
  }

  deleteConference(conference: Conference): Promise<string> {
    let headers = new Headers({ 'Content-Type': 'application/json', 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.conferenceBaseURL + "delete", JSON.stringify(conference), options)
      .toPromise()
      .then(this.extractMessage)
      .catch(this.handleError);
  }

  deleteAllConferences(): Promise<string> {
    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.conferenceBaseURL + "deleteAll", options)
      .toPromise()
      .then(this.extractMessage)
      .catch(this.handleError);
  }

  createTestData(count: string): Promise<string> {
    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.post(this.conferenceBaseURL + "createTestdata/" + count, options)
      .toPromise()
      .then(this.extractMessage)
      .catch(this.handleError);
  }

  getTwitterResults(search:string): Promise<Tweet[]> {
    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.get(this.twitterBaseURL + search, options)
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  getConferenceTweetResults(search:string): Promise<string[]> {
    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });
    return this.http.get(this.conferenceTweetsBaseURL + search, options)
      .toPromise()
      .then(this.extractData)
      .catch(this.handleError);
  }

  private extractData(res: Response) {
    let body = res.json();
    return body;
  }

  private extractMessage(res: Response) {
    let responseMessage = res.text();
    return responseMessage;
  }

  private handleError (error: Response | any) {
    // In a real world app, we might use a remote logging infrastructure
    let errMsg: string;
    if (error instanceof Response) {
      const body = error.json() || '';
      const err = body.error || JSON.stringify(body);
      errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Promise.reject(errMsg);
  }

}
