package com.example.doancoso1.Giaodien;

import com.example.doancoso1.JDBC.DatabaseConnection;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Home {
	@FXML
	public VBox commentBox;
	@FXML
	private CheckBox checkbox1;

	@FXML
	private CheckBox checkbox2;

	@FXML
	private TextArea comment;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	@FXML
	private DatePicker data;

	@FXML
	private DatePicker date1;

	@FXML
	private TextField firstName;

	@FXML
	private TextField firstName1;

	@FXML
	private ComboBox<?> gioiTinh;

	@FXML
	private TextField lastName;

	private final int VBOX_SPACING = 55;

	@FXML
	private Text luotDangKy;

	@FXML
	private Text luotTraCuu;

	@FXML
	private AnchorPane panel_comment;

	@FXML
	private TextField sodienthoai;

	@FXML
	private Hyperlink thongtin1;

	@FXML
	private Button tra;

	@FXML
	private VBox vbox1;

	@FXML
	private VBox vbox2;



	@FXML
	void DISC(ActionEvent event) {

	}

	@FXML
	void boity1(ActionEvent event) {

	}

	@FXML
	void boity2(ActionEvent event) {

	}

	@FXML
	void boity3(ActionEvent event) {

	}

	@FXML
	void boity4(ActionEvent event) {

	}

	@FXML
	void cacchiso(ActionEvent event) {

	}

	@FXML
	void cunghoangdao(ActionEvent event) {

	}

	@FXML
	public void initialize() {
		displayDataOnLuotTraCuu();
		displayDataOnLuotDangKy();
		displayDataFromDatabase();
	}

	private void displayDataFromDatabase() {
		try (Connection connection = DatabaseConnection.getConnection()) {
			String query = "SELECT ho, ten, time, binhluan FROM comments ORDER BY time DESC";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();

			panel_comment.getChildren().clear();

			double verticalPosition = 0;

			while (resultSet.next()) {
				// Lấy dữ liệu từ cơ sở dữ liệu cho mỗi bản ghi
				String hoTen = resultSet.getString("ho") + " " + resultSet.getString("ten");
				String time = resultSet.getString("time");
				String binhLuan = resultSet.getString("binhluan");

				// Tạo VBox mới để chứa dữ liệu cho mỗi bản ghi
				VBox recordBox = createRecordBox(hoTen, time, binhLuan);

				recordBox.setLayoutY(verticalPosition);
				verticalPosition += recordBox.getPrefHeight() + VBOX_SPACING;

				panel_comment.getChildren().add(recordBox);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	// Phương thức để tạo VBox chứa dữ liệu của mỗi bản ghi
	private VBox createRecordBox(String hoTen, String time, String binhLuan) {
		VBox recordBox = new VBox();
		Text hoTenText = new Text("" + hoTen);
		Text timeText = new Text("" + time);
		AnchorPane nameTimePane = new AnchorPane();
		AnchorPane.setTopAnchor(hoTenText, 0.0);
		AnchorPane.setLeftAnchor(hoTenText, 0.0);
		AnchorPane.setTopAnchor(timeText, 0.0);
		AnchorPane.setRightAnchor(timeText, 20.0);
		nameTimePane.getChildren().addAll(hoTenText, timeText);

		Text binhLuanText = new Text("" + binhLuan);
		binhLuanText.setWrappingWidth(800);
		VBox.setMargin(binhLuanText, new Insets(5, 0, 0, 5));
		String[] binhLuanParts = binhLuan.split("\\s+");
		StringBuilder currentLine = new StringBuilder();
		for (String word : binhLuanParts) {
			if (currentLine.length() + word.length() <= 50) {
//                currentLine.append(word).append(" ");
			} else {
				recordBox.getChildren().add(new Text(currentLine.toString()));
				currentLine = new StringBuilder(word + " ");
			}
		}
		recordBox.getChildren().addAll(nameTimePane, binhLuanText);

		return recordBox;
	}


	NumberFormat formatter1 = new DecimalFormat("#,###");
	private void displayDataOnLuotTraCuu() {
		try (Connection connection = DatabaseConnection.getConnection()) {
			String query = "SELECT solantracuu FROM tracuu";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();

			int totalSolanTraCuu = 0;
			while (resultSet.next()) {
				int solantracuu = resultSet.getInt("solantracuu");
				totalSolanTraCuu += solantracuu;
			}

			luotTraCuu.setText(formatter1.format(totalSolanTraCuu));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void displayDataOnLuotDangKy() {
		try (Connection connection = DatabaseConnection.getConnection()) {
			String query = "SELECT COUNT(*) AS rowCount FROM user";
			PreparedStatement statement = connection.prepareStatement(query);
			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				int rowCount = resultSet.getInt("rowCount");
				luotDangKy.setText("" + rowCount);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@FXML
	void dangxuat(ActionEvent event) {
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
	void danhxung(ActionEvent event) {

	}

	@FXML
	void dattencongai(ActionEvent event) {

	}

	@FXML
	void dattencontrai(ActionEvent event) {

	}

	@FXML
	void gioithieu(ActionEvent event) {
		try {
			MenuItem menuItem = (MenuItem) event.getSource();
			ContextMenu contextMenu = menuItem.getParentPopup();
			Node node = contextMenu.getOwnerNode();
			Scene scene = node.getScene();
			Stage stage = (Stage) scene.getWindow();

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Giaodien/Gioithieu.fxml"));
			Parent root = loader.load();

			Stage gioiThieuStage = new Stage();
			gioiThieuStage.setTitle("Giới thiệu");

			Scene gioiThieuScene = new Scene(root);
			gioiThieuStage.setScene(gioiThieuScene);

			gioiThieuStage.show();

			stage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private String userId = "Y79671917759";
	private boolean userCommented = false;

	@FXML
	void gui(ActionEvent event) {
		if (!userCommented) {
			String commentText = comment.getText().trim();
			if (!commentText.isEmpty()) {
				try {
					Socket socket = new Socket("localhost", 25689);
					DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

					outputStream.writeUTF(userId);
					outputStream.writeUTF(commentText);
					outputStream.writeUTF(new Timestamp(System.currentTimeMillis()).toString());

					socket.close();

					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					alert.setTitle("Thông báo");
					alert.setHeaderText(null);
					alert.setContentText("Bình luận của bạn đã được gửi thành công!");
					alert.showAndWait();

					panel_comment.getChildren().clear();

					displayDataFromDatabase();

					comment.clear();

					userCommented = true;
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Cảnh báo");
				alert.setHeaderText(null);
				alert.setContentText("Vui lòng nhập nội dung comment!");
				alert.showAndWait();
			}
		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Cảnh báo");
			alert.setHeaderText(null);
			alert.setContentText("Mỗi tài khoản chỉ được bình luận một lần!");
			alert.showAndWait();
		}
	}

	@FXML
	void home(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Giaodien/Home.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.setTitle("Trang Chủ");

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
	void khaisinh(ActionEvent event) {

	}

	@FXML
	void lienhe(ActionEvent event) {
		try {
			MenuItem menuItem1 = (MenuItem) event.getSource();
			ContextMenu contextMenu1 = menuItem1.getParentPopup();
			Node node1 = contextMenu1.getOwnerNode();
			Scene scene1 = node1.getScene();
			Stage stage1 = (Stage) scene1.getWindow();

			FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/Giaodien/Lienhe.fxml"));
			Parent root1 = loader1.load();

			Stage lienHeStage = new Stage();
			lienHeStage.setTitle("Liên Hệ");

			Scene lienHeScene = new Scene(root1);
			lienHeStage.setScene(lienHeScene);

			lienHeStage.show();

			stage1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	void nhomC(ActionEvent event) {

	}

	@FXML
	void nhomD(ActionEvent event) {

	}

	@FXML
	void nhomI(ActionEvent event) {

	}

	@FXML
	void nhomS(ActionEvent event) {

	}

	@FXML
	void search(ActionEvent event) {

	}

	@FXML
	void so1(ActionEvent event) {

	}

	@FXML
	void so10(ActionEvent event) {

	}

	@FXML
	void so11(ActionEvent event) {

	}

	@FXML
	void so2(ActionEvent event) {

	}

	@FXML
	void so22(ActionEvent event) {

	}

	@FXML
	void so3(ActionEvent event) {

	}

	@FXML
	void so4(ActionEvent event) {

	}

	@FXML
	void so5(ActionEvent event) {

	}

	@FXML
	void so6(ActionEvent event) {

	}

	@FXML
	void so7(ActionEvent event) {

	}

	@FXML
	void so8(ActionEvent event) {

	}

	@FXML
	void so9(ActionEvent event) {

	}

	@FXML
	void thongtin(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Giaodien/Thongtin.fxml"));
			Parent root = loader.load();

			Stage stage = new Stage();
			stage.setTitle("Thông Tin");

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
	void tracuu1(ActionEvent event) {

	}

	@FXML
	void tracuu2(ActionEvent event) {

	}

	@FXML
	void tracuu3(ActionEvent event) {

	}

	@FXML
	void tracuu4(ActionEvent event) {

	}

	@FXML
	void tracuuDISc(ActionEvent event) {

	}

	@FXML
	void tracuuMBTI(ActionEvent event) {

	}

	@FXML
	void ynghiaten(ActionEvent event) {

	}

}
