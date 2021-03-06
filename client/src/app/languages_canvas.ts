export interface Canva {
  id: string;
  assertCanva: string;
  codeCanva: string;
  filename: string;
  assertname: string;
}



export const LANG: Canva[] = [
  {
    id: 'python',
    assertCanva: 'from assertpy import assert_that\nfrom sample import *' + '\n\n' +
      '# Example : \n# assert_that(yourfunction(someValues)).is_equal_to(targetedValues)',
    codeCanva: '# Write your code here',
    filename: 'sample.py',
    assertname: 'assert.py'
  },
  {
    id: 'java', assertCanva: 'import static org.junit.Assert.*;\n' +
      '\n' +
      'public class Main {\n' +
      '\n' +
      '    public static void main(String[] args) {\n\n' +
      '// Example : \n// assertEquals(Kata.yourfunction(someValues,targetedValues))' +
      '    \n\n   }\n' +
      '}', codeCanva: 'public class Kata {\n' +
      '\n' +
      '}\n',
    assertname: 'Main.java',
    filename: 'kata.java'
  }
];
