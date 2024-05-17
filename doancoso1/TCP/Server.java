package com.example.doancoso1.TCP;
import com.example.doancoso1.JDBC.DatabaseConnection;
import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSK = new ServerSocket(25689);
            System.out.println("Server đang chạy...");

            while (true) {
                Socket skServer = serverSK.accept();
                System.out.println("Kết nối thành công");

                try (DataInputStream inputServer = new DataInputStream(skServer.getInputStream())) {
                    // Lấy userID từ client
                    String userId = inputServer.readUTF();
                    System.out.println("User ID: " + userId);

                    // Lấy thông tin người dùng từ bảng user
                    String userSql = "SELECT ho, ten FROM user WHERE id = ?";
                    String ho = "";
                    String ten = "";

                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(userSql)) {
                        stmt.setString(1, userId);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            ho = rs.getString("ho");
                            ten = rs.getString("ten");
                        } else {
                            JOptionPane.showMessageDialog(null, "Không tìm thấy người dùng!");
                            continue; // Bỏ qua vòng lặp này nếu không tìm thấy người dùng
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi truy vấn dữ liệu người dùng: " + e.getMessage());
                        continue; // Bỏ qua vòng lặp này nếu có lỗi khi truy vấn dữ liệu người dùng
                    }

                    // Lấy bình luận và thời gian từ client
                    String binhluan = inputServer.readUTF();
                    System.out.println("Bình luận: " + binhluan);
                    String time = inputServer.readUTF();
                    System.out.println("Thời gian: " + time);

                    // Lưu bình luận vào bảng comments
                    String commentSql = "INSERT INTO comments (id, ho, ten, binhluan, time) VALUES (?, ?, ?, ?, ?)";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement stmt = conn.prepareStatement(commentSql)) {
                        stmt.setString(1, userId);
                        stmt.setString(2, ho);
                        stmt.setString(3, ten);
                        stmt.setString(4, binhluan);
                        stmt.setString(5, time);
                        int n = stmt.executeUpdate();
                        if (n > 0) {
                            JOptionPane.showMessageDialog(null, "Bình luận đã được lưu thành công!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Không thể lưu bình luận!");
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi lưu bình luận: " + e.getMessage());
                    }
                } catch (IOException e) {
                    System.out.println("Lỗi: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}
