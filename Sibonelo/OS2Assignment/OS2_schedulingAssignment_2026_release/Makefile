JAVAC = javac
JAVA = java
SRC = src/barScheduling/*.java
BIN = bin
MAIN = barScheduling.SchedulingSimulation

$(BIN):
	mkdir -p $(BIN)

compile: $(BIN)
	$(JAVAC) -d $(BIN) $(SRC)

run: compile
	$(JAVA) -cp $(BIN) $(MAIN) $(ARGS)

clean:
	rm -rf $(BIN)
	rm -rf results

