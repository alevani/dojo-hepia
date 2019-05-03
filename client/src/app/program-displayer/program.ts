interface Kata {
  [key: string]: number;
}

export class Program {
  id: number;
  title: string;
  language: string;
  sensei: string;
  nbKata: number;
  description: string;
  kata: Kata;
  tag: string[];
}
