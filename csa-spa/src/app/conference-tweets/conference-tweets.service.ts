import { Injectable } from '@angular/core';
import {ConferenceTweets} from "./conference-tweets";
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import "rxjs/add/operator/toPromise"

@Injectable()
export class ConferenceTweetsService {

  constructor(private http: Http) { }

  public getTweets(name : string): Promise<ConferenceTweets[]> {
    let headers = new Headers({
      "Content-Type":"application/json"
    });
    let options = new RequestOptions({headers: headers});
    return this.http.get("/csa-conference-tweets/v1/conferences?name=" + name, options)
      .toPromise()
      .then(response => {
        return this.extractGetReply(response);
      })
      .catch(this.handleError);
  }

  private extractGetReply(res: Response): ConferenceTweets[] {
    let body = res.json();
    try {
      let ctArray = [];
      body.forEach(value => {
        ctArray.push(new ConferenceTweets(value));
      });
      return ctArray;
    } catch (any) {
      return [];
    }
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
