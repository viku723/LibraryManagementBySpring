import { DataService } from './data.service';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UserService {
    apiEndpoint = environment.API_ENDPOINT;
    private userDetails: any;
    constructor(private http: HttpClient, private dataService: DataService) {
        if (this.isLoggedIn()) this.setUserDetails(JSON.parse(localStorage.getItem("userDetails") || ""));
    }

    login(userCredential: any) {
        return this.dataService.post("/user/login", userCredential);
    }

    isLoggedIn() {
        return !!(this.userDetails || localStorage.getItem("userDetails"));
    }

    setUserDetails(userDetails: Object) {
        this.userDetails = userDetails;
        localStorage.setItem("userDetails", JSON.stringify(userDetails));
    }

    getUserDetails() {
        return this.userDetails || JSON.parse(localStorage.getItem("userDetails") || "{}");
    }

    isAdmin() {
        return this.isLoggedIn() && (this.getUserDetails().roles || []).filter((role: { authority: string; }) => role.authority.toLowerCase().includes("admin")).length;
    }

    getAccessToken() {
        return this.getUserDetails().accessToken || "";
    }

    onLogout() {
        localStorage.removeItem("userDetails");
        this.userDetails = null;
    }
}
