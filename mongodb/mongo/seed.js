conn = new Mongo();
db = conn.getDB("DojoHepia");

db.createUser(
  {
    user: "shodai",
    pwd: "shodai",
    roles: [
       { role: "readWrite", db: "DojoHepia" }
    ]
  }
);

db.Users.insertMany([{"_id":"0","level":"shodai","password":"d033e22ae348aeb5660fc2140aec35850c4da997","username":"shodai","programSubscriptions":[{"id":"a161780d-77cb-46bc-87d2-c076a48f0349","idprogram":"cc5f95d4-c921-4663-9db7-c5839131aa08","katas":[{"_id":"dca8882c-2e20-4a3d-be79-dbbbf3f71e11","mysol":"def ret(n):\n    return n","nbAttempt":1,"status":"RESOLVED"}],"nbKataDone":1,"status":true}]}]);
db.Programs.insertMany([{"_id":"cc5f95d4-c921-4663-9db7-c5839131aa08","description":"Let's play with arrays in python !","idsensei":"0","katas":[{"_id":"dca8882c-2e20-4a3d-be79-dbbbf3f71e11","canva":"def ret(n):\n    ","cassert":"from assertpy import assert_that\nimport sample as m\n\nassert_that(m.ret(4)).is_equal_to(4)\n\nassert_that(m.ret(10)).is_equal_to(10)","difficulty":"Ceinture blanche","keepAssert":false,"language":"python","nbAttempt":10,"programID":"cc5f95d4-c921-4663-9db7-c5839131aa08","rules":"###### Le but est de retourner la valeur en paramètre\n\n\n```python\nprint(m.return(12))\n```\n\nDonnera\n\n```python\n> 12\n```","solution":"def ret(n):\n    return n","title":"Return n"}],"language":"python","nbKata":1,"sensei":"shodai","tags":["array","hashmap","list"],"title":"Arrays"}]);