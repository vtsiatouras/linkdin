import { Component, OnInit, HostListener } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { environment } from '../../../environments/environment';

import { faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { faAngleDoubleUp } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  faCaretDown = faCaretDown;
  faEnvelope = faEnvelope;
  faAngleDoubleUp = faAngleDoubleUp;

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
