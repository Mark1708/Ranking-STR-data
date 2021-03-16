# Ranking an array of STR data
![Java](https://img.shields.io/badge/-Java-0a0a0a?style=for-the-badge&logo=Java) ![Spark](https://img.shields.io/badge/-Apache&Spark-0a0a0a?style=for-the-badge&logo=Apache&Spark)
<br/>
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
![Exanple1](https://github.com/Mark1708/Ranking-STR-data/blob/master/assets/Exanple1.png?raw=true)
![Exanple2](https://github.com/Mark1708/Ranking-STR-data/blob/master/assets/Exanple2.png?raw=true)
![Exanple2](https://github.com/Mark1708/Ranking-STR-data/blob/master/assets/Exanple3.png?raw=true)

Example of Data:
---
* Before ranking: [DataSet](https://github.com/Mark1708/Ranking-STR-data/blob/master/assets/DataSet.csv)
* After ranking: [RankedDataSet](https://github.com/Mark1708/Ranking-STR-data/blob/master/assets/RankedData.csv)

Note: The first column should be named "Index", followed by the names of the loci.

![Readme Card](https://github-readme-stats.vercel.app/api/pin/?username=mark1708&repo=ranking-str-data&theme=chartreuse-dark&show_icons=true)

