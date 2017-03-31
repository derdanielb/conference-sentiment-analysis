import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { ConferencesComponent } from './conferences/conferences.component';
import { TweetsComponent } from './tweets/tweets.component';
import { ConferencesAndTweetsComponent } from './conferences-and-tweets/conferences-and-tweets.component';
import {RouterModule, Routes} from "@angular/router";

const appRoutes: Routes = [
  { path: 'conferences', component: ConferencesComponent },
  { path: 'tweets',      component: TweetsComponent },
  { path: 'conferences-tweets', component: ConferencesAndTweetsComponent },
  { path: '', redirectTo: '/conferences', pathMatch: 'full' },
  { path: '**', redirectTo: '/conferences' }
]

@NgModule({
  declarations: [
    AppComponent,
    ConferencesComponent,
    TweetsComponent,
    ConferencesAndTweetsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
