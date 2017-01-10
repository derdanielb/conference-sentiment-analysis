import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ConferenceTwitterComponent} from './conference-twitter.component';
import {ConferenceComponent} from './conference/conference.component';
import {TwitterComponent} from './twitter/twitter.component';
import {ConferenceTweetsComponent} from './conference-tweets/conference-tweets.component';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {ConferenceTwitterService} from "./conference-twitter.service";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    HttpModule
  ],
  exports: [
    ConferenceTwitterComponent,
  ],
  providers: [
    ConferenceTwitterService
  ],
  declarations: [ConferenceTwitterComponent, ConferenceComponent, TwitterComponent, ConferenceTweetsComponent]
})
export class ConferenceTwitterModule {
}
