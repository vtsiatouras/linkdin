import {Component, HostListener, OnInit} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-adminnavbar',
  templateUrl: './adminnavbar.component.html',
  styleUrls: ['./adminnavbar.component.css']
})
export class AdminnavbarComponent implements OnInit {

  top = true;

  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');
  userID = localStorage.getItem('userID');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
  ) { }


  ngOnInit() { }

  @HostListener('window:scroll')
  public onScroll() {
    if (window.scrollY === 0) {
      this.top = true;
    } else {
      this.top = false;
    }
  }

  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  logoutUser() {
    console.log('logout');
    localStorage.clear();
    const API_URL = environment.API_URL;
    const req = this.http.post(API_URL + '/api/logout', {},
      { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        this.router.navigate(['/login']);
      },
      (err: HttpErrorResponse) => {
        console.log(err);
      });
  }

}
