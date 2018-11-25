# Makefile for JBase Database Engine

# JAR file to compile
JAR=JBase.jar

# Entry point for the runnable Jar file
MANIFEST=manifest.txt

# Classes to include
CLASSES=\
	jbase/JBaseAction.class \
	\
	jbase/database/Database.class \
	jbase/database/DatabaseAction.class \
	jbase/database/User.class \
	\
	jbase/field/Field.class \
	jbase/field/FieldAction.class \
	jbase/field/FieldType.class \
	jbase/field/JBaseField.class \
	jbase/field/ParentField.class \
	jbase/field/ChildField.class \
	jbase/field/PointableField.class \
	jbase/field/PointerField.class \
	jbase/field/KeyField.class \
	jbase/field/ItemField.class \
	jbase/field/ForeignKeyField.class \
	\
	jbase/acl/ACL.class \
	jbase/acl/PermissionType.class \
	\
	jbase/exception/JBaseException.class \
	jbase/exception/JBaseIOException.class \
	jbase/exception/JBaseBadDatabase.class \
	\
	jbase/exception/JBaseDatabaseException.class \
	jbase/exception/JBaseDuplicateDatabase.class \
	jbase/exception/JBaseDuplicateUser.class \
	jbase/exception/JBaseUserNotFound.class \
	jbase/exception/JBaseFieldNotFound.class \
	jbase/exception/JBaseDuplicateField.class \
	jbase/exception/JBaseWrongDatabase.class \
	\
	jbase/exception/JBaseFieldException.class \
	jbase/exception/JBaseBadFieldAction.class \
	jbase/exception/JBaseDuplicateData.class \
	jbase/exception/JBaseDataNotFound.class \
	jbase/exception/JBaseBadRow.class \
	jbase/exception/JBaseEndOfList.class \
	jbase/exception/JBaseBadResize.class \
	jbase/exception/JBaseOutOfMemory.class \
	\
	jbase/exception/JBasePermissionException.class \
	jbase/exception/JBaseDatabaseActionDenied.class \
	jbase/exception/JBaseACLEditDenied.class \
	jbase/exception/JBaseFieldActionDenied.class \
	\
	jbase/ui/JBaseDialog.class \
	jbase/ui/MainDialog.class \
	jbase/ui/DatabaseDialog.class \
	jbase/ui/SelectKeyDialog.class \
	jbase/ui/KeyDialog.class

all: $(JAR)

$(JAR): $(CLASSES) $(MANIFEST)
	jar cvfm $@ $(MANIFEST) $(CLASSES)

%.class: %.java
	javac $<

# Run the main program
.PHONY: run
run: $(JAR)
	java -jar $(JAR)

.PHONY: clean
clean:
	rm -f $(CLASSES) $(JAR)
