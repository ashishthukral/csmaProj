package graph;

import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GraphMaker extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public GraphMaker(String iTitle, String iXTitle, String iYTitle, List<XYSeries> iXYSeries, double iUpperXRange) {
		super(iTitle);
		// X-axis
		NumberAxis rangeAxis = new NumberAxis(iXTitle);
		rangeAxis.setRange(-1, iUpperXRange);

		// Y-axis
		NumberAxis domainAxis = new NumberAxis(iYTitle);
		// domainAxis.setRange(0, 2000);
		domainAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		NumberFormat format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(2); // etc.
		// {0} {1} {2} = Thread ID - x axis - y axis
		XYItemLabelGenerator generator = new StandardXYItemLabelGenerator("{1}", format, format);
		renderer.setBaseItemLabelGenerator(generator);
		renderer.setBaseItemLabelsVisible(true);
		renderer.setSeriesLinesVisible(0, true);
		// shows points for every read on the line
		renderer.setSeriesShapesVisible(0, true);

		XYSeriesCollection aXYSeriesCollection = new XYSeriesCollection();

		for (XYSeries aXYSeries : iXYSeries) {
			// System.out.println(aXYSeries.getKey());
			aXYSeriesCollection.addSeries(aXYSeries);
		}
		XYPlot plot = new XYPlot(aXYSeriesCollection, rangeAxis, domainAxis, renderer);
		plot.setOrientation(PlotOrientation.VERTICAL);
		JFreeChart chart = new JFreeChart(iTitle, plot);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(800, 600));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

}