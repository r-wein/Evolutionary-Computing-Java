# EVOLUTIONARY COMPUTING PROJECT (JAVA)
Evolutionary Computing (EC) is a software engineering paradigm based on Charles Darwin’s theory of evolution.  An EC System 
functions by using elements of natural selection, genetic inheritance, and mutation in order to solve a problem.  This program is able to generate an equivalent expression to a given set of x and y coordinates.

## Program Sample
![EC System Demo Java](ECJava-2.gif)

## About this Project
### Origin
This is my final project for a software engineering class I took in the Summer of 2016.  It absolutely works, but many design decisions and coding practices show my inexperience at this.  I have plans to improve this project in the coming future.      

### Functionality
To run this Evolutionary Computing System, you need to first set up a few parameters first.

* Generation size => How many expressions do we have in each generation
* Genome size => How long can the expressions be in the initial population
* X-Training Data => The x value we will plug into our random expressions
* Y-Training Data => The y values we will match our output to to determine fitness
* Fitness Threshold => What percentage of the population will be selected to go on to the next generation
* Stagnation Threshold => If our fitness is not improving over this set number of generations, we reboot the system
* Mutation Percentage => Of the Individuals selected for the next generation, what percentage will we mutate
* Success Threshold => Define how close to the trainging data the generate expressions needs to be in order to be considered a success.

Once all parameters are set, you may pass them to the ECSystem and you are ready to go.

### Resources
To evaluate the String expressions in this project, I used [exp4j](http://www.objecthunter.net/exp4j/).



