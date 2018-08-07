import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { RegisterComponent } from './components/register/register.component';
import { HomeComponent } from './components/home/home.component';
import { AuthGuard } from './guard/auth.guard';
import { NavbarComponent } from './components/navbar/navbar.component';
import { UserprofileComponent } from './components/userprofile/userprofile.component';
import { PostComponent } from './components/post/post.component';
import { NewpostComponent } from './components/newpost/newpost.component';

const appRoutes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthGuard] }, // For Logged in users ONLY
  { path: 'users/:user_id', component: UserprofileComponent, canActivate: [AuthGuard] }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    FooterComponent,
    HeaderComponent,
    RegisterComponent,
    HomeComponent,
    NavbarComponent,
    UserprofileComponent,
    PostComponent,
    NewpostComponent
  ],
  imports: [
    HttpClientModule,
    BrowserModule,
    FormsModule,
    FontAwesomeModule,
    InfiniteScrollModule,
    RouterModule.forRoot(appRoutes, { useHash: true })
  ],
  providers: [AuthGuard, { provide: LocationStrategy, useClass: HashLocationStrategy }],
  bootstrap: [AppComponent]
})

export class AppModule {
}
