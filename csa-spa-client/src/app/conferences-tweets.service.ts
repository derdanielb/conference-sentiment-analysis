
import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions } from '@angular/http';
import {Conference} from "./conference";
import 'rxjs/add/operator/toPromise';
import {ConferencesTweets} from "./conferences-tweets";

@Injectable()
export class ConferencesTweetsService {

  constructor(private http: Http) { }

  conferencesRestUrl : string = 'http://localhost:8080/conference/';
  tweetsRestUrl : string = 'http://localhost:8090/twitter/search/';
  conferencesTweetsRestUrl : string = 'http://localhost:8099/conferencetweets/hashtag/';

  getConferences(search: string, filter: string) : Promise<Conference[]> {

    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.get(this.conferencesRestUrl + filter + "/" + search, options)
      .toPromise()
      .then(this.extractArrayData)
      .catch(this.handleError);

  }

  getTweets(hashtag: string) : Promise<string[]> {

    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.get(this.tweetsRestUrl + hashtag, options)
      .toPromise()
      .then(this.extractArrayData)
      .catch(this.handleError);

  }

  getConferencesAndTweets(hashtag: string) : Promise<ConferencesTweets> {

    let headers = new Headers({ 'Accept': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.get(this.conferencesTweetsRestUrl + hashtag, options)
      .toPromise()
      .then(this.extractObjectData)
      .catch(this.handleError);

  }

  private extractArrayData(res: Response) {
    console.log("Response: ");
    console.log(res);
    let body = res.json();
    console.log("body: ");
    console.log(body);
    return body || [];
  }

  private extractObjectData(res: Response) {
    console.log("Response: ");
    console.log(res);
    let body = res.json();
    console.log("body: ");
    console.log(body);
    return body || {};
  }

  private handleError (error: Response | any) {
    alert('ERROR');
    return Promise.reject("ERROR");
  }

}
