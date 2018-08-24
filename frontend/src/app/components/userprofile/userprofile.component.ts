import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';

import { faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { faUserClock } from '@fortawesome/free-solid-svg-icons';
import { faUserCheck } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {

  faUserPlus = faUserPlus;
  faUserClock = faUserClock;
  faUserCheck = faUserCheck;
  requestConnectButton = false;
  connectPendingButton = false;
  connectedButton = false;
  networkButton = false;

  // Retrieve user's data from local storage
  email = localStorage.getItem('email');
  userToken = localStorage.getItem('userToken');
  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');
  userId = localStorage.getItem('userID');

  // Variables for requested user's profile
  profileFirstName: string;
  profileSurname: string;
  profilePhoneNumber: string;
  profileCity: string;
  profileProfession: string;
  profileCompany: string;
  profileEducation: string;
  profileImage: string;
  profileUserID;

  // Posts variables
  totalPosts = 0;
  showedPosts = 0;
  loadMoreButton = false;
  page = 0;
  limitPosts = 5;
  posts = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.profileUserID = +params['user_id'];
      // Posts variables
      this.totalPosts = 0;
      this.showedPosts = 0;
      this.loadMoreButton = false;
      this.page = 0;
      this.limitPosts = 5;
      this.posts = [];
      window.scrollTo(0, 0);
      this.loadProfile();
    });
  }

  loadProfile() {
    if (this.userId === this.profileUserID.toString()) {
      this.requestConnectButton = false;
      this.connectPendingButton = false;
      this.connectedButton = false;
      this.networkButton = false;
    } else {
      this.checkConnectStatus();
    }
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const requestProfile = { profileUserID: this.profileUserID };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/user', {
      userIdentifiers,
      requestProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profileImage = 'data:image/jpeg;base64,' + obj.profileImage;
      const userObj = JSON.parse(obj.user);
      this.profileFirstName = userObj.firstName;
      this.profileSurname = userObj.lastName;
      this.getUserInfo();
      this.getPosts();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getUserInfo() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const userInfoRequest = { userIdInfo: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getuserinfo', {
      userIdentifiers,
      userInfoRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profilePhoneNumber = obj.phoneNumber;
      // this.isPhonePublic = !!obj.isPhonePublic;
      this.profileCity = obj.city;
      // this.isCityPublic = !!obj.isCityPublic;
      this.profileProfession = obj.profession;
      // this.isProfessionPublic = !!obj.isProfessionPublic;
      this.profileCompany = obj.company;
      // this.isCompanyPublic = !!obj.isCompanyPublic;
      this.profileEducation = obj.education;
      // this.isEducationPublic = !!obj.isEducationPublic;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getPosts() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const pageRequest = { profileUserID: this.profileUserID, pageNumber: this.page, limit: this.limitPosts };
    this.page++;
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/getprofileposts', {
      userIdentifiers,
      pageRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      console.log(data);
      const obj = JSON.parse(data);
      this.totalPosts = obj.totalElements;
      if (this.totalPosts > 0) {
        const numberOfPosts = obj.numberOfElements;
        this.showedPosts = this.showedPosts + numberOfPosts;
        // console.log('total posts' + this.totalPosts);
        // console.log('showed posts' + this.showedPosts);
        if (this.totalPosts > this.showedPosts) {
          this.loadMoreButton = true;
        } else {
          this.loadMoreButton = false;
        }
        // console.log(this.loadMoreButton);
        for (let i = 0; i < numberOfPosts; i++) {
          this.posts.push(obj.content[i]);
        }
      } else {
        this.posts = null;
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  checkConnectStatus() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const targetProfile = { profileUserID: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/connectstatus', {
      userIdentifiers,
      targetProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      // console.log(data);
      if (obj.friends === '0') {
        this.requestConnectButton = true;
        this.connectPendingButton = false;
        this.connectedButton = false;
        this.networkButton = false;
      } else {
        // Show network if only you are connected to this person
        if (obj.pending === '1') {
          this.networkButton = false;
          this.requestConnectButton = false;
          this.connectPendingButton = true;
          this.connectedButton = false;
        } else {
          // They are connected
          this.networkButton = true;
          this.requestConnectButton = false;
          this.connectPendingButton = false;
          this.connectedButton = true;
        }
      }
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  sendConnectRequest() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    const friendRequest = { userRequestID: this.profileUserID.toString() };
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/sendconnect', {
      userIdentifiers,
      friendRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      this.checkConnectStatus();
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

}
