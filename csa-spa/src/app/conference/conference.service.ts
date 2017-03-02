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
        return this.extractList(response);
      })
      .catch(this.handleError);
  }

  public searchConferences(query : string) : Promise<Conference[]> {
    let headers = new Headers({
      "Content-Type":"application/json"
    });
    let options = new RequestOptions({headers: headers});
    return this.http.get("/csa-conference/v1/conferences?name=" + query, options)
      .toPromise()
      .then(response => {
        return this.extractList(response);
      })
      .catch(this.handleError);
  }

  public updateConference(conf : Conference) : Promise<Conference> {
    let headers = new Headers({
      "Content-Type":"application/json"
    });
    let options = new RequestOptions({headers: headers});
    return this.http.put("/csa-conference/v1/conferences/" + conf.uuid, conf, options)
      .toPromise()
      .then(response => {
        return this.extractSingle(response);
      })
      .catch(this.handleError);
  }

  public deleteConference(conf : Conference) : Promise<void> {
    let headers = new Headers({
      "Content-Type":"application/json"
    });
    let options = new RequestOptions({headers: headers});
    return this.http.delete("/csa-conference/v1/conferences/" + conf.uuid, options)
      .toPromise()
      .then(() => {
        return;
      })
      .catch(this.handleError);
  }

  private extractSingle(res: Response): Conference {
    let body = res.json();
    try {
      return new Conference(body);
    } catch (any) {
      return null;
    }
  }

  private extractList(res: Response): Conference[] {
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
