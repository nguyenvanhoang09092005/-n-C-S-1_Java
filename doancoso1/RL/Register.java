package com.example.doancoso1.RL;

import com.example.doancoso1.Email.Emailto;
import com.example.doancoso1.JDBC.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class Register implements Initializable {
    
    @FXML
    private CheckBox Check;

    @FXML
    private CheckBox Check1;

    @FXML
    private TextField sodienthoai;
    
    @FXML
    private TextField ho;
    
    @FXML
    private TextField ten;
    
    @FXML
    private TextField email;
    
    @FXML
    private PasswordField matkhau;
    
    @FXML
    private PasswordField matkhau1;

    @FXML
    private TextField showMatKhau;

    @FXML
    private TextField showMatKhau1;
    
    @FXML
    private MediaView media2;
    
    @FXML
    private Label thongbao;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String videoFile = "E:\\anhdownload\\vutru.mp4";
        Media media = new Media(new File(videoFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        media2.setMediaPlayer(mediaPlayer);
        media2.getMediaPlayer().setAutoPlay(true);
        
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.seek(Duration.ZERO);
        });
    }
    
    @FXML
    void Login(ActionEvent event) {
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
    void Register(ActionEvent event) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sodienthoaiNumber = sodienthoai.getText();
            String hoValue = ho.getText();
            String tenValue = ten.getText();
            String emailValue = email.getText();
            String mkValue = matkhau.getText();
            
            if (ho.getText().isEmpty() || ten.getText().isEmpty() || email.getText().isEmpty() || matkhau.getText().isEmpty()) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin!");
                return;
            }
            
            if (!(isFirstLetterCapitalized(hoValue) && isFirstLetterCapitalized(tenValue) && areMiddleNamesCapitalized(hoValue))) {
                showAlert("Lỗi", "Chữ đầu tiên của họ,họ đệm và tên phải được viết hoa!");
                return;
            }
            
            if (!sodienthoaiNumber.matches("\\d{10}")) {
                showAlert("Lỗi", "Số điện thoại chỉ được chứa số!");
                return;
            }
            
            if (sodienthoaiNumber.length() != 10) {
                showAlert("Lỗi", "Số điện thoại của bạn đang sai, vui lòng kiểm tra lại!");
                return;
            }
            
            if (!emailValue.endsWith("@gmail.com")) {
                showAlert("Lỗi", "Địa chỉ email của bạn đã sai! Vui lòng nhập lại.");
                return;
            }
            
            String password = matkhau.getText();
            if (!isStrongPassword(password)) {
                showAlert("Lỗi", "Mật khẩu không đủ mạnh. Gợi ý: sử dụng ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
                return;
            }
            
            if (!matkhau.getText().equals(matkhau1.getText())) {
                showAlert("Lỗi", "Mật khẩu không trùng khớp, vui lòng kiểm tra lại!");
                return;
            }
            
            if (!Check.isSelected()) {
                showAlert("Lỗi", "Bạn phải đồng ý với điều khoản và điều kiện để tiếp tục đăng ký!");
                return;
            }
            
            String checkPhoneQuery = "SELECT * FROM user WHERE sodienthoai = ?";
            PreparedStatement checkPhoneStatement = connection.prepareStatement(checkPhoneQuery);
            checkPhoneStatement.setString(1, sodienthoaiNumber);
            ResultSet phoneResultSet = checkPhoneStatement.executeQuery();
            if (phoneResultSet.next()) {
                showAlert("Lỗi", "Số điện thoại đã tồn tại trong hệ thống!");
                return;
            }
            
            String checkEmail = "SELECT * FROM user WHERE email = ?";
            PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmail);
            checkEmailStatement.setString(1,emailValue);
            ResultSet emailResultSet = checkEmailStatement.executeQuery();
            if (emailResultSet.next()) {
                showAlert("Lỗi", "Email này đã tồn tại trong hệ thống!");
                return;
            }
            
            String checkMk = "SELECT * FROM user WHERE matkhau = ?";
            PreparedStatement checkMkStatement = connection.prepareStatement(checkMk);
            checkMkStatement.setString(1,mkValue);
            ResultSet mkResultSet = checkMkStatement.executeQuery();
            if (mkResultSet.next()) {
                showAlert("Lỗi", "Mật khẩu này đã được sử dụng! Vui lòng đặt lại mật khẩu khác.");
                return;
            }
            
            String userID = generateRandomID();
            String insertQuery = "INSERT INTO user (id, sodienthoai, ho, ten, email, matkhau) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, userID);
            preparedStatement.setString(2, sodienthoaiNumber);
            preparedStatement.setString(3, ho.getText());
            preparedStatement.setString(4, ten.getText());
            preparedStatement.setString(5, email.getText());
            preparedStatement.setString(6, matkhau.getText());

//            Emailto.sendEmail(email.getText());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Thông báo");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Tạo tài khoản thành công! Vui lòng đăng nhập.");
                successAlert.getButtonTypes().setAll(ButtonType.OK);
                successAlert.showAndWait();
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Đăng nhập");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Đã xảy ra lỗi khi tạo tài khoản. Vui lòng thử lại sau!");
        }
    }
    
    private String generateRandomID() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        char capitalLetter = (char) (random.nextInt(26) + 'A');
        stringBuilder.append(capitalLetter);
        for (int i = 0; i < 11; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
    
    private boolean isStrongPassword(String password) {
        // Kiểm tra độ dài của mật khẩu
        if (password.length() < 8) {
            return false;
        }
        
        // Kiểm tra sự đa dạng của mật khẩu: chữ hoa, chữ thường, số và ký tự đặc biệt
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecialChar = !password.matches("[A-Za-z0-9 ]*");
        
        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }
    
    private boolean isFirstLetterCapitalized(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return Character.isUpperCase(str.charAt(0));
    }
    
    // Phương thức kiểm tra xem tất cả các từ trong họ đệm có viết hoa không
    private boolean areMiddleNamesCapitalized(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        
        // Tách các từ trong họ đệm bằng dấu cách
        String[] words = str.split("\\s+");
        
        // Kiểm tra từng từ trong họ đệm
        for (int i = 1; i < words.length - 1; i++) {
            if (!Character.isUpperCase(words[i].charAt(0))) {
                return false;
            }
        }
        
        return true;
    }


    @FXML
    void ShowPass(ActionEvent event) {
        if(Check1.isSelected()){
            showMatKhau.setText(matkhau.getText());
            showMatKhau.setVisible(true);
            matkhau.setVisible(false);

            showMatKhau1.setText(matkhau1.getText());
            showMatKhau1.setVisible(true);
            matkhau1.setVisible(false);

            return;
        }
        matkhau.setText(showMatKhau.getText());
        matkhau.setVisible(true);
        showMatKhau.setVisible(false);

        matkhau1.setText(showMatKhau1.getText());
        matkhau1.setVisible(true);
        showMatKhau1.setVisible(false);


    }
    
    // Hiển thị hộp thoại thông báo
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
