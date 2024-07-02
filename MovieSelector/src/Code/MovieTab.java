package Code;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MovieTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Filter filter;
	private JTextPane scroll_text;
	private JFrame focus;
	private KeyListener listener;
	
	public MovieTab(Filter filter,JFrame focus) {
		this.filter = filter;
		this.focus = focus;
		this.setLayout(new BorderLayout());
		int n = ListUtilities.getMovieAmount();
		ArrayList<Movie> movies = ListUtilities.getMoviesFiltered(filter);
		generateTop(movies.size(),n);
		generateMiddle(movies);
		generateBottom();
		generateListener();
	}
	private void generateTop(int m,int n) {
		String text = String.format("%d out of %d movies found", m, n);
		if (m == n) {
			text = String.format("%d movies found", n);
		}
		text = "\n\n"+text;
		JTextPane label = new JTextPane();
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setText(text);
		label.setFocusable(false);
		
		StyledDocument doc = label.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		label.setEditable(false);
		this.add(label,BorderLayout.NORTH);
	}
	private void generateMiddle(ArrayList<Movie> movies) {
		scroll_text = new JTextPane();
		scroll_text.setEditable(false);
		scroll_text.setFocusable(false);
		JScrollPane scroll = new JScrollPane(scroll_text);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.add(scroll,BorderLayout.CENTER);
		generateContent(movies);
	}
	
	private void generateContent(ArrayList<Movie> movies){
		String build = "";
		for (Movie movie : movies) {
			build += String.format("%.1f| %s\n", movie.rating, movie.name);
		}
		if(build.length()>0)
			build = build.substring(0, build.length() - 1);
		scroll_text.setText(build);
		this.revalidate();
	}
	private void generateBottom() {
		JTextPane label = new JTextPane();
		label.setFont(new Font("Serif",Font.PLAIN,12));
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setText("**press any number key to generate selection**");
		label.setFocusable(false);
		StyledDocument doc = label.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		label.setEditable(false);
		this.add(label,BorderLayout.SOUTH);	
	}
	private void generateListener() {
		listener = new InputListener();
		focus.addKeyListener(listener);
	}
	private void onPress(int n) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		if(n==0) 
			movies = ListUtilities.getMoviesFiltered(filter);
		else
			movies = ListUtilities.selectMovies(n, filter,true);
		generateContent(movies);
	}
	private void onShiftPress(int n) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		if(n==0) 
			movies = ListUtilities.getMoviesFiltered(filter);
		else
			movies = ListUtilities.selectMovies(n, filter,false);
		generateContent(movies);
	}
	
	private class InputListener implements KeyListener{

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent e) {
				String nums = "123456789";
				String snums = "!@#$%^&*(";
				char c = e.getKeyChar();
				if(nums.contains(c+""))
					onPress(nums.indexOf(c)+1);
				else if(snums.contains(c+""))
					onShiftPress(snums.indexOf(c)+1);
//				else
//					onPress(0);
		}
	}
	
	@Override
	public void close() {
		focus.removeKeyListener(listener);
	}
}
