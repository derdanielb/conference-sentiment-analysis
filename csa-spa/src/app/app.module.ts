import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import {TabsModule} from 'ng2-bootstrap';

import { AppComponent } from './app.component';
import { ConferenceComponent } from './conference/conference.component';
import { TwitterSearchComponent } from './twitter-search/twitter-search.component';
import { ConferenceTweetsComponent } from './conference-tweets/conference-tweets.component';

@NgModule({
  declarations: [
    AppComponent,
    ConferenceComponent,
    TwitterSearchComponent,
    ConferenceTweetsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    TabsModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
