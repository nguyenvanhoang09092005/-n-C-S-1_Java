package com.example.doancoso1.JDBC;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/doancoso1";
    private static final String USER = "root";
    private static final String PASS = "Nguyenvanhoang2005@";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static String getUserId(String sodienthoai, String email, String matkhau) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, ho, ten FROM user WHERE sodienthoai = ? AND email = ? AND matkhau = ?")) {
            preparedStatement.setString(1, sodienthoai);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, matkhau);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Trả về một chuỗi được cấu trúc sẵn chứa thông tin ID, họ và tên của người dùng
                    return resultSet.getString("id") + ", " + resultSet.getString("ho") + ", " + resultSet.getString("ten");
                } else {
                    return null; // Trả về null nếu không tìm thấy người dùng
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean checkLogin(String sodienthoai, String email) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE sodienthoai = ? AND email = ? AND matkhau = ?")) {
            preparedStatement.setString(1, sodienthoai);
            preparedStatement.setString(2, email);
//            preparedStatement.setString(3, matkhau);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean checkLoginByPhoneNumber(String sodienthoai) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE sodienthoai = ? ")) {
            preparedStatement.setString(1, sodienthoai);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean checkLoginByEmail(String email) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE email = ? ")) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

        public static boolean checkLoginByPhoneNumberAndEmail(String phoneNumber, String email) {
            String query = "SELECT * FROM user WHERE sodienthoai = ? AND email = ?";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, phoneNumber);
                statement.setString(2, email);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

    public int executeDB(String sql) {
        int n = 0;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            n = stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }
}
    