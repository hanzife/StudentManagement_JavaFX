package Controller;

import Dao.Database;
import Dao.DatabaseConnection;
import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class PromoController {

    @FXML
    private ComboBox Bx_TeachID;
    @FXML
    private TextField txt_nompromo;

    @FXML
    private DatePicker dateDub;
    @FXML
    private DatePicker DateFin;

    DatabaseConnection db;
    ObservableList list = FXCollections.observableArrayList();


    @FXML
    public void initialize() throws ClassNotFoundException, SQLException {

        Database OpenConnection = new Database();
        Connection connectDB = OpenConnection.getConnection();
        String Query = "SELECT id FROM `user` where user.Role = 'formateur'";
        try {
            PreparedStatement ps2 = OpenConnection.getConnection().prepareStatement(Query);
            ResultSet rs2;
            rs2 = ps2.executeQuery();

            while(rs2.next()) {
                list.addAll(rs2.getInt(1));
                //comboBox.setItems(list);
            }
            Bx_TeachID.setItems(list);

        }
        catch (SQLException ex) {
            System.err.println("Error"+ex);
        }


    }


    public void AddPromotion(ActionEvent actionEvent) throws SQLException {
        System.out.println("add promo");
        //check if all fields are filled else show error
        if(!txt_nompromo.getText().isBlank() && !Bx_TeachID.getValue().toString().isBlank()){
            //call to a function to insert new user
            Database OpenConnection = new Database();
            Connection connectDB = OpenConnection.getConnection();
            String namePromo = txt_nompromo.getText();
            String idTeacher = Bx_TeachID.getValue().toString();
            String DateDub = dateDub.getValue().toString();
            String dateFin = DateFin.getValue().toString();
            String insertQuery = "INSERT INTO promo(nom_promo,Date_debut_scolaire,Date_Fin_scolaire, id_user_formateur) VALUES ('"+namePromo+ "','"+ DateDub +  "','"+ dateFin+"','"+idTeacher+"')";
            System.out.println(insertQuery);
            System.out.println(namePromo);
            System.out.println(idTeacher);

            try {
                Statement stmt = connectDB.createStatement();
                stmt.executeUpdate(insertQuery);
                System.out.println("A new Promo was inserted successfully!");
                //Reflesh TableView

            }
            catch (Exception e){
                e.getMessage();
            }

        }
        else{
            System.out.println("fill all the required fields");
        }
    }

}
