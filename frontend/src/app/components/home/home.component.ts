import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  logoutUser() {
    console.log('logout');
    localStorage.clear();
    const req = this.http.post('http://localhost:8080/api/logout', {},
      {responseType: 'text', withCredentials: true}).subscribe((data: any) => {
        this.router.navigate(['/login']);
        },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }
}
