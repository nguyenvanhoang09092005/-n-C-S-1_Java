package com.example.doancoso1.Giaodien;

import com.example.doancoso1.JDBC.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test {

    @FXML
    private TextArea comment;

    @FXML
    private AnchorPane panel_comment;

    private final int VBOX_SPACING = 55;

    @FXML
    void initialize() {
        displayDataFromDatabase();
    }

    private void displayDataFromDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT ho, ten, time, binhluan FROM comments";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            double verticalPosition = 0; // Vị trí dọc ban đầu của VBox

            while (resultSet.next()) {
                // Lấy dữ liệu từ cơ sở dữ liệu cho mỗi bản ghi
                String hoTen = resultSet.getString("ho") + " " + resultSet.getString("ten");
                String time = resultSet.getString("time");
                String binhLuan = resultSet.getString("binhluan");

                // Tạo VBox mới để chứa dữ liệu cho mỗi bản ghi
                VBox recordBox = createRecordBox(hoTen, time, binhLuan);

                // Đặt vị trí của VBox trên panel_comment và cách khoảng cách giữa các VBox là 5
                recordBox.setLayoutY(verticalPosition);
                verticalPosition += recordBox.getPrefHeight() + VBOX_SPACING;

                // Thêm VBox của mỗi bản ghi vào container chính (panel_comment)
                panel_comment.getChildren().add(recordBox);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Xử lý lỗi truy vấn cơ sở dữ liệu
        }
    }

    // Phương thức để tạo VBox chứa dữ liệu của mỗi bản ghi
    private VBox createRecordBox(String hoTen, String time, String binhLuan) {
        VBox recordBox = new VBox();
//        recordBox.setSpacing(VBOX_SPACING); // Đặt khoảng cách giữa các phần tử trong VBox là 5

        Text hoTenText = new Text("" + hoTen);
        Text timeText = new Text("" + time);
        // Tạo các Text để hiển thị dữ liệu
        AnchorPane nameTimePane = new AnchorPane();
        AnchorPane.setTopAnchor(hoTenText, 0.0); // Đặt hoTenText ở vị trí đầu tiên từ trên xuống
        AnchorPane.setLeftAnchor(hoTenText, 0.0); // Đặt hoTenText ở vị trí đầu tiên từ trái sang phải
        AnchorPane.setTopAnchor(timeText, 0.0); // Đặt timeText ở vị trí đầu tiên từ trên xuống
        AnchorPane.setRightAnchor(timeText, 20.0); // Đặt timeText ở vị trí cuối cùng từ phải sang trái và cách lề phải 20px
        nameTimePane.getChildren().addAll(hoTenText, timeText);

        // Tạo Text để hiển thị dữ liệu binhLuan
        Text binhLuanText = new Text("" + binhLuan);
        binhLuanText.setWrappingWidth(800);
        VBox.setMargin(binhLuanText, new Insets(5, 0, 0, 5));
        String[] binhLuanParts = binhLuan.split("\\s+");
        StringBuilder currentLine = new StringBuilder();
        for (String word : binhLuanParts) {
            if (currentLine.length() + word.length() <= 50) {
//                currentLine.append(word).append(" ");
            } else {
                // Tạo một Text mới cho phần văn bản hiện tại và thêm vào VBox
                recordBox.getChildren().add(new Text(currentLine.toString()));
                currentLine = new StringBuilder(word + " ");
            }
        }
        // Thêm các Text vào VBox của mỗi hàng trong cơ sở dữ liệu
        recordBox.getChildren().addAll(nameTimePane, binhLuanText);

        return recordBox;
    }

    @FXML
    void gui(ActionEvent event) {

    }
}
