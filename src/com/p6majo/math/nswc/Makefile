#c compiler 
#pic: position independent code
#shared: shared library

CCFLAGS  = -fpic -shared
#for cpp files
#CPPDEFINES = 
#for fortran files
DEPS = NswcMath.h

# makefile begins
# define a variable for compiler flags (JFLAGS)
# define a variable for the compiler (JC)  
# define a variable for the Java Virtual Machine (JVM)
# define a variable for a parameter. When you run make, you could use:

JFLAGS = -g
JC = javac
JVM = java
JH = javah




#c includes and libraries
	
IDIRS  = -I/usr/lib/jvm/java-8-oracle/include -I/usr/lib/jvm/java-8-oracle/include/linux
CCLIBS = -lc -lm
PTH_JAVA = /home/jmartin/workbase/toe/out/

OFILES = nswc.so


# compilation of c files
libNswcMath.so : $(OFILES) $(DEPS)
	g++ $(CCFLAGS) -o libNswcMath.so $(OFILES) $(CCLIBS) $(IDIRS) NswcMath.cpp

nswc.so  : nswc.f90
	gfortran $(CCFLAGS) -c nswc.f90 -o nswc.so

#compilation of java files

NswcMath.h:	NswcMath.class
	javah -cp $(PTH_JAVA) -jni -o NswcMath.h com.p6majo.math.nswc.NswcMath

java: 
	$(JC) *.java -cp $(PTH_JAVA)


clean:
	$(RM) *.o *.so NswcMath.h
