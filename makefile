# Makefile for JBase Database Engine

# JAR file to compile
JAR=JBase.jar

# Entry point for the runnable Jar file
MANIFEST=manifest.txt

# Classes to include
CLASSES=\
	jbase/Database.class \
	\
	jbase/field/Field.class \
	\
	jbase/user/User.class \
	\
	jbase/exception/JBaseException.class \
	jbase/exception/JBasePermissionException.class \
	\
	jbase/acl/ACL.class \
	jbase/acl/JBaseAction.class \
	jbase/acl/DatabaseAction.class \
	jbase/acl/FieldAction.class \
	jbase/acl/PermissionType.class

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
