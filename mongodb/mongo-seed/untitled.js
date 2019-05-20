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


