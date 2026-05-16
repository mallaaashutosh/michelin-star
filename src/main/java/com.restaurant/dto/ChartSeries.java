package com.restaurant.dto;

import java.util.Collections;
import java.util.List;

/**
 * Labels and numeric values for charts (orders per day, status mix, etc.).
 */
public class ChartSeries {

    private final List<String> labels;
    private final List<Double> values;

    public ChartSeries(List<String> labels, List<Double> values) {
        this.labels = labels != null ? List.copyOf(labels) : List.of();
        this.values = values != null ? List.copyOf(values) : List.of();
    }

    public static ChartSeries empty() {
        return new ChartSeries(Collections.emptyList(), Collections.emptyList());
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Double> getValues() {
        return values;
    }
}
