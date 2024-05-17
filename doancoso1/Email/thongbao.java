package com.example.doancoso1.Email;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class thongbao {

    @FXML
    private TextField so1;

    @FXML
    private TextField so2;

    @FXML
    private TextField so3;

    @FXML
    private TextField so4;

    @FXML
    private TextField so5;

    @FXML
    private TextField so6;

    private StringBuilder verificationCode = new StringBuilder();

    @FXML
    void initialize() {
        setupTextField(so1, so2);
        setupTextField(so2, so3);
        setupTextField(so3, so4);
        setupTextField(so4, so5);
        setupTextField(so5, so6);
        setupTextField(so6, null);


    }

    private void setupTextField(TextField currentField, TextField nextField) {
        currentField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();
            if (!character.matches("\\d") || currentField.getText().length() >= 1) {
                event.consume(); // Chỉ cho phép nhập số và không thêm nếu đã có số trong ô
            } else {
                currentField.setText(character);
                verificationCode.append(character); // Thêm chữ số vào mã xác nhận
                event.consume(); // Tiêu thụ sự kiện để tránh nhập lặp lại
                if (nextField != null) {
                    nextField.requestFocus(); // Chuyển focus đến ô văn bản tiếp theo
                } else {
                    verifyCode(); // Gọi phương thức xác nhận mã
                }
            }
        });

        currentField.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case BACK_SPACE:
                    if (currentField.getText().isEmpty() && nextField != null) {
                        nextField.requestFocus();
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void verifyCode() {
        String code = verificationCode.toString();
        if (code.equals(Emailto.getVerificationCode())) {
            // Mã xác nhận đúng, xử lý thêm ở đây, ví dụ mở cửa sổ mới
            // Đóng cửa sổ hiện tại
            Stage stage = (Stage) so1.getScene().getWindow();
            stage.close();
        } else {
            // Mã xác nhận không đúng, hiển thị cảnh báo
            showAlert("Lỗi", "Mã OTP không đúng!");
            // Xóa hết dữ liệu nhập vào
            so1.setText("");
            so2.setText("");
            so3.setText("");
            so4.setText("");
            so5.setText("");
            so6.setText("");
            // Focus vào ô đầu tiên để người dùng nhập lại
            so1.requestFocus();
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void quaylai(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Đăng nhập");

            Scene scene = new Scene(root);
            stage.setScene(scene);

            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void xacnhan(ActionEvent event) throws IOException {
        String maOTP = so1.getText() + so2.getText() + so3.getText() + so4.getText() + so5.getText() + so6.getText(); // Lấy mã OTP từ các trường nhập liệu

        if (maOTP.isEmpty() || so1.getText().isEmpty() || so2.getText().isEmpty() || so3.getText().isEmpty() || so4.getText().isEmpty() || so5.getText().isEmpty() || so6.getText().isEmpty()) {
            // Hiển thị thông báo nếu có ô trống
            showAlert("Lỗi", "Vui lòng nhập đầy đủ mã OTP!");
            return;
        }

        // Kiểm tra mã OTP
        if (maOTP.equals(Emailto.getVerificationCode())) { // Emailto.getVerificationCode() trả về mã OTP chính xác cần kiểm tra
            // Mã OTP đúng, chuyển đến giao diện home
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Giaodien/Home.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Giao diện chính");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            // Mã OTP không đúng, hiển thị thông báo lỗi
            showAlert("Lỗi", "Mã OTP không đúng. Vui lòng thử lại!");
            so1.setText("");
            so2.setText("");
            so3.setText("");
            so4.setText("");
            so5.setText("");
            so6.setText("");
            // Focus vào ô đầu tiên để người dùng nhập lại
            so1.requestFocus();
        }
    }


}
