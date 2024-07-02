package Code;
import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TagsTab extends Tab {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JRadioButton byTag;
	private boolean isTag;
	private Tab content;

	public TagsTab() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(generateTop());
		initializeContent();
	}

	private Container generateTop() {
		Container containerTop = new Container();
		containerTop.setLayout(new BoxLayout(containerTop, BoxLayout.X_AXIS));
		ButtonGroup g = new ButtonGroup();
		byTag = new JRadioButton();
		JRadioButton byMovie = new JRadioButton();
		g.add(byTag);
		g.add(byMovie);
		containerTop.add(byTag);
		containerTop.add(new JLabel("By Tag"));
		containerTop.add(byMovie);
		containerTop.add(new JLabel("By Movie"));
		return containerTop;
	}

	private void initializeContent() {
		byTag.setSelected(true);
		isTag = true;
		content = new byTagTab();
		this.add(content);
		byTag.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				swap();
			}
		});
	}

	private void swap() {
		if (byTag.isSelected() == isTag)
			return;
		if (isTag) {
			content.close();
			this.remove(content);
			isTag = false;
			content = new byMovieTab();
			this.add(content);
		} else {
			content.close();
			this.remove(content);
			isTag = true;
			content = new byTagTab();
			this.add(content);
		}
		this.revalidate();
	}

	@Override
	public void close() {
		content.close();
	}
}
