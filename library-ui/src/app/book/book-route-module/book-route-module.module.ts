import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { AddBookComponent } from '../add-book/add-book.component';



const routes: Routes = [
  {
    path: "addBook",
    component: AddBookComponent
  },
  {
    path: "addBook/:id",
    component: AddBookComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class BookRouteModuleModule { }