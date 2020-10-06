import { Component, OnInit } from '@angular/core';
import {User} from '../../user';
import {ActivatedRoute} from '@angular/router';
import {UserService} from '../../user.service';
import {Location} from '@angular/common';
import {ApplicationForm} from '../../application-form';
import {Building} from '../../building';

@Component({
  selector: 'app-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrls: ['./item-detail.component.css']
})
export class ItemDetailComponent implements OnInit {
  user: User;
  building: Building;
  application: ApplicationForm;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private location: Location
  ) {}

  ngOnInit() {
    const path = this.route.snapshot.routeConfig.path;
    if (path.includes('user')) {
      this.getUser();
    } else if (path.includes('building')) {
      this.getBuilding();
    } else if (path.includes('application')) {
      this.getApplication();
    }
  }

  getUser(): void {
    const index = +this.route.snapshot.paramMap.get('id');
    this.userService.getUser(index)
      .subscribe(user => this.user = user);
  }

  getBuilding(): void {
    const index = +this.route.snapshot.paramMap.get('id');
    this.userService.getBuilding(index)
      .subscribe(building => this.building = building);
  }

  getApplication(): void {
    const index = this.route.snapshot.paramMap.get('id');
    this.userService.getApplication(index)
      .subscribe(application => this.application = application);
  }

  goBack(): void {
    this.location.back();
  }
}
