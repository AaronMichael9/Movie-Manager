package Code;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

public class MovieManager extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String[] tabs = { "Movies", "Filter", "New Movie", "Tags" };
	private Tab tab = null;
	private String tabName = "";
	private Filter filter = new Filter();

	public static void main(String[] args) {
		JFrame window = new MovieManager();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public MovieManager() {
		generateLayout();
		generateTopBar();
		this.setVisible(true);
		transitionTab(tabs[0]);
	}

	private void generateLayout() {
		BorderLayout mainLayout = new BorderLayout();
		this.setLayout(mainLayout);
		this.setVisible(true);
		this.setTitle("Movie Manager 2.0");
		try {
			Image image = ImageIO.read(new File("icon.jpg"));
			this.setIconImage(image);
		} catch (Exception e) {
		}
		this.setSize(500, 600);
		this.setLocation(500, 250);
	}

	private void generateTopBar() {
		Container top = new Container();
		this.add(top, BorderLayout.NORTH);
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));
		for (String s : tabs) {
			Button button = new Button(s);
			button.setFocusable(false);
			button.setBackground(Color.ORANGE);
			button.setFont(new Font("Serif", Font.BOLD, 16));
			top.add(button);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					transitionTab(s);
				}
			});

		}
	}

	private void transitionTab(String name) {
		if (name.equals(tabName))
			return;
		if (tab != null) {
			tab.close();
			this.remove(tab);
		}
		Tab newTab = null;
		switch (name) {
		case "Movies":
			this.requestFocus();
			newTab = new MovieTab(filter, this);
			break;
		case "Filter":
			newTab = new FilterTab(filter);
			break;
		case "New Movie":
			newTab = new AddMovieTab();
			break;
		case "Tags":
			newTab = new TagsTab();
			break;
		default:
			return;
		}
		this.add(newTab, BorderLayout.CENTER);
		tab = newTab;
		tabName = name;
		this.revalidate();
	}
}
