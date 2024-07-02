package Code;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

public class FilterTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Filter filter;
	JTextField name;
	JTextField min;
	JTextField max;
	JList<String> listSeries;
	JList<String> listTags;
	JRadioButton beta;

	public FilterTab(Filter filter) {
		this.filter = filter;
		this.setLayout(new BorderLayout());
		addBuffer();
		Container container = new Container();
		this.add(container, BorderLayout.CENTER);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(setupC2());
		container.add(setupS1());
		container.add(setupS2());
		Container container3 = setupC3();
		container.add(container3);
		initiateFilter();
	}
	private void addBuffer() {
		int eastBuffer = 120;
		int westBuffer = 120;
		int height = 100;
		int southBuffer = 50;
		this.add(Box.createRigidArea(new Dimension(westBuffer, height)), BorderLayout.WEST);
		this.add(Box.createRigidArea(new Dimension(eastBuffer, height)), BorderLayout.EAST);
		this.add(Box.createRigidArea(new Dimension(500, southBuffer)), BorderLayout.SOUTH);
	}
	private Container setupC2() {
		Container container2 = new Container();
		container2.setLayout(new GridLayout(3, 2));
		container2.add(new JLabel("Name: "));
		name = new JTextField();
		container2.add(name);
		container2.add(new JLabel("Min rating: "));
		min = new JTextField();
		container2.add(min);
		container2.add(new JLabel("Max rating: "));
		max = new JTextField();
		container2.add(max);
		return container2;
	}
	private JScrollPane setupS1() {
		ArrayList<String> series = ListUtilities.getSeries();
		series.add(0," ");
		listSeries = new JList<String>(series.toArray(new String[] {}));
		listSeries.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(listSeries);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return scroll;
	}
	private JScrollPane setupS2() {
		ArrayList<String> series2 = ListUtilities.getTags();
		series2.add(0," ");
		listTags = new JList<String>(series2.toArray(new String[] {}));
		listTags.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll2 = new JScrollPane(listTags);
		scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		return scroll2;
	}
	private Container setupC3() {
		Container container3 = new Container();
		container3.setLayout(new BoxLayout(container3,BoxLayout.X_AXIS));
		JRadioButton alpha = new JRadioButton();
		beta = new JRadioButton();
		ButtonGroup g = new ButtonGroup();
		g.add(alpha);
		g.add(beta);
		container3.add(alpha);
		container3.add(new JLabel("AND"));
		container3.add(beta);
		container3.add(new JLabel("OR"));
		beta.setSelected(true);
		return container3;
	}
	private void initiateFilter() {
		if(filter.term!=null)
			name.setText(filter.term);
		if(filter.min_rating!=null)
			min.setText(filter.min_rating+"");
		if(filter.max_rating!=null)
			max.setText(filter.max_rating+"");
		List<String> sa = ListUtilities.getSeries();
		sa.add(0," ");
		List<String> sb = ListUtilities.getTags();
		sb.add(0," ");
		for(String series:filter.series)
			listSeries.addSelectionInterval(sa.indexOf(series), sa.indexOf(series));
		for(String tag:filter.tags)
			listTags.addSelectionInterval(sb.indexOf(tag),sb.indexOf(tag));
	}
	
	
	

	@Override
	public void close() {
		filter.term = name.getText();
		try {
			if(min.getText().isEmpty())
				filter.min_rating = null;
			else
				filter.min_rating = Double.parseDouble(min.getText());
		}
		catch(Exception e) {}
		try {
			if(max.getText().isEmpty())
				filter.max_rating = null;
			else
				filter.max_rating = Double.parseDouble(max.getText());
		}
		catch(Exception e) {}
		filter.series =listSeries.getSelectedValuesList();
		filter.tags = listTags.getSelectedValuesList();
		filter.tagUnion = beta.isSelected();
	}

}
