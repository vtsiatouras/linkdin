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
      this.loadProfile();
    });
  }

  loadProfile() {
    this.href = this.router.url;
    const url = this.href.split('/');
    const urlUserID = url[2];
    this.profileUserID = urlUserID;
    const userAttrs = { userToken: this.userToken, email: this.email };
    const requestProfile = { userID: this.userID, profileUserID: this.profileUserID };
    const req = this.http.post('http://localhost:8080/api/user', {
      userAttrs,
      requestProfile
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.profileImage = 'data:image/jpeg;base64,' + obj.profileImage;
      const userObj = JSON.parse(obj.user);
      this.profileFirstName = userObj.firstName;
      this.profileSurname = userObj.lastName;
      this.getPosts();
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
      const obj = JSON.parse(data);
      this.totalPosts = obj.totalElements;
      if (this.totalPosts > 0) {
        const numberOfPosts = obj.numberOfElements;
        this.showedPosts = this.showedPosts + numberOfPosts;
        console.log('total posts' + this.totalPosts);
        console.log('showed posts' + this.showedPosts);
        if (this.totalPosts > this.showedPosts) {
          this.loadMoreButton = true;
        } else {
          this.loadMoreButton = false;
        }
        console.log(this.loadMoreButton);
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

  loadMorePosts() {
    console.log('add!');
    // const newVal = this.test.length + 1;
    this.test.push(this.test.length + 1, this.test.length + 2, this.test.length + 3, this.test.length + 4, this.test.length + 5);
    this.getPosts();
  }

  // onScroll() {
  //   console.log("Scroll...");
  // }

}
