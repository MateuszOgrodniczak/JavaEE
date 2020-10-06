import { Component, OnInit } from '@angular/core';
import {UserService} from '../../user.service';

@Component({
  selector: 'app-applications',
  templateUrl: './applications.component.html',
  styleUrls: ['./applications.component.css']
})
export class ApplicationsComponent implements OnInit {
  applications = [];
  service;

  constructor(service: UserService) {
    this.service = service;
  }

  ngOnInit() {
    this.getApplications();
  }

  getApplications(): void {
    this.service.getApplications()
      .subscribe(applications => this.applications = applications);
  }
}
