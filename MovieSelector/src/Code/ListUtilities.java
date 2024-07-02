package Code;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtilities {

	private static final String FILEPATH = "movies.csv";
	private static final double X = 2;

	public static void addMovie(String name, String series, double rating,List<String> tags) {
		if(tags==null)
			tags = new ArrayList<>();
		ArrayList<Movie> movies = getMovies();
		movies.add(new Movie(name, series, rating, tags.toArray(new String[] {})));
		movies.sort(new Movie.movieComparator(0));
		writeMovies(movies);
	}

	public static ArrayList<Movie> getMovies() {
		ArrayList<Movie> movies = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(FILEPATH));
			String line = reader.readLine();
			while (line != null) {
				String[] alpha = line.split("\\|");
				String[] info = alpha[0].split(";");
				String[] tags = {};
				if (alpha.length > 1)
					tags = alpha[1].split(";");
				if (info.length == 3) {
					String name = info[0];
					String series = info[1];
					double rating = Double.parseDouble(info[2]);
					movies.add(new Movie(name, series, rating, tags));
				}
				line = reader.readLine();
			}
			reader.close();
			return movies;
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		return movies;
	}

	public static ArrayList<Movie> getMoviesFiltered(Filter filter) {
		ArrayList<Movie> movies = getMovies();
		for (int i = movies.size() - 1; i >= 0; i--)
			if (!checkFilter(movies.get(i), filter))
				movies.remove(i);
		return movies;
	}

	public static ArrayList<String> getSeries() {
		ArrayList<Movie> movies = getMovies();
		ArrayList<String> result = new ArrayList<>();
		for (Movie movie : movies)
			if (!movie.series.equals("") && !result.contains(movie.series))
				result.add(movie.series);
		result.sort(new StringComparator());
		return result;
	}

	public static ArrayList<String> getTags() {
		ArrayList<Movie> movies = getMovies();
		ArrayList<String> result = new ArrayList<>();
		for (Movie movie : movies)
			if (movie.tags != null)
				for (String tag : movie.tags)
					if (!result.contains(tag))
						result.add(tag);
		result.sort(new StringComparator());
		return result;
	}

	private static boolean checkFilter(Movie movie, Filter filter) {
		if (filter.term != null && !movie.name.toLowerCase().contains(filter.term.toLowerCase()))
			return false;
		if (filter.max_rating != null && movie.rating > filter.max_rating)
			return false;
		if (filter.min_rating != null && movie.rating < filter.min_rating)
			return false;
		if (filter.series.size() > 0 && !filter.series.contains(movie.series) && !(filter.series.size()==1 && filter.series.get(0).equals(" ")))
			return false;
		if(!checkTags(movie,filter.tags,filter.tagUnion))
			return false;
		return true;
	}
	private static boolean checkTags(Movie movie,List<String> tags,boolean isUnion) {
		if(tags.size()==0 || (tags.size()==1 && tags.get(0).equals(" ")))
			return true;
		for(String tag:tags) {
			if(isUnion && movie.tags.contains(tag))
				return true;
			if(!isUnion && !movie.tags.contains(tag) && !tag.equals(" "))
				return false;
		}
		return !isUnion;
	}

	public static void sortMovies() {
		ArrayList<Movie> movies = getMovies();
		movies.sort(new Movie.movieComparator(0));
		writeMovies(movies);
	}

	public static void writeMovies(ArrayList<Movie> movies) {
		try {
			FileWriter writer = new FileWriter(FILEPATH);
			for (Movie movie : movies) {
				String line = movie.name + ";" + movie.series + ";" + movie.rating;
				if (movie.tags.size() > 0) {
					line += "|";
					for (String tag : movie.tags)
						line += tag + ";";
					line = line.substring(0, line.length() - 1);
				}
				line += "\n";
				writer.append(line);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateSeries(String old, String updated) {
		ArrayList<Movie> movies = getMovies();
		for (Movie movie : movies)
			if (movie.series.equals(old))
				movie.series = updated;
		writeMovies(movies);
	}

	public static int getMovieAmount() {
		return getMovies().size();
	}

	public static int getMovieAmountFiltered(Filter filter) {
		return getMoviesFiltered(filter).size();
	}

	public static ArrayList<Movie> selectMovies(int n, Filter filter,boolean weighted) {
		ArrayList<Movie> pos = getMoviesFiltered(filter);
		if (pos.size() == 0)
			return null;
		if (pos.size() <= n)
			return pos;
		Set<String> names = new HashSet<>();
		ArrayList<Movie> results = new ArrayList<>();
		while (results.size() < n) {
			Movie m;
			if(weighted)
				m = selectMovie(pos);
			else
				m = selectUnweightedMovie(pos);
			if (m == null)
				return null;
			if (!names.contains(m.name)) {
				results.add(m);
				names.add(m.name);
			}
		}
		return results;
	}

	// returns null if no movies match the filter
	public static Movie selectMovie(ArrayList<Movie> movies) {
		double total = 0;
		for (Movie m : movies)
			total += Math.pow(X, m.rating);
		double choice = total * Math.random();
		for (Movie movie : movies) {
			if (Math.pow(X, movie.rating) >= choice)
				return movie;
			choice -= Math.pow(X, movie.rating);
		}
		return null;
	}
	public static Movie selectUnweightedMovie(ArrayList<Movie> movies) {
		if(movies.size()==0)
			return null;
		double r = Math.random();
		int n = (int) (r*movies.size());
		System.out.println(n);
		return movies.get(n);
	}

	public static class StringComparator implements Comparator<String> {

		@Override
		public int compare(String o1, String o2) {
			return o1.toLowerCase().compareTo(o2.toLowerCase());
		}

	}
}
