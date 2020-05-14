import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sorter {
    public static void sortMovies(int task, Connection connection) {
        String query;
        Statement stmt;
        ResultSet rs;
        String diveder = "+-------------------------------------------------------------------------------------+";
        System.out.println(diveder);
        switch(task) {
            case 1:
                query="SELECT name, year FROM imdb.movies order by year;";
                System.out.printf( "%s %-65s %s %15s %s %n", "|", "title", "|", "year", "|");
                break;
            case 2:
                query="SELECT name, metascore FROM imdb.movies order by metascore;";
                System.out.printf("%s %-65s %s %15s %s %n", "|", "title", "|", "metascore","|");
                break;
            case 3:
                query="SELECT name, runtime FROM imdb.movies order by runtime LIMIT 1;";
                System.out.printf("%s %-65s %s %15s %s %n", "|", "title", "|", "runtime", "|");
                break;
            case 4:
                query="SELECT d.name, count(m.id) FROM imdb.director d \n" +
                        "inner join director_in_movie dm on d.id=dm.director  \n" +
                        "inner join movies m on m.id=dm.movie \n" +
                        "GROUP by director \n" +
                        "ORDER by COUNT(m.id) desc;";
                System.out.printf("%s %-65s %s %15s %s %n", "|", "director", "|", "number of films", "|");
                break;
            case 5:
                query="SELECT d.name, round(AVG(m.rating),1) FROM imdb.director d \n" +
                        "inner join director_in_movie dm on d.id=dm.director \n" +
                        "inner join movies m on m.id=dm.movie \n" +
                        "GROUP by director \n" +
                        "ORDER by AVG(m.rating) desc;";
                System.out.printf("%s %-65s %s %15s %s %n", "|", "director", "|", "average rating", "|");
                break;
            case 6:
                query="SELECT s.name, count(m.id) FROM imdb.stars s \n" +
                        "inner  join stars_in_movie sm on s.id=sm.star \n" +
                        "inner join movies m on m.id=sm.movie \n" +
                        "GROUP by s.name  \n" +
                        "ORDER by count(m.id) desc;";
                System.out.printf("%s %-65s %s %15s %s %n", "|", "actor", "|", "number of films", "|");
                break;
            case 7:
                query="SELECT s.name, round(AVG(m.rating),1) FROM imdb.stars s\n" +
                        "inner  join stars_in_movie sm on s.id=sm.star\n" +
                        "inner join movies m on m.id=sm.movie\n" +
                        "GROUP by s.name\n" +
                        "ORDER by AVG(m.rating) desc;";
                System.out.printf("%s %-65s %s %15s %s %n", "|", "actor", "|", "average rating", "|");
                break;
            default:
                query="SELECT name, id FROM imdb.movies;";
                System.out.printf("%s %-65s %s %15s %s %n", "|",  "movie", "|",  "position in rating", "|");
                break;
        }

        try {
            if (!connection.isClosed()) {
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                System.out.println(diveder);
                while (rs.next()) {
                    System.out.printf( "%s %-65s %s %15s %s %n", "|", rs.getString(1), "|", rs.getString(2), "|");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(diveder);
    }
}
