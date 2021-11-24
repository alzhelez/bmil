package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;
import java.util.Optional;

public class UserController {
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private Button save;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bmil";
    private static final String USER = "root";
    private static final String PASS = "123";
    private double[] vector;


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
            ((Stage) save.getScene().getWindow()).close();
        }
    }
