import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SnackbarService } from './snackbar.service';

@Injectable({
    providedIn: 'root'
})
export class DataService {
    private apiEndpoint = environment.API_ENDPOINT;
    constructor(private http: HttpClient, private snackbarService: SnackbarService) { }

    get(api: string) {
        return this.http.get(this.apiEndpoint + api)
        .pipe(
            catchError(error => this.catchError(error))
        );
    }
    post(api: string, payload: any) {
        return this.http.post(this.apiEndpoint + api, payload)
        .pipe(
            catchError(error => this.catchError(error))
        );
    }

    delete(api: string) {
        return this.http.delete(this.apiEndpoint + api)
        .pipe(
            catchError(error => this.catchError(error))
        );
    }

    private catchError(errorResponse: any) {
        let errorMsg: string;
        if (errorResponse.error instanceof ErrorEvent) {
            errorMsg = `Error: ${errorResponse.error.message}`;
        } else if(typeof errorResponse.error === "string" || typeof errorResponse.error.error === "string") {
            errorMsg = errorResponse.error.error || errorResponse.error;
        } else {
            errorMsg = this.getServerErrorMessage(errorResponse);
        }
        this.snackbarService.openSnackBar(errorMsg);
        return throwError(errorMsg);
    }

    private getServerErrorMessage(error: HttpErrorResponse): string {
        switch (error.status) {
            case 404: {
                return `Not Found: ${error.message}`;
            }
            case 403: {
                return `Access Denied: ${error.message}`;
            }
            case 500: {
                return `Internal Server Error: ${error.message}`;
            }
            default: {
                return `Unknown Server Error: ${error.message}`;
            }

        }
    }
}
