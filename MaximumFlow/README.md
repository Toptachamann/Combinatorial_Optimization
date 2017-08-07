Simple class for solving maximum flow problem. 
It has following interface: public void readGraph (String path) 
throws IOException takes a path to a file with a graph, which is represented as an adjacency matrix. First 
three integers are number of vertices, index of a source and index of a sink. All vertices are assumed to be 
indexed using 1-indexation. It throws IOException, if (obviously) an IOException occures, file doesn`t exist 
or contains wrong data (i.e. negative vertices` indexes, etc.). Public int getMaximumFlow() solves maximum flow 
problem uisng push-relabel algorithm or returns -1 if the graph hasn't been read already. Public void formulateLP
(String path) throws IOException formulate linear programming corresponding to the network
MaximumFlowTester has a method for generating random networks with given parameters and a method for testing push 
relabel vs. linear programming.
Probably, I'll add an option for comparing running times, but that seems kind of unnessesary, cause linear programming 
runs much more slower.
As always, feel free to fork, collaborate, pull requests or report bugs.