package Code;
import java.awt.AWTException;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class byTagTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList<String> left;
	private JList<String> right;
	ArrayList<String> movieNames;
	ArrayList<String> tags = ListUtilities.getTags();
	TextField tagInput;
	Robot robot;

	public byTagTab() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		generateLeft();
		generateRight();
		startRobot();
	}

	private void getMovieNames() {
		ArrayList<Movie> movies = ListUtilities.getMovies();
		movieNames = new ArrayList<>();
		for (Movie movie : movies)
			movieNames.add(movie.name);
	}

	private void generateLeft() {
		int width = 200;
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		left = new JList<String>(tags.toArray(new String[] {}));
		left.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(left);
		scroll.setMinimumSize(new Dimension(width,300));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scroll);
		container.add(generateTagInput());
		this.add(container);
		left.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				rewriteRight(left.getSelectedValue());
			}
		});
	}

	private Container generateTagInput() {
		Container container = new Container();
		container.setLayout(new BoxLayout(container,BoxLayout.Y_AXIS));
		Container container2 = new Container();
		container2.setLayout(new BoxLayout(container2, BoxLayout.X_AXIS));
		container2.add(new JLabel("new tag: "));
		tagInput = new TextField();
		tagInput.setMaximumSize(new Dimension(125, 25));
		container2.add(tagInput);
		JButton button = new JButton("add");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addTag();
			}
		});
		container.add(container2);
		container.add(button);
		return container;
	}

	private void addTag() {
		String newTag = tagInput.getText();
		tags.add(0, newTag);
		left.setListData(tags.toArray(new String[] {}));
	}

	JCheckBox box;
	private void generateRight() {
		int bufferSize1 = 70;
		int bufferSize2 = 50;
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		getMovieNames();
		right = new JList<String>(movieNames.toArray(new String[] {}));
		JScrollPane scroll = new JScrollPane(right);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scroll);
		JButton update = new JButton("update");
		Container containerPadded = new Container();
		containerPadded.setLayout(new BoxLayout(containerPadded, BoxLayout.X_AXIS));
		Container containerc = new Container();
		containerc.setLayout(new BoxLayout(containerc,BoxLayout.X_AXIS));
		box = new JCheckBox();
		box.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ros();
			}
		});
		JLabel ctrl = new JLabel("CTRL");
		containerc.add(box);
		containerc.add(ctrl);
		containerc.setMaximumSize(new Dimension(20,10));
		containerPadded.add(containerc);
		containerPadded.add(ctrl);
		containerPadded.add(Box.createRigidArea(new Dimension(bufferSize1, 25)));
		containerPadded.add(update);
		containerPadded.add(Box.createRigidArea(new Dimension(bufferSize2, 25)));
		containerPadded.setMaximumSize(new Dimension(300,10));
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateByTag(left.getSelectedValue(), right.getSelectedValuesList());
			}
		});

		container.add(containerPadded);
		this.add(container);
	}

	// dependent on right JList using same movie order as ListUtilities.getMovies
	private void rewriteRight(String tag) {
		right.clearSelection();
		if (tag == null) {
			return;
		}
		ArrayList<Movie> movies = ListUtilities.getMovies();
		for (int i = 0; i < movies.size(); i++)
			if (movies.get(i).tags.contains(tag))
				right.addSelectionInterval(i, i);
	}

	private void updateByTag(String tag, List<String> names) {
		if(tag==null) {
			System.out.println("ERROR: No tag selected");
			return;}
		ArrayList<Movie> movies = ListUtilities.getMovies();
		for (Movie movie : movies) {
			if (names.contains(movie.name) && !movie.tags.contains(tag))
				movie.tags.add(tag);
			else if (!names.contains(movie.name))
				movie.tags.remove(tag);
		}
		ListUtilities.writeMovies(movies);
	}
	private void ros() {
		if(box.isSelected())
			robot.keyPress(KeyEvent.VK_CONTROL);
		else
			robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	private void startRobot() {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			System.out.println("problem starting robot");
		}
	}

	@Override
	public void close() {
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

}
