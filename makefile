# Makefile for JBase Database Engine

# JAR file to compile
JAR=JBase.jar

# Entry point for the runnable Jar file
MANIFEST=manifest.txt

# Classes to include
CLASSES=\
	jbase/JBaseAction.class \
	jbase/ui/Test.class \
	\
	jbase/database/Database.class \
	jbase/database/DatabaseAction.class \
	jbase/database/User.class \
	\
	jbase/field/Field.class \
	jbase/field/FieldAction.class \
	jbase/field/FieldType.class \
	jbase/field/ParentField.class \
	jbase/field/ChildField.class \
	jbase/field/KeyField.class \
	jbase/field/ItemField.class \
	jbase/field/ForeignKeyField.class \
	\
	jbase/acl/ACL.class \
	jbase/acl/PermissionType.class \
	\
	jbase/exception/JBaseException.class \
	jbase/exception/JBaseDatabaseException.class \
	jbase/exception/JBaseDuplicateUser.class \
	jbase/exception/JBaseUserNotFound.class \
	jbase/exception/JBaseFieldNotFound.class \
	jbase/exception/JBaseDuplicateField.class \
	jbase/exception/JBaseFieldException.class \
	jbase/exception/JBaseBadFieldAction.class \
	jbase/exception/JBaseDuplicateData.class \
	jbase/exception/JBaseDataNotFound.class \
	jbase/exception/JBaseBadRow.class \
	jbase/exception/JBaseEndOfList.class \
	jbase/exception/JBaseBadResize.class \
	jbase/exception/JBaseOutOfMemory.class \
	jbase/exception/JBasePermissionException.class \
	jbase/exception/JBaseDatabaseActionDenied.class \
	jbase/exception/JBaseACLEditDenied.class \
	jbase/exception/JBaseFieldActionDenied.class

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
