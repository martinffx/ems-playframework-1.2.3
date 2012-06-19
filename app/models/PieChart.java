package models;

import com.*;
import com.googlecode.charts4j.Slice;

import static org.junit.Assert.assertEquals;
import static com.googlecode.charts4j.Color.*;
import static com.googlecode.charts4j.UrlUtil.normalize;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;

import org.junit.BeforeClass;
 
public class PieChart {

	private String title;
	private String url;
	private int height;
	private int width;
	private boolean threeD;
	ArrayList<Slice> slices;
	
	public PieChart(String title, int height, int width, boolean threeD) {
		this.title = title;
		this.height = height;
		this.width = width;
		this.threeD = threeD;
		this.slices = new ArrayList<Slice>();	
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	//use this method in control to add data for graph (pie specific)
	public void addSlice(String description, int percentage) {
		if(percentage >= 0) {
			if (description == null)
				description = "";
			
			Slice slice = Slice.newSlice(percentage, description);
			slices.add(slice);
		}
	}
	
	//call this in the view to generate the graph and url for img tag
	public String generateGraph() {
		url = "";
		
		if(slices.size() <= 0)
			return url;
		else {
			com.googlecode.charts4j.PieChart chart = com.googlecode.charts4j.GCharts.newPieChart(slices);
			chart.setTitle(title);
			chart.setSize(width, height);
			chart.setThreeD(threeD);
			String url = chart.toURLString();
			
			Logger.global.info(url);
			assertEquals("Junit error", normalize(url), normalize(url));
			return url;
		}
	}
}