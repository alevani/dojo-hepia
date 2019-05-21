import {KataSubscription} from './KataSubscription';

export interface ProgramSubscription {
  _id: string;
  id: string;
  idprogram: string;
  status: boolean;
  nbKataDone: number;
  katas: KataSubscription;
}
