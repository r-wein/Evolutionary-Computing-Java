# EVOLUTIONARY COMPUTING PROJECT (JAVA)
Evolutionary Computing (EC) is a software engineering paradigm based on Charles Darwin’s theory of evolution.  An EC System 
functions by using elements of natural selection, genetic inheritance, and mutation in order to solve a problem.  This program is able to generate an equivalent expression to a given set of x and y coordinates.

## Program Sample
![EC System Demo Java](ECJava-2.gif)

The numbers shown above the results are the fitness values of the most fit expression in each generation.

## About this Project
### Origin
This is my final project for a software engineering class I took in the Summer of 2016.  It absolutely works, but many design decisions and coding practices show my inexperience at this point.  I have plans to improve this project in the future.      

### Functionality
To run this Evolutionary Computing System, you need to set up a few parameters first.

* Generation size => Governs the number of expressions in each generation
* Genome size => Governs the length of the expressions in the initial population
* Training Data => The x and y values used to evalute an expression's fitness
* Fitness Threshold => The percentage of the population selected for the next generation
* Stagnation Threshold => If our fitness is not improving over this set number of generations, the EC System reboots
* Mutation Percentage => Governs the ratio of expressions crossed to expressions mutated
* Success Threshold => Defines the minimum fitness value required for the system to deem the expression equivalent

Once all parameters are set, you may pass them to the ECSystem and you are ready to go.

![Example Parameters](ECParameters.png)

### Resources
To evaluate the String expressions in this project, I used [exp4j](http://www.objecthunter.net/exp4j/).



