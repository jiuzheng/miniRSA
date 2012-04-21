JFLAGS = -g
JC = javac

default: JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	ChatClient.java \
	ChatServer.java \
	RSA.java \
	Global.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

runServer: ChatServer.class
	$ java ChatServer

runClient: ChatClient.class
	$ java ChatClient