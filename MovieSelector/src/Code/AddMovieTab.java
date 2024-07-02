package Code;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AddMovieTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddMovieTab() {
		generateLayout();
		generateTop();
		generateFiller();
		generateMiddle();
		this.revalidate();
	}

	private void generateLayout() {
		this.setLayout(new BorderLayout());
	}

	private void generateTop() {
		String text = "\n\nInput New Movie";
		JTextPane label = new JTextPane();
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setText(text);
		label.setFont(new Font("Serif", Font.BOLD, 16));
		StyledDocument doc = label.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		label.setEditable(false);
		this.add(label, BorderLayout.NORTH);
	}

	private JTextField name = new JTextField();
	private JTextField series = new JTextField();
	private JTextField rating = new JTextField();
	private JTextArea message = new JTextArea();
	private JList<String> tags;
	
	private void generateMiddle() {
		int inputH = 100;
		int inputW = 200;
		Container container = new Container();
		Container container2 = new Container();
		this.add(container, BorderLayout.CENTER);
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(container2);
		container2.setLayout(new GridLayout(4, 2));
		container2.add(new JLabel("Name: "));
//		name.setMaximumSize(new Dimension(inputH, inputW));
		container2.add(name);
		container2.add(new JLabel("Series: "));
		container2.add(series);
//		series.setPreferredSize(new Dimension(inputH, inputW));
		container2.add(new JLabel("Rating: "));
		container2.add(rating);
//		rating.setPreferredSize(new Dimension(inputH, inputW));
		container2.setMaximumSize(new Dimension(inputW,inputH));
		tags = new JList<String>(ListUtilities.getTags().toArray(new String[] {}));
		tags.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scroll = new JScrollPane(tags);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scroll);
		JButton submit = new JButton("submit");
		container.add(submit);
		container.add(message);
		message.setEditable(false);
		message.setVisible(false);
		
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onPress();
			}});
	}
	private void onPress() {
			String iName = name.getText();
			String iSeries = series.getText();
			String iRating = rating.getText();
			List<String> iTags = tags.getSelectedValuesList();
			try {
				double nRating = Double.parseDouble(iRating);
				if(iName.length()>0) {
					ListUtilities.addMovie(iName,iSeries,nRating,iTags);
					message.setText(iName+ " successfully added");
					message.setVisible(true);
					return;
				}
			}
			catch(Exception e) {
				
			}
			message.setText("There was a problem adding "+iName);
			message.setVisible(true);
	}

	private void generateFiller() {
		int eastBuffer = 150;
		int westBuffer = 150;
		int height = 100;
		int southBuffer = 150;
		this.add(Box.createRigidArea(new Dimension(westBuffer, height)), BorderLayout.WEST);
		this.add(Box.createRigidArea(new Dimension(eastBuffer, height)), BorderLayout.EAST);
		this.add(Box.createRigidArea(new Dimension(200, southBuffer)), BorderLayout.SOUTH);
	}

	@Override
	public void close() {
	}

}
