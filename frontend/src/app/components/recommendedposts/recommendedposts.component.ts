import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-recommendedposts',
  templateUrl: './recommendedposts.component.html',
  styleUrls: ['./recommendedposts.component.css']
})
export class RecommendedpostsComponent implements OnInit {

  userId = localStorage.getItem('userID');
  userToken = localStorage.getItem('userToken');

  // Posts variables
  renderPosts = false;
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
    this.totalPosts = 0;
    this.showedPosts = 0;
    this.loadMoreButton = false;
    this.page = 0;
    this.limitPosts = 5;
    this.posts = [];
    this.renderPosts = false;
    this.getPosts();
  }

  getPosts() {
    const userIdentifiers = { userToken: this.userToken, id: this.userId };
    this.page++;
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/recommendedposts', {
      userIdentifiers,
    }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
      const obj = JSON.parse(data);
      this.totalPosts = obj.totalElements;
      if (this.totalPosts > 0) {
        const numberOfPosts = obj.numberOfElements;
        //   this.showedPosts = this.showedPosts + numberOfPosts;
        //   if (this.totalPosts > this.showedPosts) {
        //     this.loadMoreButton = true;
        //   } else {
        //     this.loadMoreButton = false;
        //   }
        for (let i = 0; i < numberOfPosts; i++) {
          this.posts.push(obj.content[i]);
        }
        // } else {
        //   this.posts = null;
      }
      console.log(this.posts);
      this.renderPosts = true;
    },
      (err: HttpErrorResponse) => {
        console.log(err);
        this.router.navigate(['/error', false], { skipLocationChange: true });
      });
  }

}
