import {Kata} from './kata';

// TODO un kata n'a pas besoin de programtitile, ca devrait être passé par la classe d'en dessus (idem pour sensei)
export const KATAS: Kata[] = [{
  id: 0,
  language: 'python',
  programTitle: 'Arrays in Python',
  sensei: 'Orestis Pileas Malaspinas',
  title: 'Multiplication par 2',
  canva: 'def multiTwo(n):',
  rules: 'Le but ici est de jouer avec les tableaux en python. dans cet exercice, il faut multiplier tous les éléments d\'un tableau par 2.\neasyline([2,4]) donnera par exemple [4,8]',
  assert: 'from assertpy import assert_that\n' +
    'import sample as m\n' +
    '\n' +
    'assert_that(m.multiTwo([1,2])).is_equal_to([2,4])\n' +
    'assert_that(m.multiTwo([45,53,12])).is_equal_to([90,106,24])\n' +
    'assert_that(m.multiTwo([3])).is_equal_to([6])',
  solution: 'def multiTwo(n):\n' + '  return [i*2 for i in n]'
}];
