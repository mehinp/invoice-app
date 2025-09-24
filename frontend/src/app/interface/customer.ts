import { Invoice } from "./invoice";

export interface Customer {
    id: number;
    name: string;
    email: string;
    address: string;
    phone: string;
    type: string;
    status: string;
    imageUrl: string;
    createdAt: Date;
    invoices?: Invoice[];
}