package com.example.doancoso1.RL;

import com.example.doancoso1.Email.Emailto;
import com.example.doancoso1.Giaodien.Thongtin;
import com.example.doancoso1.JDBC.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Login extends Component {

    private String userId;

    public void setUserId(String userId) {
        this.userId = userId;
    }
    private String sodienthoai;

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    @FXML
    private CheckBox Check;

    @FXML
    private TextField sodienthoai1;

    @FXML
    private TextField email1;

    @FXML
    private PasswordField matkhau1;

    @FXML
    private TextField showMatKhau1;

    @FXML
    private MediaView media3;

    @FXML
    private TextField verificationCodeField;  // Trường mới để nhập mã xác nhận

    public void initialize() {
        String videoFile = "E:\\anhdownload\\vutru.mp4";
        Media media = new Media(new File(videoFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        media3.setMediaPlayer(mediaPlayer);
        media3.getMediaPlayer().setAutoPlay(true);

        // Đặt lại video khi kết thúc
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
    }

    @FXML
    void Login1(ActionEvent event) throws IOException {
        String sodienthoai = sodienthoai1.getText();
        String email = email1.getText();
        String matkhau = matkhau1.getText();

        if (sodienthoai.isEmpty() || email.isEmpty() || matkhau.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin!");
            return;
        }

        // Lấy userId từ cơ sở dữ liệu
        String userId = DatabaseConnection.getUserId(sodienthoai, email, matkhau);
        if (userId != null) {
            // Gán giá trị userId vào biến userId của Controller
            setUserId(userId);

            Emailto.sendEmail(email);

            // Hiển thị cửa sổ nhập mã xác nhận
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Email/thongbao.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Nhập mã OTP");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            showAlert("Lỗi", "Số điện thoại, Email hoặc Mật khẩu không đúng. Vui lòng thử lại!");
        }
    }

    @FXML
    void Register1(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Đăng Ký");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void quen(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Quenmk.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Quên Mật Khẩu?");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            // Đóng cửa sổ hiện tại
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ShowPass(ActionEvent event) {
        if (Check.isSelected()) {
            showMatKhau1.setText(matkhau1.getText());
            showMatKhau1.setVisible(true);
            matkhau1.setVisible(false);
        } else {
            matkhau1.setText(showMatKhau1.getText());
            matkhau1.setVisible(true);
            showMatKhau1.setVisible(false);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
