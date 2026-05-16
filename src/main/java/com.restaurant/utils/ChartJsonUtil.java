package com.restaurant.utils;

import com.restaurant.dto.ChartSeries;

/**
 * Embeds chart data as JSON in JSP script blocks (no extra dependencies).
 */
public final class ChartJsonUtil {

    private ChartJsonUtil() {
    }

    public static String toJsonObject(ChartSeries series) {
        if (series == null || series.getLabels().isEmpty()) {
            return "{\"labels\":[],\"values\":[]}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{\"labels\":[");
        for (int i = 0; i < series.getLabels().size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append('"').append(escapeJson(series.getLabels().get(i))).append('"');
        }
        sb.append("],\"values\":[");
        for (int i = 0; i < series.getValues().size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            double v = series.getValues().get(i);
            if (Double.isNaN(v) || Double.isInfinite(v)) {
                sb.append('0');
            } else {
                sb.append(v);
            }
        }
        sb.append("]}");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\\' -> out.append("\\\\");
                case '"' -> out.append("\\\"");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> out.append(c);
            }
        }
        return out.toString();
    }
}
