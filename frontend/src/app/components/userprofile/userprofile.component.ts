import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.css']
})
export class UserprofileComponent implements OnInit {

  test = [1, 2, 3, 4, 5];

  href = '';

  // Retrieve user's data from local storage
  email = localStorage.getItem('email');
  userToken = localStorage.getItem('userToken');
  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');
  userID = localStorage.getItem('userID');

  // Variables for requested user's profile
  profileFirstName: string;
  profileSurname: string;
  profilePhoneNumber: string;
  profileImage: string;
  profileUserID;

  // Posts variables
  page = 0;
  limitPosts = 5;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {
    console.log("requested profile!")
  }

  ngOnInit() {

    this.route.params.subscribe((params) => {
      this.profileUserID = +params['user_id'];
      this.loadProfile();
    });

  }

  loadProfile() {

    this.href = this.router.url;
    const url = this.href.split('/');
    const urlUserID = url[2];
    this.profileUserID = urlUserID;
    console.log(urlUserID);

    const userAttrs = { userToken: this.userToken, email: this.email };
    const requestProfile = { userID: this.userID, profileUserID: this.profileUserID };

    const req = this.http.post('http://localhost:8080/api/user', {
      userAttrs,
      requestProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profileImage = 'data:image/jpeg;base64,' + obj.profileImage;
      const userObj = JSON.parse(obj.user);
      console.log(userObj);
      this.profileFirstName = userObj.firstName;
      this.profileSurname = userObj.lastName;
      console.log(obj);
      // Todo call getPosts() to retrieve the 5 most recent posts
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  getPosts() {
    const pageRequest = { profileUserID: this.profileUserID, pageNumber: this.page, limit: this.limitPosts };
    this.page++;
    const req = this.http.post('http://localhost:8080/api/getprofileposts', {
      pageRequest
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      console.log(data);
      // Todo call getPosts() to retrieve the 5 most recent posts
    },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

  loadMorePosts() {
    console.log('add!');
    // const newVal = this.test.length + 1;
    this.test.push(this.test.length + 1, this.test.length + 2, this.test.length + 3, this.test.length + 4, this.test.length + 5);
    this.getPosts();
    // Todo call getPosts() to retrieve next 5 posts
  }

  // onScroll() {
  //   console.log("Scroll...");
  // }

}
