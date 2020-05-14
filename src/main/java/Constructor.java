import org.jsoup.nodes.Element;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Constructor {

    public static void constructor(Element film, Connection connection) throws UnsupportedEncodingException {
        ResultSet rs;

        String title = film.child(0).child(1).text().replace("'","`");

        int year = Integer.parseInt(film.select("span.lister-item-year").text()
                   .replaceAll("[^0-9]", ""));

        float rating = Float.parseFloat(film.select("strong").text());

        int metascore;
        try {
            metascore = Integer.parseInt(film.select("span.metascore").text());
        }
        catch (NumberFormatException e)
        {
            metascore = 0;
        }

        int runtime = Integer.parseInt(film.select("span.runtime").text()
                     .replaceAll("[^0-9]", ""));

        Movie movie= new Movie(title, year, metascore, rating, runtime);
        movie.insertMovieToDB(connection);
        int movieID = movie.getID(connection);

        Director director = new  Director(film.child(4).child(0).text().replace("'","`"), movieID);
        director.insertDirectorToDB(connection);
        int directorID = director.getID(connection);
        director.addDirectorToMovie(directorID, movieID, connection);

        List<String> stars = film.child(4).select("span.ghost").nextAll().eachText();
        Iterator<String> starIterator = stars.iterator();
        while(starIterator.hasNext()){
            Star star = new Star(starIterator.next().replace("'","`"), movieID);
            star.insertStarToDB(connection);
            int starID = star.getID(connection);
            star.addStarToMovie(starID, movieID, connection);
        }

        List<String> genres =  Arrays.asList(film.select("span.genre").text().split(","));
        Iterator<String> genreIterator = genres.iterator();
        while(genreIterator.hasNext()){
            Genre genre = new Genre(genreIterator.next().trim(), movieID);
            genre.insertGenreToDB(connection);
            int genreID = genre.getID(connection);
            genre.addGenreToMovie(genreID, movieID, connection);
        }
    }
}
