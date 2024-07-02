package Code;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JudgementGame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static enum State {CLEAR,WAITING,ANSWERED}
	static State state;
	
	static final double MARGIN_MINIMUM = 0.1;
	static final double MARGIN_MAXIMUM = 2;
	
	static JFrame frame;
	static JButton buttonL;
	static JButton buttonR;
	static JLabel output;

	static final int FRAME_HEIGHT = 300;
	static final int FRAME_WIDTH = 500;
	static final int PADDING = 10;
	
	static int correct;
	static int total;
	static String leftName;
	static double leftRating;
	static String rightName;
	static double rightRating;
	static Color buttonColor = Color.LIGHT_GRAY;

	public static void main(String[] args) {
		frame = new JFrame();
		frame.setTitle("Judgement Game");

		// JPanel panel = new JPanel();
		LayoutManager layout1 = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
		c.weightx = 1;
		c.weighty = 1;
		frame.setLayout(layout1);
		buttonL = new JButton();
		buttonR = new JButton();
		output = new JLabel();
		JButton next = new JButton("next");
		buttonL.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(false);
			}});
		buttonR.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selection(true);
			}
		});
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				onNext();
			}
		});
		
		
		c.ipady = 50;
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		frame.add(buttonL, c);

		c.ipady = 50;
		c.gridx = 1;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		frame.add(buttonR, c);

		c.ipady = 0;
		c.weighty = 0.5;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 1;
		output.setAlignmentX(CENTER_ALIGNMENT);
		frame.add(output, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 2;
		frame.add(next, c);

		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		startUp();
	}
	public static void startUp() {
		buttonL.setBackground(buttonColor);
		buttonR.setBackground(buttonColor);
		buttonL.setText("");
		buttonR.setText("");
		output.setText("Push Next to start");
		correct = 0;
		total = 0;
		state = State.CLEAR;
	}
	public static void selection(boolean right) {
		if(state!=State.WAITING)
			return;
		if(leftRating>rightRating) {
			buttonL.setBackground(Color.GREEN);
			buttonR.setBackground(Color.RED);
		}
		else {
			buttonL.setBackground(Color.RED);
			buttonR.setBackground(Color.GREEN);
		}
		if(right==(rightRating>leftRating))
			correct+=1;
		total+=1;
		output.setText(String.format("score: %.2f (%d/%d)",correct/((double) total),correct,total));
		buttonL.setText("<html>"+leftName+"<br>"+leftRating+"</html>");
		buttonR.setText("<html>"+rightName+"<br>"+rightRating+"</html>");
		state = State.ANSWERED;
	}
	public static void onNext() {
		generateMovies();
		buttonL.setText(leftName);
		buttonR.setText(rightName);
		buttonL.setBackground(buttonColor);
		buttonR.setBackground(buttonColor);
		if(total>0)
			output.setText(String.format("score: %.2f (%d/%d)",correct/((double) total),correct,total));
		else
			output.setText("");
		state = State.WAITING;
	}
	public static void generateMovies() {
		ArrayList<Movie> movies = ListUtilities.getMovies();
		Movie left=null;
		Movie right=null;
		while (!check(left,right)){
			left = movies.get((int) (Math.random()*movies.size()));
			right = movies.get((int) (Math.random()*movies.size()));
		}
		leftName = left.name;
		leftRating = left.rating;
		rightName = right.name;
		rightRating = right.rating;
	}
	public static boolean check(Movie a,Movie b) {
		if(a==null || b==null)
			return false;
		double dif = Math.abs(a.rating-b.rating);
		dif = ((int) (dif*10))/10.0;
		if(dif==0)
			return false;
		if(dif<MARGIN_MINIMUM && MARGIN_MINIMUM>0)
			return false;
		if(dif>MARGIN_MAXIMUM && MARGIN_MAXIMUM>0)
			return false;
		return true;
	}
}
