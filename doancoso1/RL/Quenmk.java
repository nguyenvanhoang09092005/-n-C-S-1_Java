
package com.example.doancoso1.RL;

import com.example.doancoso1.JDBC.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Quenmk {

	@FXML
	private CheckBox checkbox;

	@FXML
	private MediaView media4;

	@FXML
	private TextField email3;

	@FXML
	private PasswordField matkhau3;

	@FXML
	private PasswordField nhaplai3;

	@FXML
	private TextField sodienthoai3;

	@FXML
	private TextField showMatKhau3;

	@FXML
	private TextField showNhapLai3;

	public void initialize() {
		String videoFile = "E:\\anhdownload\\vutru.mp4";
		Media media = new Media(new File(videoFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		media4.setMediaPlayer(mediaPlayer);
		media4.getMediaPlayer().setAutoPlay(true);

		// Bắt sự kiện kết thúc của video
		mediaPlayer.setOnEndOfMedia(() -> {
			// Đặt thời gian của video về 0 để bắt đầu lại
			mediaPlayer.seek(Duration.ZERO);
		});
	}

//	@FXML
//	void kiemTraMatKhau(ActionEvent event) {
//		String mkValue = matkhau3.getText();
//		String sodienthoai = sodienthoai3.getText();
//		String email = email3.getText();
//
//		try (Connection connection = DatabaseConnection.getConnection()) {
//			String checkUserQuery = "SELECT * FROM user WHERE sodienthoai = ? OR email = ?";
//			PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery);
//			checkUserStatement.setString(1, sodienthoai);
//			checkUserStatement.setString(2, email);
//			ResultSet userResultSet = checkUserStatement.executeQuery();
//
//			if (userResultSet.next()) {
//				String storedPassword = userResultSet.getString("matkhau");
//				if (mkValue.equals(storedPassword)) {
//					showAlert("Lỗi", "Mật khẩu mới không được trùng với mật khẩu cũ! Vui lòng đặt lại mật khẩu khác.");
//					return;
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			showAlert("Lỗi", "Đã xảy ra lỗi khi kiểm tra mật khẩu. Vui lòng thử lại sau!");
//		}
//
//	}

	@FXML
	void capnhat(ActionEvent event) {
		String sodienthoai = sodienthoai3.getText();
		String email = email3.getText();
		String mkValue = matkhau3.getText();

		if (!sodienthoai.isEmpty() && !email.isEmpty()) {
			if (!DatabaseConnection.checkLoginByPhoneNumberAndEmail(sodienthoai, email)) {
				showAlert("Lỗi", "Số điện thoại và email không khớp với nhau trong cơ sở dữ liệu hoặc không hợp lệ. Vui lòng thử lại hoặc đăng ký tài khoản mới.");
				return;
			}
		} else if (!sodienthoai.isEmpty()) {
			if (!DatabaseConnection.checkLoginByPhoneNumber(sodienthoai)) {
				showAlert("Lỗi", "Số điện thoại không tồn tại trong cơ sở dữ liệu hoặc không hợp lệ. Vui lòng thử lại hoặc đăng ký tài khoản mới.");
				return;
			}
		} else if (!email.isEmpty()) {
			if (!DatabaseConnection.checkLoginByEmail(email)) {
				showAlert("Lỗi", "Email không tồn tại trong cơ sở dữ liệu hoặc không hợp lệ. Vui lòng thử lại hoặc đăng ký tài khoản mới.");
				return;
			}
		} else {
			showAlert("Lỗi", "Vui lòng nhập số điện thoại hoặc email.");
			return;
		}


		if (!matkhau3.getText().equals(nhaplai3.getText())) {
			showAlert("Lỗi", "Mật khẩu không trùng khớp, vui lòng kiểm tra lại!");
			return;
		}

		String password = matkhau3.getText();
		if (!isStrongPassword(password)) {
			showAlert("Lỗi", "Mật khẩu không đủ mạnh. Gợi ý: sử dụng ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.");
			return;
		}

		if (kiemTraMatKhauTrongCSDL(mkValue)) {
			showAlert("Lỗi", "Mật khẩu này đã được sử dụng! Vui lòng đặt lại mật khẩu khác.");
			return;
		}

		try (Connection connection = DatabaseConnection.getConnection()) {
			String updateQuery = "UPDATE user SET matkhau = ? WHERE sodienthoai = ? OR email = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setString(1, mkValue);
			preparedStatement.setString(2, sodienthoai);
			preparedStatement.setString(3, email);
			preparedStatement.executeUpdate();

			Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
			successAlert.setTitle("Thông báo");
			successAlert.setHeaderText(null);
			successAlert.setContentText("Mật khẩu đã được cập nhật thành công.");

			// Thêm biểu tượng dấu tích vào danh sách các nút của thông báo
			successAlert.getButtonTypes().setAll(ButtonType.OK);

			// Hiển thị và chờ người dùng xác nhận
			successAlert.showAndWait();
		} catch (SQLException e) {
			e.printStackTrace();
			showAlert("Lỗi", "Đã xảy ra lỗi khi cập nhật mật khẩu. Vui lòng thử lại sau!");
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.setTitle("Đăng Nhập");

			Scene scene = new Scene(root);
			stage.setScene(scene);

			stage.show();

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	private boolean kiemTraMatKhauTrongCSDL(String mkValue) {
		try (Connection connection = DatabaseConnection.getConnection()) {
			String query = "SELECT * FROM user WHERE matkhau = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, mkValue);
			ResultSet resultSet = statement.executeQuery();
			return resultSet.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean isStrongPassword(String password) {
		if (password.length() < 8) {
			return false;
		}
		boolean hasUppercase = !password.equals(password.toLowerCase());
		boolean hasLowercase = !password.equals(password.toUpperCase());
		boolean hasDigit = password.matches(".*\\d.*");
		boolean hasSpecialChar = !password.matches("[A-Za-z0-9 ]*");

		return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
	}

	@FXML
	void huy(ActionEvent event) {
		sodienthoai3.clear();
		email3.clear();
		matkhau3.clear();
		nhaplai3.clear();
	}


	@FXML
	void quaylai(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Login.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.setTitle("Đăng Nhập");

			Scene scene = new Scene(root);
			stage.setScene(scene);

			stage.show();

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML
	void show(ActionEvent event) {
		if(checkbox.isSelected()){
			showMatKhau3.setText(matkhau3.getText());
			showMatKhau3.setVisible(true);
			matkhau3.setVisible(false);

			showNhapLai3.setText(nhaplai3.getText());
			showNhapLai3.setVisible(true);
			nhaplai3.setVisible(false);

			return;
		}
		matkhau3.setText(showMatKhau3.getText());
		matkhau3.setVisible(true);
		showMatKhau3.setVisible(false);

		nhaplai3.setText(showNhapLai3.getText());
		nhaplai3.setVisible(true);
		showNhapLai3.setVisible(false);


	}


	private void showAlert(String title, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
