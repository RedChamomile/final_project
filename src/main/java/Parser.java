import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Parser {
    public static void main(String[] args) throws IOException, NullPointerException {
        try (Connection connection=Connector.openConnection()) {
            if (connection != null) {
                Scanner scanner=new Scanner(System.in);
                System.out.print("Введите 1 для загрузки данных в локальную базу, или 0, чтобы работать с существующей: ");
                int mode=scanner.nextInt();
                if (mode == 1) {
                    recreationDB(connection);
                    for (int i=1; i < 202; i=i + 50) {
                        for (Element film : parser(i)) {
                            Constructor.constructor(film, connection);
                        }
                    }
                } else if (mode != 0) {
                    System.out.println("Я что-то нажала и всё исчезло!");
                    return;
                }
                System.out.println("Выберите номер задания: \n" +
                        "1. Отсортировать список фильмов по году выхода и вывести в консоль; \n" +
                        "2. Отсортировать список фильтов по метаскору и вывести в консоль; \n" +
                        "3. Отсортировать список фильмов по продолжительности. Вывести в консоль самый короткий фильм; \n" +
                        "4. Вывести в консоль список директоров с кол-вом фильмов в топ 250; \n" +
                        "5. Вывести в консоль список директоров со средней оценкой по всех их фильмам в топ 250, отсортировать по средней оценке их фильмов;\n" +
                        "6. Вывести в консоль список актеров с кол-вом фильмов в которых они играют из списка топ 250;\n" +
                        "7. Вывести в консоль список список актеров со средней оценкой по всех их фильмам в топ 250, отсортировать по средней оценке их фильмов;");
                int task=scanner.nextInt();
                System.out.print("Сохранить результат отбора в файл: (1 - да, 0 - нет): ");
                int out=scanner.nextInt();
                Sorter.sortMovies(task, connection, out);
                Connector.closeConnection();
                scanner.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    static void recreationDB(Connection connection) {
        try {
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader("src/main/resources/recreationDB.sql"));
            sr.runScript(reader);
        } catch (FileNotFoundException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    static Elements parser(int start) throws IOException {
        String url=Connector.getSource() + start;
        Document document=Jsoup.parse(new URL(url).openStream(), "utf8", url);
        Elements films=document.getElementsByAttributeValue("class", "lister-item-content");
        return films;
    }


}
