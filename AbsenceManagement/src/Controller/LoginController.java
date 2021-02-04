package Controller;

import Dao.Database;
import Dao.DatabaseConnection;
import Dao.UserSession;
import Models.Formateur;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
//import model.dao.inmplement.database.Connect;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private  Button btn_login;
    @FXML
    private  Label lbl_Error;
    @FXML
    private  TextField txt_username;
    @FXML
    private  TextField txt_password;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    public void Btn_loginAction(ActionEvent actionEvent) throws SQLException {
        if(!txt_username.getText().isBlank() && !txt_password.getText().isBlank()){
            validateLogin(actionEvent);
            
        }
        else{
            lbl_Error.setText("Invalid login please try again");
        }

    }

    public  void validateLogin(ActionEvent event) throws SQLException {
        Database OpenConnection = new Database();
        Connection connectDB = OpenConnection.getConnection();



        String email = txt_username.getText().toString();
        String password = txt_password.getText().toString();

        DatabaseConnection db_con = new DatabaseConnection();
        Main.logged = db_con.authentification(email, password);

        String verifyLoginQuery = "select count(1) from user where Email = '" + email + "' and password ='" + password + "'";

        try {

            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(verifyLoginQuery);

            while(resultSet.next()){
                if(resultSet.getInt(1)==1){

                    String ReqtypeUser = "SELECT * from user where Email = '" + email + "' and password ='" + password + "'";
                    try {
                        PreparedStatement  ps2 = OpenConnection.getConnection().prepareStatement(ReqtypeUser);
                        ResultSet rs2;
                        rs2 = ps2.executeQuery();

                        if(rs2.next()) {
                            String resultT = rs2.getString(4);
                            UserSession.setUserID(rs2.getString(1));
                            System.out.println(  UserSession.getID());
                            System.out.println( rs2.getString(1));
                            System.out.println( resultT);
                            if (resultT.equals("admin")){
                                try{
                                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/AdminPage.fxml"));
                                    Parent root1 = (Parent) fxmlLoader.load();
                                    Stage stage = new Stage();
                                    stage.setScene(new Scene(root1));
                                    //this.hide();
                                    stage.show();


                                    Stage stageold = (Stage) btn_login.getScene().getWindow();
                                    // do what you have to do
                                    stageold.close();



                                }
                                catch (Exception e){
                                    System.out.println(e.getMessage());
                                }
                            }
                            else if (resultT.equals("formateur"))
                            {
                                if (Main.logged.getClass() == Formateur.class) {
                                    Parent PageFormateur = FXMLLoader.load(getClass().getResource("../View/pageFormateur.fxml"));
                                    Scene s = new Scene(PageFormateur);
                                    Stage page = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    page.setScene(s);
                                    page.show();
                                }


                            }
                            else if (resultT.equals("Secretary"))
                            {
                                //Khalid
                            }
                            else if (resultT.equals("apprenant"))
                            {
                                //Snaiki
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else {
                    lbl_Error.setText("User Doesn't Exist, please check you email and password");
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
