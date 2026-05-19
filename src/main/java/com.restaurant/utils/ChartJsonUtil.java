/*
 * Builds JSON strings for dashboard charts without pulling in a JSON library.
 * Takes a ChartSeries (labels + values) and outputs something Chart.js can read in a JSP script tag.
 * Also escapes special characters so dish names and categories don't break the JSON.
 */
package com.restaurant.utils; // utils package for chart helpers

import com.restaurant.dto.ChartSeries; // DTO holding parallel label and value lists

/**
 * Embeds chart data as JSON in JSP script blocks (no extra dependencies).
 */
public final class ChartJsonUtil { // utility class; cannot be subclassed

    private ChartJsonUtil() { // private constructor blocks instantiation
    }

    public static String toJsonObject(ChartSeries series) { // convert one chart series to a JSON object string
        if (series == null || series.getLabels().isEmpty()) { // nothing to chart — return empty structure
            return "{\"labels\":[],\"values\":[]}"; // safe default for empty data
        }
        StringBuilder sb = new StringBuilder(); // build JSON piece by piece
        sb.append("{\"labels\":["); // start labels array in JSON
        for (int i = 0; i < series.getLabels().size(); i++) { // walk each label
            if (i > 0) { // not the first label
                sb.append(','); // comma between JSON array elements
            }
            sb.append('"').append(escapeJson(series.getLabels().get(i))).append('"'); // quoted, escaped label
        }
        sb.append("],\"values\":["); // close labels, open values array
        for (int i = 0; i < series.getValues().size(); i++) { // walk each numeric value
            if (i > 0) { // not the first value
                sb.append(','); // comma between numbers
            }
            double v = series.getValues().get(i); // current data point
            if (Double.isNaN(v) || Double.isInfinite(v)) { // bad floats break JSON parsers
                sb.append('0'); // substitute zero so the chart still renders
            } else { // normal numeric value
                sb.append(v); // append the number as-is
            }
        }
        sb.append("]}"); // close values array and root object
        return sb.toString(); // finished JSON ready for the JSP
    }

    private static String escapeJson(String s) { // make a string safe inside JSON double quotes
        if (s == null) { // null label treated as empty
            return ""; // empty string is valid in JSON
        }
        StringBuilder out = new StringBuilder(); // escaped output
        for (int i = 0; i < s.length(); i++) { // character by character
            char c = s.charAt(i); // current char to check
            switch (c) { // handle characters that need escaping
                case '\\' -> out.append("\\\\"); // escape backslash
                case '"' -> out.append("\\\""); // escape quote
                case '\n' -> out.append("\\n"); // escape newline
                case '\r' -> out.append("\\r"); // escape carriage return
                case '\t' -> out.append("\\t"); // escape tab
                default -> out.append(c); // normal character — copy as-is
            }
        }
        return out.toString(); // escaped string for JSON
    }
}
