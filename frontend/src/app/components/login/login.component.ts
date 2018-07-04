import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {User} from "../../models/user";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  user: User = new User();

  constructor(
    /*private route: ActivatedRoute,
    private router: Router*/) { }

  ngOnInit() {
  }

  loginUser() {
    console.log(this.user);
  }

}
