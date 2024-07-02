package Code;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class byMovieTab extends Tab {

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

	public byMovieTab() {
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		generateLeft();
		generateRight();
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
		getMovieNames();
		left = new JList<String>(movieNames.toArray(new String[] {}));
		left.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(left);
		scroll.setMinimumSize(new Dimension(width, 300));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scroll);
		this.add(container);
		left.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				rewriteRight(left.getSelectedIndex());
			}
		});
	}

	private Container generateTagInput() {
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
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
		int[] selected = right.getSelectedIndices();
		right.setListData(tags.toArray(new String[] {}));
		for (int i : selected)
			right.addSelectionInterval(i + 1, i + 1);
	}

	private void generateRight() {
//		int bufferSize1 = 70;
//		int bufferSize2 = 50;
		Container container = new Container();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(generateTagInput());
		getMovieNames();
		right = new JList<String>(tags.toArray(new String[] {}));
		JScrollPane scroll = new JScrollPane(right);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		container.add(scroll);
		JButton update = new JButton("update");
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateByMovie(left.getSelectedIndex(), right.getSelectedValuesList());
			}
		});
		container.add(update);
		this.add(container);
	}

	// dependent on right JList using same tag order as ListUtilities.getTags
	private void rewriteRight(int moviePosition) {
		right.clearSelection();
		Movie movie = ListUtilities.getMovies().get(moviePosition);
		for (int i = 0; i < tags.size(); i++) {
			if (movie.tags.contains(tags.get(i)))
				right.addSelectionInterval(i, i);
		}
	}

	private void updateByMovie(int moviePos, List<String> tags) {
		ArrayList<Movie> movies = ListUtilities.getMovies();
		movies.get(moviePos).tags = tags;
		ListUtilities.writeMovies(movies);
	}

	@Override
	public void close() {
	}
}
