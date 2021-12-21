package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.*;
import java.util.Arrays;

public class UserController {
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private RadioButton log;
    @FXML
    private Button save;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bmil";
    private static final String USER = "root";
    private static final String PASS = "123";
    private double[] vector;
    public static String user;


    public void initialize(double[] vector) {
        this.vector = vector;
    }

    public void saveUser(ActionEvent actionEvent) {
            System.out.println("Testing connection to PostgreSQL JDBC");

            try {
                System.out.println("PostgreSQL JDBC Driver successfully connected");
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
                e.printStackTrace();
                return;
            }
            if (log.isSelected()){
                try {
                    int i = 0, j = 0;
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    String sql = "SELECT * FROM users.userss WHERE name = '"+ name.getText()+"'";

                    PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet res = stmt.executeQuery();
                    while (res.next()){
                        if (BCrypt.checkpw(password.getText(), res.getString(4))){
                            System.out.println("User " + res.getString(2) + " passed authentication");
                            user = name.getText();
                            i = 1;
                        }
                        if (res.getString(5).equals(Arrays.toString(vector))){
                            System.out.println("User " + res.getString(2) + " passed verification");
                            user = name.getText();
                            j = 1;
                        } else {
                            user = name.getText();
                        }
                        System.out.println(user);

                        if (i == 1 && j == 1){
                            JFrame jFrame = new JFrame();
                            JOptionPane.showMessageDialog(jFrame,
                                    "User: " + user + " is already passed authentication and verification");
                        }

                    }
                    stmt.close();
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Connection Failed");
                    e.printStackTrace();
                    return;
                }
            } else {
                try {
                Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
                String sql = "INSERT INTO users.userss(name, email, password, vector) VALUES (?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,name.getText());
                stmt.setString(2,email.getText());
                stmt.setString(3,BCrypt.hashpw(password.getText(), BCrypt.gensalt()));
                stmt.setString(4,Arrays.toString(vector));
                int rows = stmt.executeUpdate();
                if (rows > 0){
                    System.out.println("You successfully add data to database now");
                }
                stmt.close();
                connection.close();
                } catch (SQLException e) {
                    System.out.println("Connection Failed");
                    e.printStackTrace();
                    return;
                }
            }
            ((Stage) save.getScene().getWindow()).close();
        }
}
