import { Router } from '@angular/router';
import { DataService } from './../../services/data.service';
import { Component, Input, OnInit } from '@angular/core';
import { switchMap } from 'rxjs/operators';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';

export enum BOOK_LIST_TYPE  {
    All,
    Issued,
    Borrowed,
    AvailableToBorrow
}

@Component({
    selector: 'app-book-list',
    templateUrl: './book-list.component.html',
    styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {
    @Input() bookListType: any;
    bookListTypeEnum: typeof BOOK_LIST_TYPE = BOOK_LIST_TYPE;
    displayedColumns: string[] = [];
    books$: any;

    constructor(private dataService: DataService, private router: Router, private snackbarService: SnackbarService, public userService: UserService) { }

    ngOnInit(): void {
        
        this.getBookListByType(this.bookListType);
    }
    
    private getBookListByType(bookListType: any) {
        let Api: string = '';
        //TODO: Create a helper to get API URLs
        switch (bookListType) {
            case BOOK_LIST_TYPE.All:
                Api = "/book/all";
                break;

            case BOOK_LIST_TYPE.Issued:
                Api = "/book/issued";
                break;

            case BOOK_LIST_TYPE.AvailableToBorrow:
                Api = "/book/availableBooks";
                break;

            case BOOK_LIST_TYPE.Borrowed:
                Api = "/book/borrowed";
                break;
        }
        this.books$ = this.dataService.get("/book/columns/name")
            .pipe(switchMap((columns: any) => {
                //It's not perfect solution but to try out something new
                this.displayedColumns = [...columns, "actions"].filter(coumn => coumn != "issued_to");
                return this.dataService.get(Api);
            }));
    }

    onAddBook() {
        this.router.navigate(["/addBook"]);
    }

    onEditBook(id: any) {
        this.router.navigate(["/addBook/" + id]);
    }

    onDeleteBook(id: any) {
        this.dataService.delete("/book/" + id)
            .subscribe((response: any) => {
                this.snackbarService.openSnackBar(response.success);
                this.getBookListByType(this.bookListType);
            });
    }

    onBorrow(id: any) {
        this.dataService.post("/book/borrow/" + id, null)
        .subscribe((response: any) => {
            this.snackbarService.openSnackBar(response.success);
            this.getBookListByType(this.bookListType);
        })
    }
    onReturn(id: any) {
        this.dataService.post("/book/return/" + id, null)
        .subscribe((response: any) => {
            this.snackbarService.openSnackBar(response.success);
            this.getBookListByType(this.bookListType);
        })
    }
}
