import {KataSubscription} from './KataSubscription';

export interface ProgramSubscription {
  id: string;
  iduser: string;
  idprogram: string;
  status: boolean;
  nbKataDone: number;
  katas: KataSubscription;
}
