import { Injectable } from '@angular/core';
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import {Conference} from "../conference";
import "rxjs/add/operator/toPromise"

@Injectable()
export class ConferenceService {

  constructor(private http : Http) {
  }

  public listConferences() : Promise<Conference[]> {
    let headers = new Headers({
      "Content-Type":"application/json"
    });
    let options = new RequestOptions({headers: headers});
    return this.http.get("/csa-conference/v1/conferences", options)
      .toPromise()
      .then(response => {
        return this.extractGetReply(response);
      })
      .catch(this.handleError);
  }

  private extractGetReply(res: Response): Conference[] {
    let body = res.json();
    try {
      let rArray = [];
      body.forEach(value => {
        rArray.push(new Conference(value));
      });
      return rArray;
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
