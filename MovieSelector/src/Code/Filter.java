package Code;
import java.util.ArrayList;
import java.util.List;

public class Filter {
	public Double min_rating;
	public Double max_rating;
	public String term;
	public List<String> series;
	public List<String> tags;
	public boolean tagUnion;
	
	public Filter() {
		series = new ArrayList<>();
		tags = new ArrayList<>();
	}
}
