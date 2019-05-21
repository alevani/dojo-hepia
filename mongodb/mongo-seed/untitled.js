db.ProgramsSubscription.find({"idprogram":"baac03de-2816-470e-8789-3c10b438364f","iduser":"0"}).map(
	doc => {doc.katas = doc.katas.filter(x => x._id == "55501e10-12fe-446f-aea1-2d4db550f1f6")}
	)






db.ProgramsSubscription.find({"idprogram":"baac03de-2816-470e-8789-3c10b438364f","iduser":"0", "katas._id": "41ea64c1-0711-46a8-aece-fae593283ad1"}).pretty()



db.ProgramsSubscription.aggregate( [
	{$match: {"iduser": "0", "idprogram": "baac03de-2816-470e-8789-3c10b438364f"}},
	{$unwind: "$katas"},
	{$match: {"katas._id": "41ea64c1-0711-46a8-aece-fae593283ad1"}},
	{$project: {"katas":true,"_id":false}}

	]).pretty()


programs.aggregate(Arrays.asList(Aggregates.unwind("$katas"),Aggregates.project(Projections.fields(Projections.excludeId(),Projections.include("katas"))),Aggregates.match(eq("katas._id",kataid))))


db.Programs.aggregate([
{$unwind:"$katas"},
{$project: {"katas":true,"_id":false}},
{$match: {"katas._id": "41ea64c1-0711-46a8-aece-fae593283ad1"}},
{$replaceRoot: {newRoot:"$katas"}}
]).pretty();


database.getCollection("ProgramsSubscription").updateOne(combine(eq("_id",userid),eq("idprogram", programid)),push("katas",new KataSubscription(kataid, "ON-GOING", "", 0)));

db.ProgramsSubscription.updateOne({_})

db.Users.aggregate([
{$match: {"_id": "0"}},
{$unwind:"$programSubscriptions"},
{$project: {"programSubscriptions":true,"_id":false}},
{$match: {"programSubscriptions.idprogram": "483685f4-aa41-4cf0-91cf-3e7ac467eea7"}},
{$replaceRoot: {newRoot:"$programSubscriptions"}}
]).pretty();


db.Programs.aggregate([
{$match: {"_id": "0"}},
{$unwind:"$programSubscriptions"},
{$project: {"programSubscriptions":true,"_id":false}},
]).pretty();

db.Programs.aggregate([
{$match: {"_id": "0"}},
]).pretty();






        database.getCollection("Users").updateOne(combine(eq("programSubscriptions.idprogram", idrogram), eq("iduser", userid)), bitwiseXor("programSubscriptions.status", 1));




    db.Users.updateOne();


    db.Users.aggregate(
{$match: {"_id": "d8412eff-dabb-4cd4-8c39-9cd336026195"}},
{$unwind:"$programSubscriptions"},
{$project: {"idprogram":true,"_id":false}},
{$replaceRoot: {newRoot:"$programSubscriptions"}}
    	);
