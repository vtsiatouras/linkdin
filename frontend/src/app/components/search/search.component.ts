import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

import { faSearch } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  email = localStorage.getItem('email');
  userToken = localStorage.getItem('userToken');

  faSearch = faSearch;
  showResults = false;
  totalResults = 0;
  results = [];
  userQuery: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  search() {
    this.results = [];
    this.showResults = true;
    if (this.userQuery != null) {
      const userAttrs = { userToken: this.userToken, email: this.email };
      const searchData = { searchQuery: this.userQuery };
      const req = this.http.post('http://localhost:8080/api/searchusers', {
        userAttrs,
        searchData
      }, { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        console.log(data);
        const obj = JSON.parse(data);
        this.totalResults = obj.numberOfResults;
        if (this.totalResults > 0) {
          for (let i = 0; i < this.totalResults; i++) {
            this.results.push(obj.list[i]);
          }
          console.log(this.results);
        }
      },
        (err: HttpErrorResponse) => {
          console.log(err);
        });
    } else {
      this.results = [];
      this.totalResults = 0;
    }
  }

}
