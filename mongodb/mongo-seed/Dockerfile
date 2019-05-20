FROM mongo

COPY programs.json /programs.json
COPY programssubscriptions.json /programssubscriptions.json
CMD mongoimport --db DojoHepia --collection Programs --type json --file /programs.json --jsonArray
CMD mongoimport --db DojoHepia --collection ProgramsSubscritpion --type json --file /programssubscriptions.json --jsonArray
