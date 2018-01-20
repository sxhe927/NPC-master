# This is a project for cse6140 in Georgia Tech

To run: 

java -jar MVC.jar -inst <path_to_graph> -alg <LS1/LS2/Approx/BnB> -time <in seconds> -seed <any number>

For eg:

java -jar MVC.jar -inst Data/star.graph -alg LS1 -time 30 -seed 1

====================
The code folder contains all the source code for the algorithms

The output folder contains output .sol and .trace files

manifest.txt is required for creating jar file


Code structure:

Main-Class: RunExperiments

Branch and Bound: Bnb.java

LS1: LS1.java

LS2: LS2.java

Construction Heuristics: Heuristic.java

Everything else : Support files for custom data structure

====================

If above method doesn't work: Please read the following instructions for compilation. 

If the MVC.jar does not exist, it might be because the server rejected it due to security issues.

The code must be compiled first. To compile, make sure the manifest.txt file is present in the working directory and type this in command line: 

javac code/*.java -d .

jar cvfm MVC.jar manifest.txt *.class

rm *.class  # to clean

To run: 

Make sure you are present in the root directory of the submission. There should be a file named MVC.jar which is the executable.

To run, type the following in command line

java -jar MVC.jar -inst Data/star.graph -alg LS1 -time 30 -seed 1

The solution and the trace files will be generated in the folder this is run from.
