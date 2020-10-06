import {User} from './user';
import {Address} from './address';
import {Apartment} from './apartment';

export class Building {
  id: number;
  removed: boolean;
  name: string;
  address: Address;
  owner: User;
  apartments: Apartment[];
}
