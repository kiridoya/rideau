package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.LoginModel;
import model.User;


public class UserDAO {
	//データベース接続に使用する情報
	private final String JDBC_URL="jdbc:mariadb://localhost/rideau";
	private final String DB_USER = "root";
	private final String DB_PASS = "insource.2015it";

	public User findByLogin(LoginModel login) {
		User user = null;

		//データベースへ接続
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try(Connection conn = DriverManager.getConnection(
				JDBC_URL,DB_USER,DB_PASS)) {

			//SELECT文の準備
			String sql = "select * from user where email = ? AND password = ?";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			pStmt.setString(1, login.getMail());
			pStmt.setString(2, login.getPass());


			//SELECTを実行
			ResultSet rs = pStmt.executeQuery();


			//一致したユーザーが存在した場合
			//そのユーザーを表すAccountインスタンスを生成
			if(rs.next()) {
				//結果表からデータを取得
				String userId = rs.getString("user_id");
				String email = rs.getString("email");
				String password = rs.getString("password");
				String userName = rs.getString("user_nm");
				String address = rs.getString("address");
				user = new User( Integer.parseInt(userId), email, password ,userName,address);
			}


		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		//見つかったユーザーまたはnullを返す
		return user;

	}

	public boolean create(User user) {
		//データベースへ接続
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try(Connection conn = DriverManager.getConnection(
				JDBC_URL,DB_USER,DB_PASS)) {

			//INSERT文の準備(idは自動連番)
			String sql ="insert into user (email,password,user_nm,address) values (?,?,?,?)";
			PreparedStatement pStmt = conn.prepareStatement(sql);

			//INSERT文中の「?」に使用する値を設定しSQLを完成
			pStmt.setString(1, user.getEmail());
			pStmt.setString(2, user.getPassword());
			pStmt.setString(3, user.getUserName());
			pStmt.setString(4, user.getAddress());

			//INSERT文を実行(resultには追加された行数が代入される)
			int result = pStmt.executeUpdate();
			if(result != 1) {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;


	}


}
