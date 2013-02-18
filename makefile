default:
		javac ./cranepackage/DirectManip.java
		java cranepackage/DirectManip
run:
		javac ./cranepackage/DirectManip.java
		java cranepackage/DirectManip

clean:
		$(RM) cranepackage/*.class