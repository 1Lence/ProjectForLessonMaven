package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class DataBase {
    static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static Connection connection;
    public static Statement statmt;
    private static final String URL = "jdbc:sqlite:test.db";

    public static void CreateTable() throws SQLException {
        connection = DriverManager.getConnection(URL);
        statmt = connection.createStatement();

        statmt.execute("DROP TABLE IF EXISTS EarthShake;");
        statmt.execute("CREATE TABLE IF NOT EXISTS EarthShake" +
                "(Id VARCHAR(11) not null primary key," +
                "DepthM INT," +
                "MagnitudeType VARCHAR(5)," +
                "Magnitude DOUBLE," +
                "State TEXT," +
                "Time DATETIME" +
                ")");
        CsvToFile();
    }

    private static void CsvToFile() {
        boolean isFirstLine = true;

        String csvFile = "Землетрясения.csv";
        String sql = "INSERT INTO EarthShake(Id, DepthM, MagnitudeType, Magnitude, State, Time) VALUES(?,?,?,?,?,?)";

        SimpleDateFormat csvDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        SimpleDateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (CSVReader reader = new CSVReader(new FileReader(csvFile));
             var conn = DriverManager.getConnection(URL);
             var pstmt = conn.prepareStatement(sql)) {

            //Необходимо для ускорения работы метода
            // Отключение автоматической фиксации
            conn.setAutoCommit(false);

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String id = nextLine[0];
                int depthM = Integer.parseInt(nextLine[1]);
                String magnitudeType = nextLine[2];
                double magnitude = Double.parseDouble(nextLine[3]);
                String state = nextLine[4];
                Date date = nextLine[5].contains("Z") ? csvDateFormat.parse(nextLine[5]) : sqliteDateFormat.parse(nextLine[5]);

                pstmt.setString(1, id);
                pstmt.setInt(2, depthM);
                pstmt.setString(3, magnitudeType);
                pstmt.setDouble(4, magnitude);
                pstmt.setString(5, state);
                pstmt.setString(6, sqliteDateFormat.format(date));

                pstmt.addBatch();

                if (reader.getLinesRead() % 1000 == 0) {
                    pstmt.executeBatch();
                }
            }

            pstmt.executeBatch();
            // Ручная фиксация транзакции
            conn.commit();

        } catch (IOException | SQLException | CsvValidationException | ParseException e) {
            throw new IllegalArgumentException(e);
        }
        countDownLatch.countDown();
    }

    public static void GetAverageMag() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT AVG(Magnitude) as tests from EarthShake where State like '%West Virginia%'");
             ResultSet rs = pstmt.executeQuery()) {
            double i = rs.getDouble("tests");
            String formattedValue = String.format("%.2f", i);
            System.out.println("Средняя магнитуда в штате 'West Virginia' имеет значение: " + formattedValue);
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void GraphAverageEartshake() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TreeMap<String, Double> result = new TreeMap<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT strftime('%Y', time) as year, COUNT(*) AS count from EarthShake GROUP BY strftime('%Y', time)");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("year"), rs.getDouble("count"));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        Graph graph = new Graph(result, "Graph Average EarthShake", "EarthShake count per year", "Year");
        graph.setVisible(true);
    }

    public static void GraphAverageMag() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TreeMap<String, Double> result = new TreeMap<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT Magnitude, strftime('%Y', Time) as year from EarthShake where State like '%West Virginia%'");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("year"), rs.getDouble("Magnitude"));
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        Graph graph = new Graph(result, "Graph Average Magnitude", "Magnitude", "Date");
        graph.setVisible(true);
    }

    public static void GetMaxDeep() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT State, Max(DepthM) as maxDeep from EarthShake WHERE strftime('%Y', Time) = '2013' limit 1");
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println("Максимальная глубина в 2013 году наблюдалась в штате " + rs.getString("State") + ", которая составила: " + rs.getString("maxDeep"));
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void GraphMaxDeep() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        TreeMap<String, Double> result = new TreeMap<>();
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("SELECT MAX(DepthM) as MaxDepth, State as name from EarthShake WHERE strftime('%Y', Time) = '2013' group by State");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                if (rs.getString("name").contains(",")) {
                    String[] state = rs.getString("name").split(",");
                    result.put(state[1], rs.getDouble("MaxDepth"));
                } else {
                    result.put(rs.getString("name"), rs.getDouble("MaxDepth"));
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        Graph graph = new Graph(result, "Graph Max Deep", "Depth meter", "State name");
        graph.setVisible(true);
    }
}