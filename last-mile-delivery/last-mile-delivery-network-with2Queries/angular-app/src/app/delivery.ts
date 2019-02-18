import {Asset} from './org.hyperledger.composer.system';
import {Participant} from './org.hyperledger.composer.system';
import {Transaction} from './org.hyperledger.composer.system';
import {Event} from './org.hyperledger.composer.system';
// export namespace delivery{
   export abstract class ProductHolder extends Participant {
      holderId: string;
   }
   export class Warehouse extends ProductHolder {
      location: string;
   }
   export class Transporter extends ProductHolder {
   }
   export class Courier extends ProductHolder {
   }
   export class Customer extends ProductHolder {
   }
   export class Product extends Asset {
      productId: string;
      productStatus: string;
      productHolder: ProductHolder;
   }
   export class SendToTransporter extends Transaction {
      product: Product;
      transporter: Transporter;
   }
   export class SendToWarehouse extends Transaction {
      product: Product;
      warehouse: Warehouse;
   }
   export class SendToCourier extends Transaction {
      product: Product;
      courier: Courier;
   }
   export class SendToCustomer extends Transaction {
      product: Product;
      customer: Customer;
   }
   export class RecieveProduct extends Transaction {
      product: Product;
   }
// }
