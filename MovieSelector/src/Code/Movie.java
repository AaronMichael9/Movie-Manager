package Code;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Movie {
	
	String name;
	String series;
	List<String> tags;
	double rating;
	
	
	public Movie(String name,String series,double rating,String[] tagArray) {
		this.name = name;
		this.series = series;
		this.rating = rating;
		tags = new ArrayList<>();
		for(String s:tagArray) {
			tags.add(s.trim());
		}
	}
	
	
	
	public static class movieComparator implements Comparator<Movie>{

		int mode;
		
		public movieComparator(int mode) {
			this.mode = mode;
		}

		@Override
		public int compare(Movie o1, Movie o2) {
			if(mode==0) {
				if(o1.rating<o2.rating)
					return 1;
				if(o1.rating>o2.rating)
					return -1;
				return 0;
			}
			return 0;
		}
		
	}
}
