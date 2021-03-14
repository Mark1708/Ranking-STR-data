import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import scala.collection.immutable.List;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.apache.spark.sql.functions.col;
import static java.lang.Math.pow;

public class Main {

    private static String dataPath;
    private static String haplotypeIndex;
    private static int averageAge;
    private static final Double mu = 0.0026;


    public static void main(String[] args) {
        getInfo(args);
        SparkConf conf = new SparkConf().setMaster("local").setAppName("Ranking");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder().master("local").appName("App").getOrCreate();

        Dataset<Row> data = spark.read().format("csv")
                .option("sep", ";")
                .option("inferSchema", "true")
                .option("header", "true")
                .load(dataPath);

        Dataset<Row> sortedData = getRankedDataset(data, spark, sc);

        java.util.List<Row> rows = sortedData.collectAsList();

        StringBuilder sb = new StringBuilder();
        sb.append(String.join(";", data.columns()) + "\n");
        for (int i = 0; i < rows.size(); i++) {
            List<Object> list = rows.get(i).toSeq().toList();
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < list.length(); j++) {
                if ((!list.apply(j).equals(null))) {
                    row.append(String.valueOf(list.apply(j)) + ";");
                } else {
                    row.append(" ;");
                }
            }
            sb.append(row.toString() + "\n");
        }

        File file = new File(dataPath.substring(0, dataPath.lastIndexOf("/")) + "/RankedData.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        printReport(data, sortedData);
    }

    private static Dataset<Row> getRankedDataset(Dataset<Row> data, SparkSession spark, JavaSparkContext sc) {
        ArrayList<String> base = new ArrayList<>();
        data.filter(col("Index").equalTo(haplotypeIndex))
                .collectAsList().get(0)
                .toSeq().toList().foreach(
                x -> (x == null) ? base.add(null) : base.add(String.valueOf(x))
        );

        ArrayList<Row> countedValuesList = new ArrayList<>();
        data.collectAsList().forEach(row -> {
            double sum = 0;
            int notNullLocusCount = 0;
            String index = "";
            List<Object> list = row.toSeq().toList();
            for (int i = 0; i < list.length(); i++) {
                if (i == 0) {
                    index = String.valueOf(list.apply(i));
                } else if (!list.apply(i).equals(null) && !base.get(i).equals(null)) {
                    sum += pow((Integer.parseInt(base.get(i)) - Integer.parseInt(String.valueOf(list.apply(i)))), 2) / 2;
                    notNullLocusCount++;
                }
            }
            double T = averageAge * ((sum / notNullLocusCount) / mu);
            double k = ((mu * T) / 2) * (1 + pow(Math.E, (-mu) * T));
            double factMutation = (k / 2) * (1 + pow(Math.E, k));
            countedValuesList.add(RowFactory.create(index, T, k, factMutation));
        });

        String[] schemaFields = "Index, TMRCA, Average number of mutation steps(k), Average number of actual mutations(𝜆)".split(", ");
        ArrayList<StructField> fields = new ArrayList<>();
        for (int i = 0; i < schemaFields.length; i++) {
            if (i == 0)
                fields.add(DataTypes.createStructField(schemaFields[i], DataTypes.StringType, true));
            else
                fields.add(DataTypes.createStructField(schemaFields[i], DataTypes.DoubleType, true));
        }

        Dataset<Row> countedValues = spark.createDataFrame(
                sc.parallelize(countedValuesList),
                DataTypes.createStructType(fields)
        );


        return data.join(countedValues, "Index")
                .sort("TMRCA");
    }

    private static void printReport(Dataset<Row> data, Dataset<Row> sortedData) {
        StringBuilder sb = new StringBuilder();
        sb.append("Size of processing data: " + data.columns().length + "×" + data.count() + "\n");
        sb.append("\n\n");
        sb.append("Data info: \n");
        sb.append(data.drop("Index").describe().showString(6, 0, false));
        sb.append("\n\n");
        sb.append("The haplotype relative to which the STR data will be ranked: \n");
        sb.append(data.filter(col("Index").equalTo(haplotypeIndex)).showString(1, 0, false));
        sb.append("\n\n");
        sb.append("Ranked data: \n");
        sb.append(sortedData.showString(10, 0, false));
        sb.append("\n\n");

        System.out.println(sb.toString());
    }

    private static void getInfo(String[] args) {
        if (args.length == 0) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Path to csv file: ");
                dataPath = scanner.nextLine();
                System.out.print("Index of haplotype: ");
                haplotypeIndex = scanner.nextLine();
                System.out.print("Average age: ");
                averageAge = scanner.nextInt();
            }
        } else {
            CommandLineArgs parameters = new CommandLineArgs();
            JCommander commander = JCommander.newBuilder()
                    .addObject(parameters)
                    .build();
            try {
                commander.parse(args);
            } catch (ParameterException e) {
                noArgsExit(commander);
            }
            if (parameters.isHelp()) {
                commander.usage();
                System.exit(-1);
            } else {
                dataPath = parameters.getDataPath();
                haplotypeIndex = parameters.getHaplotypeIndex();
                averageAge = parameters.getAverageAge();
            }
        }
    }

    private static void noArgsExit(JCommander commander) {
        System.err.println("No required parameter!");
        commander.usage();
        System.exit(-1);
    }

}
