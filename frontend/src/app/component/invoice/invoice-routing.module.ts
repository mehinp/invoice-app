import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AuthenticationGuard } from "src/app/guard/authentication.guard";
import { InvoiceDetailComponent } from "./invoice-detail/invoice-detail.component";
import { InvoicesComponent } from "./invoices/invoices.component";
import { NewinvoicecomponentComponent } from "./newinvoice/newinvoicecomponent.component";


const invoiceRoutes: Routes = [
    { path : 'invoices/new', component: NewinvoicecomponentComponent, canActivate: [AuthenticationGuard]}, 
    { path : 'invoices', component: InvoicesComponent, canActivate: [AuthenticationGuard]}, 
    { path : 'invoices/:id/:invoiceNumber', component: InvoiceDetailComponent, canActivate: [AuthenticationGuard]}, 
];

@NgModule({
  imports: [RouterModule.forChild(invoiceRoutes)],
  exports: [RouterModule]
})
export class InvoiceRoutingModule { }
