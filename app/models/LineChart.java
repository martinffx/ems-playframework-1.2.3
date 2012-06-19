package models;

import com.*;
import com.googlecode.charts4j.*;

import static org.junit.Assert.assertEquals;
import static com.googlecode.charts4j.UrlUtil.normalize;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import org.junit.BeforeClass;
 
public class LineChart {

	private String title;
	private String url;
	private int height;
	private int width;
	private double minYValue;
	private double maxYValue;
	ArrayList<Line> lines;
	
	ArrayList<AxisLabels> yAxis;
	ArrayList<AxisLabels> xAxis;
	
	Random numGen;
	
	public LineChart(String title, int height, int width) {
		this.title = title;
		this.width = width;
		this.height = height;
		lines = new ArrayList<Line>();

		this.minYValue = 0;
		this.maxYValue = 0;
		
		this.yAxis = new ArrayList<AxisLabels>();
		this.xAxis = new ArrayList<AxisLabels>();
		
		numGen = new Random();
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void addLine(String description, ArrayList<Double> values) {
		Color lineColor = Color.newColor(getRandomRGB());
	
		Line line = Plots.newLine(DataUtil.scale(values), lineColor, description);
		Iterator<Double> iteratorItems = values.iterator();
		
		line.addShapeMarkers(com.googlecode.charts4j.Shape.CIRCLE, Color.BLACK, 8);
		
		line.setFillAreaColor(lineColor);
		
		//get max value from each line's values.
		while(iteratorItems.hasNext()) {
			double valueObject = iteratorItems.next();
			
			if(valueObject > maxYValue)
				maxYValue = valueObject;
		}
		
		lines.add(line);
	}
	
	//call this in the view to generate the graph and url for img tag
	public String generateGraph() {
		url = "";
		
		if(lines.size() < 0)
			return url;
		else {
			com.googlecode.charts4j.LineChart chart = com.googlecode.charts4j.GCharts.newLineChart(lines);
			Iterator<AxisLabels> iteratorItems = xAxis.iterator();
			
			//add xaxis labels to graph
			while(iteratorItems.hasNext()) {
				AxisLabels axisObject = iteratorItems.next();
				
				chart.addXAxisLabels(axisObject);
			}
			
			chart.addYAxisLabels(AxisLabelsFactory.newNumericRangeAxisLabels(minYValue, maxYValue, (maxYValue/10)));
			
			chart.setTitle(title);
			chart.setSize(width, height);
			
			String url = chart.toURLString();
			
			Logger.global.info(url);
			assertEquals("Junit error", normalize(url), normalize(url));
			return url;
		}
	}
	
	public void addXAxisLabelsNumeric(double min, double max, double interval) {
		xAxis.add(AxisLabelsFactory.newNumericRangeAxisLabels(min, max, interval));
	}
	
	public void addXAxisLabel(String description, double position) {
		xAxis.add(AxisLabelsFactory.newAxisLabels(description, position));
	}
	
	 String getRandomRGB() {
      String rgb = Integer.toHexString(new java.awt.Color(numGen.nextInt(256), numGen.nextInt(256), numGen.nextInt(256)).getRGB());
      return rgb.substring(2, rgb.length());
   }
}