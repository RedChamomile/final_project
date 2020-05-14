import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Movie {
    private String title;
    private float rating;
    private int metascore;
    private int year;
    private int runtime;
    private int id;
    private static Statement stmt;
    private static ResultSet rs;

    public Movie(String title) throws UnsupportedEncodingException {
        this.title=new String(title.getBytes(), "UTF-8");
    }

    public Movie(String title, int year, int metascore, float rating, int runtime) throws UnsupportedEncodingException {
        this.title=new String(title.getBytes(), "UTF-8");
        this.year=year;
        this.metascore=metascore;
        this.rating=rating;
        this.runtime=runtime;
    }

    public void insertMovieToDB(Connection connection){
        String query="INSERT INTO imdb.`movies`(`name`, `rating`, `metascore`, `year`, `runtime`) VALUES ('"+title+"',"+rating+","+metascore+","+year+","+runtime+");";
        try {
            if (!connection.isClosed()) {
                connection.createStatement().executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getID(Connection connection){
        String query="select id from imdb.movies where name like '"+title+"';";
        try {
            if (!connection.isClosed()) {
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);

                while (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


}
