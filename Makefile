#------------------------------------------------------------------------------
#  Makefile for CMPS 12B pa4
#  This Makefile uses wildcards and pattern substitution, which have not been
#  explained in any previous assignment.  See the GNU Make tutorial 
#
#       http://www.gnu.org/software/make/manual/make.html
#
#  to learn more about these features. 
#------------------------------------------------------------------------------

JAVAC      = javac 
MAINCLASS  = Simulation
JAVASRC    = $(wildcard *.java)
SOURCES    = $(JAVASRC) Makefile README Simulation.java QueueInterface.java QueueEmptyException.java Queue.java QueueTest.java Job.java
CLASSES    = $(patsubst %.java, %.class, $(JAVASRC))
JARCLASSES = $(patsubst %.class, %*.class, $(CLASSES))
JARFILE    = $(MAINCLASS) 
SUBMIT 	   = submit cmps012b-pt.u16 pa4
CHECK 	   = ls  /afs/cats.ucsc.edu/class/cmps012b-pt.u16/pa4/ajfische


all: $(JARFILE)

$(JARFILE): $(CLASSES)
	echo Main-class: $(MAINCLASS) > Manifest
	jar cvfm $(JARFILE) Manifest $(JARCLASSES)
	chmod +x $(JARFILE)
	rm Manifest

%.class: %.java
	$(JAVAC) $<

submit: $(SOURCES)
	$(SUBMIT) $(SOURCES)

clean:
	rm *.class $(JARFILE)

check:
	$(CHECK)