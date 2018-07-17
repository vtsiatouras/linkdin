import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { faCaretDown } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  faCaretDown = faCaretDown;
  firstName = localStorage.getItem('firstName');
  lastName = localStorage.getItem('lastName');

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) { }

  ngOnInit() {
  }

  clickLogo() {
    console.log("logo clicked");
    this.router.navigate(['/home']);
  }

  logoutUser() {
    console.log('logout');
    localStorage.clear();
    const req = this.http.post('http://localhost:8080/api/logout', {},
      { responseType: 'text', withCredentials: true }).subscribe((data: any) => {
        this.router.navigate(['/login']);
      },
        (err: HttpErrorResponse) => {
          console.log(err);
        });
  }

}
