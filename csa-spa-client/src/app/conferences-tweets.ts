import {Conference} from "./conference";
export class ConferencesTweets {

  constructor(
    public conferences: Conference[],
    public tweets: string[]
  ) {}

}
