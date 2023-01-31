# Here is a sample Makefile for building a Java program with Gradle:

#### makefile


```

# the name of the project
PROJECT_NAME=MyJavaProject

# build all the things
all: build

# build the project with gradle
build:
	./gradlew build

# run the project with gradle
run: build
	./gradlew run

# clean up the project with gradle
clean:
	./gradlew clean
 ``` 
  
# Here is a sample Makefile for building a Java program:

#### makefile

```
# the name of the main class
MAIN_CLASS=Main

# the name of the jar file
JAR_FILE=Main.jar

# build all the things
all: build

# build the jar file
build:
	javac $(MAIN_CLASS).java
	jar cvfe $(JAR_FILE) $(MAIN_CLASS) *.class

# run the jar file
run: build
	java -jar $(JAR_FILE)

# clean up
clean:
	rm -f *.class $(JAR_FILE)
 `` 
