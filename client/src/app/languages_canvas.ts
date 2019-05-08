export class Canva {
  id: string;
  assertCanva: string;
  codeCanva: string;
  filename: string;
  assertname: string;
}


// TODO un kata n'a pas besoin de programtitile, ca devrait être passé par la classe d'en dessus (idem pour sensei)
export const LANG: Canva[] = [
  {
    id: 'python',
    assertCanva: 'from assertpy import assert_that\nimport sample as m' + '\n\n' +
      '# Example : \n# assert_that(m.yourfunction(someValues)).is_equal_to(targetedValues)',
    codeCanva: '# Write your code here',
    filename: 'sample.py',
    assertname: 'assert.py'
  },
  {
    id: 'java', assertCanva: 'import static org.junit.Assert.*;\n' +
      '\n' +
      'public class Main {\n' +
      '\n' +
      '    public static void main(String[] args) {\n' +
      '       // Example : \n// assertEquals(Kata.yourfunction(someValues,targetedValues))' +
      '    }\n' +
      '}', codeCanva: 'public class Kata {\n' +
      '\n' +
      '// Write your code here' +
      '\n' +
      '}\n',
    assertname: 'Main.java',
    filename: 'kata.java'
  }
];
