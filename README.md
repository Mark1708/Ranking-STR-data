# Ranking an array of STR data
The utility evaluates TMRCA (Time to the Most Recent Common Ancestor) by Y-STR loci and then performs ranking relative to the base haplotype.

* [TMRCA](https://en.wikipedia.org/wiki/Most_recent_common_ancestor)
* [Poisson distribution](https://en.wikipedia.org/wiki/Poisson_distribution)
* [Y-STR](https://en.wikipedia.org/wiki/Haplotype#Y-DNA_haplotypes_from_genealogical_DNA_tests)

TMRCA calculations among civilian researchers mainly use data on tandem mutations at STR (Short Tandem Repeats) loci of the male chromosome.




Launching:
---
     Options:

        * -a, --age
          Average age
          Default: 0
          -h, --help
          Help/Usage
        * -i, --index
          Index of haplotype
        * -p, --path
          Path to csv file

    Execute the jar:

        java -jar target/ranking.jar -p /path/to/DataSet.csv -i indexOfHaplotype -a averageAge
        java -jar target/ranking.jar -h
        java -jar target/ranking.jar

Example of Using:
---
![Exanple1](https://github.com/Mark1708/TextReadability/blob/master/assets/Exanple1.png)
![Exanple2](https://github.com/Mark1708/TextReadability/blob/master/assets/Exanple2.png)
![Exanple2](https://github.com/Mark1708/TextReadability/blob/master/assets/Exanple3.png)

Example of Data^
---
* Before ranking: [DataSet](https://github.com/Mark1708/Mark1708/blob/master/assets/DataSet.csv)
* After ranking: [RankedDataSet](https://github.com/Mark1708/Mark1708/blob/master/assets/RankedDataSet.csv)