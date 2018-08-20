import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { environment } from '../../../environments/environment';


@Component({
  selector: 'app-newpost',
  templateUrl: './newpost.component.html',
  styleUrls: ['./newpost.component.css']
})
export class NewpostComponent implements OnInit {

  userId = localStorage.getItem('userID');
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
    if (this.postContent) {
      // TODO clean the below
      console.log(this.postContent);
      console.log(this.isPublic);

      const userIdentifiers = { userToken: this.userToken, id: this.userId };
      const postData = { postContent: this.postContent, isAd: this.isAd, isPublic: this.isPublic };

      const API_URL = environment.API_URL;
      const req = this.http.post(API_URL + '/api/newpost', {
        userIdentifiers,
        postData
      }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        // Reset fields
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
