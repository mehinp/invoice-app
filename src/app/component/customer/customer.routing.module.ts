import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { CustomerDetailComponent } from "./customer-detail/customer-detail.component";
import { NewcustomerComponent } from "./newcustomer/newcustomer.component";
import { CustomersComponent } from "./customers/customers.component";
import { AuthenticationGuard } from "src/app/guard/authentication.guard";


const customerRoutes: Routes = [
    { path : 'customers', component: CustomersComponent, canActivate: [AuthenticationGuard]}, 
    { path : 'customers/new', component: NewcustomerComponent, canActivate: [AuthenticationGuard]}, 
    { path : 'customers/:id', component: CustomerDetailComponent, canActivate: [AuthenticationGuard]}, 
];

@NgModule({
  imports: [RouterModule.forChild(customerRoutes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
