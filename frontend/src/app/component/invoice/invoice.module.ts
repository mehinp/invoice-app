import { NgModule } from '@angular/core';
import { SharedModule } from 'src/app/shared/shared.module';
import { InvoiceRoutingModule } from './invoice-routing.module';
import { InvoicesComponent } from './invoices/invoices.component';
import { NewinvoicecomponentComponent } from './newinvoice/newinvoicecomponent.component';
import { InvoiceDetailComponent } from './invoice-detail/invoice-detail.component';
import { NavbarModule } from '../navbar/navbar.module';


@NgModule({
  declarations: [
    InvoicesComponent,
    NewinvoicecomponentComponent,
    InvoiceDetailComponent
  ],
  imports: [
    SharedModule,
    InvoiceRoutingModule,
    NavbarModule
  ],
})
export class InvoiceModule {}
