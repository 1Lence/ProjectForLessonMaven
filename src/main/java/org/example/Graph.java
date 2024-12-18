package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Graph extends JFrame {
    private final int DEFAULT_PADDING = 15;

    public Graph(Map<String, Double> map, String header, String leftData, String footer) {
        init(map, header, leftData, footer);
    }

    private void init(Map<String, Double> map, String header, String leftData, String footer) {
        CategoryDataset dataset = createDataset(map, footer);
        JFreeChart chart = createChart(dataset, header, leftData);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING));
        chartPanel.setBackground(Color.WHITE);
        add(chartPanel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JFreeChart createChart(CategoryDataset dataset, String header, String leftData) {
        JFreeChart chart = ChartFactory.createBarChart(header, "", leftData, dataset);

        CategoryPlot plot = chart.getCategoryPlot();

        CategoryAxis axis_d = plot.getDomainAxis();
        axis_d.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
        return chart;
    }

    private CategoryDataset createDataset(Map<String, Double> map, String footer) {
        var dataset = new DefaultCategoryDataset();
        map.forEach((key, value) -> {
            dataset.setValue(value, footer, key);
        });
        return dataset;
    }
}