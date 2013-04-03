package graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
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
import org.jfree.util.ShapeUtilities;

public class GraphMaker extends ApplicationFrame {

	private static final long serialVersionUID = 1L;

	public GraphMaker(String iTitle, String iXTitle, String iYTitle, Collection<List<XYSeries>> iCollection, double iUpperXRange) {
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

		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();

		XYSeriesCollection aXYSeriesCollection = new XYSeriesCollection();
		int i = 0;
		for (List<XYSeries> aXYSeries : iCollection) {
			aXYSeriesCollection.addSeries(aXYSeries.get(0));
			renderer.setSeriesLinesVisible(i, true);
			// shows points for every read on the line
			renderer.setSeriesShapesVisible(i, true);
			renderer.setSeriesShape(i, ShapeUtilities.createDiamond(4));
			renderer.setSeriesStroke(i, new BasicStroke(2));
			renderer.setSeriesPaint(i, Color.GREEN);
			// if (i > 0)
			// renderer.setSeriesVisibleInLegend(i, false);
			i++;
		}
		i = 0;
		XYSeriesCollection aXYSeriesCollection1 = new XYSeriesCollection();
		for (List<XYSeries> aXYSeries : iCollection) {
			aXYSeriesCollection1.addSeries(aXYSeries.get(1));
			renderer1.setSeriesLinesVisible(i, true);
			// shows points for every read on the line
			renderer1.setSeriesShapesVisible(i, true);
			// if (i > 0)
			// renderer1.setSeriesVisibleInLegend(i, false);
			renderer1.setSeriesStroke(i, new BasicStroke(2));
			renderer1.setSeriesPaint(i, Color.RED);
			renderer1.setSeriesShape(i, ShapeUtilities.createDiamond(4));
			i++;
		}
		XYPlot plot = new XYPlot(null, rangeAxis, domainAxis, renderer);
		plot.setDataset(0, aXYSeriesCollection);
		plot.setRenderer(0, renderer);
		plot.setDataset(1, aXYSeriesCollection1);
		plot.setRenderer(1, renderer1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		// reverse order of bottom legend to put it in ascending order
		LegendItemCollection legendItemsOld = plot.getLegendItems();
		final LegendItemCollection legendItemsNew = new LegendItemCollection();
		System.out.println(legendItemsOld.getItemCount());

		for (i = 0; i < legendItemsOld.getItemCount(); i++) {
			LegendItem aLegendItem = legendItemsOld.get(i);
			// System.out.println(aLegendItem.getSeriesKey() + "," + aLegendItem.getSeriesIndex());
			if (aLegendItem.getSeriesKey().equals("Wait") || aLegendItem.getSeriesKey().equals("Run"))
				legendItemsNew.add(aLegendItem);
		}

		plot.setFixedLegendItems(legendItemsNew);

		JFreeChart chart = new JFreeChart(iTitle, plot);
		// LegendItemSource source = new LegendItemSource() {
		//
		// public LegendItemCollection getLegendItems() {
		// LegendItemCollection lic = new LegendItemCollection();
		// lic.addAll(legendItemsNew);
		// return lic;
		// }
		// };

		// chart.addLegend(new LegendTitle(source));
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(1800, 900));
		setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}
}
