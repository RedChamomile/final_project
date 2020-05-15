import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sorter {
    private static String diveder = "+-------------------------------------------------------------------------------------+\n";

    public static void sortMovies(int task, Connection connection, int out) {
        String query;
        Statement stmt;
        ResultSet rs;
        String column1;
        String column2;

        switch(task) {
            case 1:
                query="SELECT name, year FROM imdb.movies order by year;";
                column1 ="title";
                column2 = "year";
                break;
            case 2:
                query="SELECT name, metascore FROM imdb.movies order by metascore;";
                column1 ="title";
                column2 = "metascore";
                break;
            case 3:
                query="SELECT name, runtime FROM imdb.movies order by runtime LIMIT 1;";
                column1 ="title";
                column2 = "runtime";
                break;
            case 4:
                query="SELECT d.name, count(m.id) FROM imdb.director d \n" +
                        "inner join director_in_movie dm on d.id=dm.director  \n" +
                        "inner join movies m on m.id=dm.movie \n" +
                        "GROUP by director \n" +
                        "ORDER by COUNT(m.id) desc;";
                column1 ="director";
                column2 = "number of films";
                break;
            case 5:
                query="SELECT d.name, round(AVG(m.rating),1) FROM imdb.director d \n" +
                        "inner join director_in_movie dm on d.id=dm.director \n" +
                        "inner join movies m on m.id=dm.movie \n" +
                        "GROUP by director \n" +
                        "ORDER by AVG(m.rating) desc;";
                column1 ="director";
                column2 = "average rating";
                break;
            case 6:
                query="SELECT s.name, count(m.id) FROM imdb.stars s \n" +
                        "inner  join stars_in_movie sm on s.id=sm.star \n" +
                        "inner join movies m on m.id=sm.movie \n" +
                        "GROUP by s.name  \n" +
                        "ORDER by count(m.id) desc;";
                column1 ="actor";
                column2 = "number of films";
                break;
            case 7:
                query="SELECT s.name, round(AVG(m.rating),1) FROM imdb.stars s\n" +
                        "inner  join stars_in_movie sm on s.id=sm.star\n" +
                        "inner join movies m on m.id=sm.movie\n" +
                        "GROUP by s.name\n" +
                        "ORDER by AVG(m.rating) desc;";
                column1 ="actor";
                column2 = "average rating";
                break;
            default:
                query="SELECT name, id FROM imdb.movies;";
                column1 ="movie";
                column2 = "position in rating";
                break;
        }

        try {
            if (!connection.isClosed()) {
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                if(out==1){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    Date date = new Date();
                    String pathname = "resultset_"+formatter.format(date)+".txt";
                    File file = new File(pathname);
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        FileWriter writer = new FileWriter(pathname, true);
                        BufferedWriter bufferWriter = new BufferedWriter(writer);
                        bufferWriter.write(diveder);
                        bufferWriter.write(output(column1, column2));
                        bufferWriter.newLine();
                        bufferWriter.write(diveder);
                        while (rs.next()) {
                            bufferWriter.write(output(rs.getString(1),  rs.getString(2)));
                            bufferWriter.newLine();
                        }
                        bufferWriter.write(diveder);
                        bufferWriter.close();
                    }
                    catch (IOException e) {
                        System.out.println(e);
                    }
                }
                else {
                    System.out.printf(diveder);
                    System.out.println(output(column1, column2));
                    System.out.printf(diveder);
                    while (rs.next()) {
                        System.out.println(output(rs.getString(1), rs.getString(2)));
                    }
                    System.out.printf(diveder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static String output(String column1, String columl2){
        String output = String.format("%s %-65s %s %15s %s", "|", column1, "|", columl2, "|");
        return output;
    }

}
