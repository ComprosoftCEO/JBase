# Makefile for JBase Database Engine

# JAR file to compile
JAR=JBase.jar

# Entry point for the runnable Jar file
MANIFEST=manifest.txt

# Classes to include
CLASSES=\
	jbase/Database.class


all: $(JAR)

$(JAR): $(CLASSES) $(MANIFEST)
	jar cvfm $@ $(MANIFEST) $(ENTRY) $^

%.class: %.java
	javac $<

# Run the main program
.PHONY: run
run: $(JAR)
	java -jar $(JAR)

.PHONY: clean
clean:
	rm -f $(CLASSES) $(JAR)
