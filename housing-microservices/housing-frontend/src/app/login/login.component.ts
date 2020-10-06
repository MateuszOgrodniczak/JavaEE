import { Component, OnInit } from '@angular/core';
import {LoginForm} from "../login-form";
import {ToastrService} from 'ngx-toastr';
import {UserService} from "../../user.service";
import { CookieService } from 'ngx-cookie-service';
import {OauthToken} from "../access-token";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  model = new LoginForm('password', '', '');

  constructor(private userService: UserService, private cookieService: CookieService, private toastr: ToastrService) { }

  ngOnInit() {
  }

  informAboutLoginProcessing(): void {
    this.toastr.info('Signing in...');
  }

  handleSuccess(): void {
    this.toastr.success('Login successful');
  }

  handleError(): void {
    this.toastr.error('Login failure');
  }

  onSubmit() {
    this.informAboutLoginProcessing();
    const response = this.userService.obtainAccessToken(this.model);
    response
      .subscribe(
        token => {
          this.cookieService.set('access_token', JSON.stringify(token));
          this.handleSuccess();
        },
        error => {
            this.handleError();
            console.log(error);
        }
      )
  }
}
