JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java

CLASSES = CLASSES = DirectManip.java Drawable.java  Tractor.java  Magnet.java  CraneArm.java Block.java


default: classes

classes: $(CLASSES:.java=.class)

clean:
		$(RM) *.class