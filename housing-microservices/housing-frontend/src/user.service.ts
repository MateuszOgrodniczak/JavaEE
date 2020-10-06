import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {User} from './user';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Building} from './building';
import {ApplicationForm} from './application-form';
import {Statistics} from './statistics';
import {LoginForm} from "./app/login-form";
import {OauthToken} from "./app/access-token";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient) {
  }

  getUsers(): Observable<User[]> {
    return this.httpClient.get<User[]>(`http://localhost:8000/housing/users/`, {responseType: 'json'});
  }

  getUser(index: number): Observable<User> {
    return this.httpClient.get<User>(`http://localhost:8000/housing/users/` + index, {responseType: 'json'});
  }

  getBuildings(): Observable<Building[]> {
    return this.httpClient.get<Building[]>(`http://localhost:8000/housing/buildings/`, {responseType: 'json'});

  }

  getBuilding(index: number): Observable<Building> {
    return this.httpClient.get<Building>(`http://localhost:8000/housing/buildings/` + index, {responseType: 'json'});
  }

  getApplications(): Observable<ApplicationForm[]> {
    return this.httpClient.get<ApplicationForm[]>(`http://localhost:8000/application/`, {responseType: 'json'});
  }

  getApplication(index: string): Observable<ApplicationForm> {
    return this.httpClient.get<ApplicationForm>(`http://localhost:8000/application/` + index, {responseType: 'json'});
  }

  getStatistics(): Observable<Statistics> {
    return this.httpClient.get<Statistics>(`http://localhost:8000/housing/statistics/`, {responseType: 'json'});
  }

  sendEmail(emailForm: string, accessToken: OauthToken): Observable<object> {
    console.log("access token: " + accessToken);
    return this.httpClient.post(`http://localhost:8000/notification/sendCustomEmail`, emailForm, {
      headers: new HttpHeaders({'Content-Type': 'application/json', 'Authorization': `${accessToken.token_type} ${accessToken.access_token}`})
    });
  }

  obtainAccessToken(loginData: LoginForm): Observable<object> {
    console.log(loginData);

    let body = `grant_type=${loginData.grant_type}&username=${loginData.username}&password=${loginData.password}`;

    let headers =
      new HttpHeaders({
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        'Authorization': 'Basic ' + btoa('politechnika' + ':' + "politechnika1")
      });

    return this.httpClient.post('http://localhost:8000/oauth/token', body, {
      headers: headers
    });
  }
}
