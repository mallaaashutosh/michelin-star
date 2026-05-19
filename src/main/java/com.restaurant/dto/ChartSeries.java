/*
 * Simple pair of lists used to feed dashboard charts: category labels and matching numeric values.
 * Immutable after construction so servlets can pass chart data to JSP without accidental edits.
 * ChartJsonUtil turns instances of this class into JSON for Chart.js on the admin dashboard.
 */
package com.restaurant.dto; // DTO package for data passed to views

import java.util.Collections; // used by empty() factory
import java.util.List; // parallel lists for chart axes

/**
 * Labels and numeric values for charts (orders per day, status mix, etc.).
 */
public class ChartSeries { // one dataset for a single chart

    private final List<String> labels; // x-axis or slice names (days, statuses, dish names)
    private final List<Double> values; // y-axis or slice sizes aligned with labels

    public ChartSeries(List<String> labels, List<Double> values) { // build from query results
        this.labels = labels != null ? List.copyOf(labels) : List.of(); // defensive copy; empty if null
        this.values = values != null ? List.copyOf(values) : List.of(); // same for values
    }

    public static ChartSeries empty() { // placeholder when there is no data yet
        return new ChartSeries(Collections.emptyList(), Collections.emptyList()); // both lists empty
    }

    public List<String> getLabels() {
        return labels; // chart category names
    }

    public List<Double> getValues() {
        return values; // numbers matching each label
    }
}
