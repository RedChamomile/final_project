import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;


public class Tests {
    private static Connection connection=Connector.openConnection();
    private static Statement stmt;
    private static ResultSet rs;

    @Test
    public void conectionIsOpen(){
        try {
            Assert.assertTrue(!connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void existsDB(){
        String baseName ="imdb";
        String dbName=null;
        String query="show databases like '"+baseName+"';";
        try {
            if (!connection.isClosed()) {
                stmt = connection.createStatement();
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    dbName = rs.getString(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(baseName.equals(dbName));
    }

    @Test
    public void moviesNotEmpty() throws UnsupportedEncodingException {
        Movie movie = new Movie("Втеча з Шоушенка");
        int id=0;
        id = movie.getID(connection);
        Assert.assertTrue(id!=0);
    }

    @Test
    public void sourceIsReachable() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(Connector.getSource()).openConnection();
        connection.setRequestMethod("HEAD");
        int responseCode = connection.getResponseCode();
        Assert.assertTrue(responseCode == 200);
    }
}
