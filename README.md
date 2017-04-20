# EVOLUTIONARY COMPUTING PROJECT (JAVA)
Evolutionary Computing (EC) is a software engineering paradigm based on Charles Darwin’s theory of evolution.  An EC System 
functions by using elements of natural selection, genetic inheritance, and mutation in order to solve a problem.  This program is able to generate an expression which equates to a given set of x and y coordinates.  
  
For example, when given the coordinates (-31, 640), (-11,80), (1,0) and (20, 266), it will produce an expression like:  
  
    (x/x/3)+(x+6/x\*x-x+x\*x-7-x\*x/3)

## Sample Output
![EC System Demo Java](ECJava-2.gif)

The numbers shown above the results are the fitness values of the most fit expression in each generation (the lower the number, the more fit the expression).

## Sample Code
```java
private List<Individual> createInitialPopulation(int genNum) {

	/*
	 * check the parameters for generationSize and genomeSize and create as
	 * many new Individual objects as needed
	 */

	// create a new list to hold the new Individuals
	List<Individual> population = new ArrayList<Individual>();

	// add the specified number of new Individuals to the list
	for (int x = 0; x < this.parameters.getPopulationSize(); x++) {
		population.add(new Individual(this.parameters.getGenomeSize(), genNum));
		this.stats.addToOperationCount();
	}

	this.stats.addToGenerationTotal();
	this.stagnantCount++;

	// return the newly created list of Individuals
	return population;
}
```


## About this Project
### Origin
This is my final project for a software engineering class I took in the Summer of 2016.  It absolutely works, but many design decisions and coding practices show my inexperience at this point.  

### Installation
Currently, the only way to get this code is to clone the repository.
```
$ git clone https://github.com/rossweinstein/Evolutionary-Computing-Java
```

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
I wrote this program before I knew of JUnit. I wrote my own unit tests and they can be viewed [here](https://github.com/rossweinstein/Evolutionary-Computing-Java/tree/master/Java%20Files/unitTests).

### Future Improvements
1. Many classes, and some methods, do too much so a bit of refactoring is in order.  
1. This program can only generate basic expressions written with +, -, *, and / at the moment. I would like to improve this program to generate trigonometric functions as well.

## Resources
I used [exp4j](http://www.objecthunter.net/exp4j/) to evaluate the String expressions in this project. 

## License
[MIT License](https://en.wikipedia.org/wiki/MIT_License)

