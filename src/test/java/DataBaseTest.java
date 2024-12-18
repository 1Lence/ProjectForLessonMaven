import org.example.DataBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataBaseTest {
    @BeforeEach
    public void setUp() throws SQLException {
        DataBase.CreateTable();
    }

    @Test
    public void testCreateTable() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             ResultSet rs = conn.getMetaData().getTables(null, null, "EarthShake", null)) {
            assertTrue(rs.next(), "Table EarthShake should be created");
        }
    }

    @Test
    public void testGetAverageMag() throws SQLException, InterruptedException {
        DataBase.CreateTable();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EarthShake(Id, DepthM, MagnitudeType, Magnitude, State, Time) VALUES(?,?,?,?,?,?)")) {
            pstmt.setString(1, "test1");
            pstmt.setInt(2, 100);
            pstmt.setString(3, "Mw");
            pstmt.setDouble(4, 5.0);
            pstmt.setString(5, "West Virginia");
            pstmt.setString(6, "2013-01-01 00:00:00");
            pstmt.executeUpdate();
        }

        DataBase.GetAverageMag();
    }

    @Test
    public void testGetMaxDeep() throws SQLException, InterruptedException {
        DataBase.CreateTable();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EarthShake(Id, DepthM, MagnitudeType, Magnitude, State, Time) VALUES(?,?,?,?,?,?)")) {
            pstmt.setString(1, "test1");
            pstmt.setInt(2, 100);
            pstmt.setString(3, "Mw");
            pstmt.setDouble(4, 5.0);
            pstmt.setString(5, "West Virginia");
            pstmt.setString(6, "2013-01-01 00:00:00");
            pstmt.executeUpdate();
        }

        DataBase.GetMaxDeep();
    }

    @Test
    public void testGraphAverageMag() throws SQLException, InterruptedException {
        DataBase.CreateTable();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EarthShake(Id, DepthM, MagnitudeType, Magnitude, State, Time) VALUES(?,?,?,?,?,?)")) {
            pstmt.setString(1, "test1");
            pstmt.setInt(2, 100);
            pstmt.setString(3, "Mw");
            pstmt.setDouble(4, 5.0);
            pstmt.setString(5, "West Virginia");
            pstmt.setString(6, "2013-01-01 00:00:00");
            pstmt.executeUpdate();
        }

        DataBase.GraphAverageMag();
    }

    @Test
    public void testGraphMaxDeep() throws SQLException, InterruptedException {
        DataBase.CreateTable();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EarthShake(Id, DepthM, MagnitudeType, Magnitude, State, Time) VALUES(?,?,?,?,?,?)")) {
            pstmt.setString(1, "test1");
            pstmt.setInt(2, 100);
            pstmt.setString(3, "Mw");
            pstmt.setDouble(4, 5.0);
            pstmt.setString(5, "West Virginia");
            pstmt.setString(6, "2013-01-01 00:00:00");
            pstmt.executeUpdate();
        }

        DataBase.GraphMaxDeep();
    }

    @Test
    public void testGraphAverageEartshake() throws SQLException, InterruptedException {
        DataBase.CreateTable();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO EarthShake(Id, DepthM, MagnitudeType, Magnitude, State, Time) VALUES(?,?,?,?,?,?)")) {
            pstmt.setString(1, "test1");
            pstmt.setInt(2, 100);
            pstmt.setString(3, "Mw");
            pstmt.setDouble(4, 5.0);
            pstmt.setString(5, "West Virginia");
            pstmt.setString(6, "2013-01-01 00:00:00");
            pstmt.executeUpdate();
        }

        DataBase.GraphAverageEartshake();
    }
}