import { Component, OnInit } from '@angular/core';
import {UserService} from '../../user.service';
import {EmailForm} from '../email-form';
import {ToastrService} from "ngx-toastr";
import { CookieService } from 'ngx-cookie-service';
import {OauthToken} from "../access-token";

@Component({
  selector: 'app-email',
  templateUrl: './email.component.html',
  styleUrls: ['./email.component.css']
})
export class EmailComponent implements OnInit {
  submitted = false;
  emailsToLogins = new Map();
  userService: UserService;
  model = new EmailForm('', '', '', '');
  userAuthenticated = false;

  constructor(userService: UserService, private toastr: ToastrService, private cookieService: CookieService) {
    this.userService = userService;
  }

  ngOnInit() {
    const oauthToken = this.cookieService.get('access_token');
    console.log(oauthToken);
    if(oauthToken != null && oauthToken != '') {
      this.userAuthenticated = true;
      this.getEmails();
    } else {
      this.userAuthenticated = false;
    }
  }

  onSubmit() {
    this.submitted = true;
    let subject = this.model.subject;
    this.model.subject = 'Statistics Module - ' + subject;
    this.model.username = this.emailsToLogins.get(this.model.addressee);
    const json = JSON.stringify(this.model);
    console.log(json);

    const tokenJson = this.cookieService.get('access_token');
    console.log(tokenJson);
    const accessToken: OauthToken = JSON.parse(tokenJson);
    console.log(accessToken);

    const response = this.userService.sendEmail(json, accessToken);

    this.informAboutEmailSending();

    response.subscribe(
      success => this.handleSuccess(),
      error => {
        console.log(error);
        this.handleError()
      }
    );
    this.model.subject = subject;
  }

  informAboutEmailSending(): void {
    this.toastr.info('Sending email message...');
  }

  handleSuccess(): void {
    this.toastr.success('Email sent');
  }

  handleError(): void {
    this.toastr.error('Email could not be sent');
  }

  getEmails(): void {
    this.userService.getUsers()
      .subscribe(users => {
        users.forEach(user => {
          this.emailsToLogins.set(user.email, user.username);
        });
      });
  }
}
