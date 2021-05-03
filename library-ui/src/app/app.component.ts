import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from './services/user.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    title = 'library-ui';
    constructor(public userService: UserService, private route: Router) {
        if (this.userService.isLoggedIn()) this.route.navigate(["/home"])
    }

    onSignOut() {
        this.userService.onLogout();
        this.route.navigate(["/login"]);
    }
}
