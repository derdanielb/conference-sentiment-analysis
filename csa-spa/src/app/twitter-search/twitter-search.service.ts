import { Injectable } from '@angular/core';
import {Http, Response, RequestOptions, Headers} from "@angular/http";

@Injectable()
export class TwitterSearchService {

  constructor(private http : Http) { }

  public searchTweets(hashtag : string) : Promise<string[]> {
    let headers = new Headers({
      "Content-Type":"application/json"
    });
    let options = new RequestOptions({headers: headers});
    return this.http.get("/csa-twitter-search/v1/search/" + hashtag, options)
      .toPromise()
      .then(response => {
        try {
          return response.json();
        } catch (any) {
          return [];
        }
      })
      .catch(this.handleError);
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
