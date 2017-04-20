# EVOLUTIONARY COMPUTING PROJECT (JAVA)
Evolutionary Computing (EC) is a software engineering paradigm based on Charles Darwin’s theory of evolution.  An EC System 
functions by using elements of natural selection, genetic inheritance, and mutation in order to solve a problem.  This program is able to generate an expression which equates to a given set of x and y coordinates.

## Sample Output
![EC System Demo Java](ECJava-2.gif)

The numbers shown above the results are the fitness values of the most fit expression in each generation.

## Sample Code


## About this Project
### Origin
This is my final project for a software engineering class I took in the Summer of 2016.  It absolutely works, but many design decisions and coding practices show my inexperience at this point.  I have plans to improve this project in the future.      

### Installation

### How To Run
To run this Evolutionary Computing System, you need to set up a few parameters first.

```java
	  ECSystemParameters ecParams = new ECSystemParameters();
		
		// Governs the number of expressions in each generation
		ecParams.setPopulationSize(400);
		
		// Governs the length of the expressions in the initial population
		ecParams.setGenomeSize(20);
		
		// The percentage of the population selected for the next generation
		ecParams.setFitnessThreshold(.2);
		
		// The percentage of the population selected for mutation
		ecParams.setMutationPercentage(.1);
		
		// If our fitness is not improving over this set number of generations, the EC System reboots
		ecParams.setStagnationThreshold(45);
		
		// Minimum fitness value required for the system to deem the expression equivalent to training data
		ecParams.setSuccessThreshold(0.01);
		
		// Training Data: The x and y values used to evaluate the expression's fitness
		ecParams.setValuesForXTrainingData(Arrays.asList(-31,-11,-8,-4,-1,1,5,10,20,31));
		ecParams.setValuesForYTrainingData(Arrays.asList(640.0,80.0,42.0,10.0,0.0,0.0,16.0,66.0,266.0,640.0));
		
		ECSystem evo = new ECSystem(ecParams);
		evo.showResultStream();
		evo.runECSystem();
		
		// System Results
		System.out.println(evo.getSystemStatistics().toString());
```

### Testing
I wrote this program before I knew of JUnit. I wrote my own unit tests and they can be viewed here.

### Future Improvements


## Resources
To evaluate the String expressions in this project, I used [exp4j](http://www.objecthunter.net/exp4j/).

## License


