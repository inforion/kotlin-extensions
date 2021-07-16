package ru.inforion.lab403.common.spark;

import org.apache.spark.SparkContext;

import java.util.List;

public class SparkScalaAbstracter {
    public static List<String> getExecutors(SparkContext sc) {
        return scala.collection.JavaConverters.seqAsJavaList(sc.getExecutorIds());
    }
}
