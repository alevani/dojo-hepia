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

    AggregateIterable<ProgramSubscription> kata = database.getCollection("Users", KataSubscription.class).aggregate(Arrays.asList(
                match(combine(eq("_id", userid), eq("programSubscriptions.idprogram", programid),eq("programSubscriptions.status",true))),
                unwind("$programSubscriptions"),
                project(
                        fields(excludeId(), include("katas"))),
                replaceRoot("$programSubscriptions")
        ));
        
,"programSubscriptions.idprogram":"0d02afc8-83ed-47fa-9269-3f02368f97ef","programSubscriptions.status":true

   db.Users.aggregate(
{$match: {"_id": "0","programSubscriptions.idprogram":"0d02afc8-83ed-47fa-9269-3f02368f97ef","programSubscriptions.status":true}}
).pretty

   db.Users.aggregate(
{$match: {"_id": "fd4c297b-44f6-4a0f-841a-40c606df6d47","programSubscriptions.katas._id":"6fee8e18-72b3-4ef8-8e14-e43ffbf0ec31"}},
{$unwind:"$programSubscriptions"},
{$replaceRoot: {newRoot:"$programSubscriptions"}},
{$project: {"_id":false,"katas":true}},
{$unwind:"$katas"},
{$match :{"katas._id":"6fee8e18-72b3-4ef8-8e14-e43ffbf0ec31"}}
).pretty()




        AggregateIterable<KataSubscription> kata = database.getCollection("Users", KataSubscription.class).aggregate(Arrays.asList(
                match(combine(eq("_id", userid), eq("programSubscriptions.idprogram", programid), eq("programSubscriptions.status", true))),
                unwind("$programSubscriptions"),
                replaceRoot("$programSubscriptions"),
                project(
                        fields(excludeId(), include("katas"))),
                match(eq("_id", kataid))
        ));

        	  db.Users.aggregate(
{$match: {"_id": "0","programSubscriptions.katas._id":"52cde578-24c4-4397-a70c-1a66f63cf758"}},
{$unwind:"$programSubscriptions"},
{$replaceRoot: {newRoot:"$programSubscriptions"}},
{$project: {"_id":false,"katas":true}},
{$unwind:"$katas"},
{$match :{"katas._id":"52cde578-24c4-4397-a70c-1a66f63cf758"}},
{$replaceRoot: {newRoot:"$katas"}}
).pretty()


{$replaceRoot: {newRoot:"$programSubscriptions"}},
{$project: {"katas":true,"_id":false}}
{$match }

{$unwind:"$programSubscriptions"},
{$project: {"programSubscriptions.katas":true,"_id":false}},
{$unwind:"$programSubscriptions.katas"}


    db.Users.update(
    	{},
	{"$pull": {"programSubscriptions":{"idprogram": "3a746466-2a15-492d-aa82-a85a26c084f6"} }},
	{multi:true}
    	);
{"_id": "7a7fa27a-0a03-4c38-9ea6-ea8ce05ebb00","programSubscriptions.katas._id":"ec105da6-6ef7-4eeb-84c9-68be706f6ea8"},
   
    db.Users.update(
    	{"_id": "7a7fa27a-0a03-4c38-9ea6-ea8ce05ebb00","programSubscriptions.katas._id":"ec105da6-6ef7-4eeb-84c9-68be706f6ea8"},
		{$inc: {"programSubscriptions.0.katas.$.nbAttempt": 1}}    	
    	);



 db.Users.update(
        {"_id":"0","programSubscriptions.katas._id":"a02aa850-1676-4b79-ad89-cc510d7c677a"},	
		{$inc: {"programSubscriptions.$[i].katas.$[j].nbAttempt": 1.0}},
		{arrayFilters: [{"i" : {"i.idprogram":"2ce48559-404a-481a-9664-e05a23de66f8"}},{"j" : {"j._id":"a02aa850-1676-4b79-ad89-cc510d7c677a"}}]}
    	);

 

	




 db.Users.update(
        {"_id":"0"},	
		{$inc: {"programSubscriptions.$[i].katas.$[j].nbAttempt": 1.0}},
		{arrayFilters: [{"i.idprogram" : { $eq:"2ce48559-404a-481a-9664-e05a23de66f8"}},{"j._id" : {$eq:"6d33e758-adf6-4f1b-a089-30543dce9e3b"}}]}
    	);











 AggregateIterable<ProgramSubscription> programids = database.getCollection("Users", ProgramSubscription.class).aggregate(Arrays.asList(
                match(combine(eq("_id", userid), eq("programSubscriptions.status", true))),
                unwind("$programSubscriptions"),
                project(
                        fields(excludeId(), exclude("katas"))),
                replaceRoot("$programSubscriptions")
        ));




db.Users.aggregate(
	{$match: {"_id":"29de7bfd-2304-489c-bd96-ec74127b76d5"}},
	{$project: {"_id":false,"programSubscriptions":true}},
	{$unwind: "$programSubscriptions"},
	{$match: {"programSubscriptions.status":true}},
	{$replaceRoot: {newRoot:"$programSubscriptions"}}

	).pretty();

db.Users.updateMany({"programSubscriptions.idprogram":"c4f038b3-1f8f-412c-a13f-dc18d084c2ce"},
	{$pull: {"programSubscriptions": {"idprogram":"c4f038b3-1f8f-412c-a13f-dc18d084c2ce"}}});






db.Users.aggregate(
	{$match: {"_id":"0","programSubscriptions.katas._id":"c8f2535c-6731-46fb-a933-06ce578cd6e2"}},
	{$project: {"_id":false,"programSubscriptions":true}},
	{$unwind: "$programSubscriptions"},
	{$replaceRoot: {newRoot:"$programSubscriptions"}},
	{$unwind: "$katas"},
	{$match: {"katas._id":"c8f2535c-6731-46fb-a933-06ce578cd6e2"}},
	{$replaceRoot: {newRoot:"$katas"}}
	{$project: {"_id":false,"status":true}}
	).pretty();














