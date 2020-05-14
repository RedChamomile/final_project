import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Star {
    private String name;
    private int movieID;
    private int id;
    private static Statement stmt;
    private static ResultSet rs;

    public Star(String name, int movieID) {
        this.name = name;
        this.movieID = movieID;
    }

    public void insertStarToDB(Connection connection){
        String query="INSERT INTO imdb.`stars`(`name`) SELECT case when not exists(select * from stars where name like '"+name+"')  \n"+
                "then '"+name+"' else 'NULL' end where not exists(SELECT id  from stars where name  like '"+name+"');";
        try {
            if (!connection.isClosed()) {
                connection.createStatement().executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getID(Connection connection) {
        String query="select id from imdb.stars where name like '"+name+"';";
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

    public void addStarToMovie(int starID, int movieID, Connection connection){
        String query="INSERT INTO imdb.`stars_in_movie`(`star`, `movie`) VALUES ("+starID+", "+movieID+");";
        try {
            if (!connection.isClosed()) {
                connection.createStatement().executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
