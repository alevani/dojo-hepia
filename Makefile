JVM= java
EXEC= mvn exec:$(JVM)
PACKAGE= mvn package
DIR_COMP= compilation/
DIR_GATE= gateway/
DIR_CLIE= client/
ANGC= ng serve --open

COMP_PORT= 6999
GATE_PORT= 7000

all: package

run:
	cd $(DIR_GATE) ; $(PACKAGE) ; $(EXEC) &
	cd $(DIR_COMP) ; $(PACKAGE) ; $(EXEC) &
	cd $(DIR_CLIE) ; $(ANGC)

clean: SHELL:=/bin/bash
	@echo "kill $(lsof -t -i :6999)"
#	kill $(lsof -t -i :$(GATE_PORT))
	