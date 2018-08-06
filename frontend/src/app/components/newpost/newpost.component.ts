import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-newpost',
  templateUrl: './newpost.component.html',
  styleUrls: ['./newpost.component.css']
})
export class NewpostComponent implements OnInit {

  email = localStorage.getItem('email');
  userToken = localStorage.getItem('userToken');

  postContent: string;
  isAd: boolean;
  isPublic: boolean;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient) { }

  ngOnInit() {
    this.isAd = false;
    this.isPublic = false;
  }

  sharePost() {
    if (this.postContent != null) {
      console.log(this.postContent);
      console.log(this.isPublic);

      const userAttrs = { userToken: this.userToken, email: this.email };
      const postData = { postContent: this.postContent, isAd: this.isAd, isPublic: this.isPublic };


      const req = this.http.post('http://localhost:8080/api/newpost', {
        userAttrs,
        postData
      }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        this.postContent = '';
        this.isAd = false;
        this.isPublic = false;
      },
        (err: HttpErrorResponse) => {
          console.log(err);
        });
    }

  }
}
