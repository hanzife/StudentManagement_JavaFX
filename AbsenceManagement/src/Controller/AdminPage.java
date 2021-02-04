package Controller;

import Dao.Database;
import Dao.DatabaseConnection;
import Dao.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Models.TableModel;
//import model.dao.inmplement.database.Connect;

import java.io.IOException;
import java.sql.*;

public class AdminPage {
    //Get all FXML Elements
    @FXML
    private TextField nomUser;
    @FXML
    private TextField emailUser;
    @FXML
    private TextField passUser;
    @FXML
    private ComboBox bx_typeUser;
    @FXML
    private ComboBox bx_promo;
    @FXML
    private Button btnlogout;

    //Needed for retrieving data to TableView (see Model/TableView)
    @FXML
    private TableView<TableModel> table;
    @FXML
    private TableColumn<TableModel, String> idcol;
    @FXML
    private TableColumn<TableModel, String> namecol;
    @FXML
    private TableColumn<TableModel, String> emailcol;
    @FXML
    private TableColumn<TableModel, String> passwordcol;
    @FXML
    private TableColumn<TableModel, String> stafftypecol;
    @FXML
    private TableColumn<TableModel, String> promocol;
    //to Store the rows into a list - neede for tableview
    private ObservableList<TableModel> listView = FXCollections.observableArrayList();
    ObservableList listIdTeachers = FXCollections.observableArrayList();

    private int id_Promo = 0;
    //call for TableView and to fill combobox
    @FXML
    public void initialize() throws ClassNotFoundException, SQLException {
        getData();
        bx_typeUser.getItems().addAll("admin","formateur","Secretary","apprenant");
        //bx_promo.getItems().addAll("ada","mari");
        loadCBX();
    }

    //Add new user button
    public void InsertAction(ActionEvent actionEvent) throws SQLException {
        //check if all fields are filled else show error
        if(!nomUser.getText().isBlank() && !emailUser.getText().isBlank() &&  !passUser.getText().isBlank() &&  !bx_typeUser.getValue().toString().isBlank()){
           //call to a function to insert new user
           InsertUser();
        }
        else{
            System.out.println("fill all the required fields");
        }
    }

    public void  loadCBX(){
        Database OpenConnection = new Database();
        Connection connectDB = OpenConnection.getConnection();
        String Query = "SELECT `id_promo` FROM `promo`";
        try {
            PreparedStatement ps2 = OpenConnection.getConnection().prepareStatement(Query);
            ResultSet rs2;
            rs2 = ps2.executeQuery();

            while(rs2.next()) {
                listIdTeachers.addAll(rs2.getInt(1));
                //comboBox.setItems(list);
            }
            bx_promo.setItems(listIdTeachers);

        }
        catch (SQLException ex) {
            System.err.println("Error"+ex);
        }
    }
   public void AddPromo(ActionEvent actionEvent) throws IOException {

       try{
           FXMLLoader fxmlLoaderPro = new FXMLLoader(getClass().getResource("../View/Promo.fxml"));
           Parent promoRoot= (Parent) fxmlLoaderPro.load();
           Stage stageLogin = new Stage();
           stageLogin.setScene(new Scene(promoRoot));
           stageLogin.show();
           System.out.println("Prom2o");



       }
       catch (Exception e){
           System.out.println(e.getMessage());
       }




    }
    public void ActivatedPromotion(ActionEvent actionEvent) throws SQLException{
        if(bx_typeUser.getValue().toString() == "apprenant" || bx_typeUser.getValue().toString() == "formateur"){

            bx_promo.setDisable(false);

        }
    }




    public void ActionDestroy(ActionEvent actionEvent) throws IOException {
        UserSession.cleanUserSession(); //Destroy Session

        Stage stage = (Stage) btnlogout.getScene().getWindow();
        // do what you have to do
        stage.close();


        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/pageLogin.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stageLogin = new Stage();
            stageLogin.setScene(new Scene(root1));
            //this.hide();
            stageLogin.show();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }





    }

    public void InsertUser() throws SQLException{
        /*if(bx_typeUser.getValue().toString() == "apprenant" || bx_typeUser.getValue().toString() == "formateur") {
            if (bx_promo.getValue().toString() == "ada") {
                id_Promo = 20;
            } else if (bx_promo.getValue().toString() == "mari") {
                id_Promo = 21;
            }
        }
        else {
            id_Promo = 0;
        }*/
        //Connect to DV
        Database OpenConnection = new Database();
        Connection connectDB = OpenConnection.getConnection();

        //getting data from textfields
        String nom = nomUser.getText();
        String email = emailUser.getText();
        String passuser = passUser.getText();
        String type_user = bx_typeUser.getValue().toString();
        id_Promo = Integer.parseInt(bx_promo.getValue().toString());
        //Query needed to check if this email already taken or a new one
        //if taken > error else insert user
        String ReqtypeUser = "SELECT Email from user where Email = '" + email +"'";
        PreparedStatement  ps2 = OpenConnection.getConnection().prepareStatement(ReqtypeUser);
        ResultSet rs2;
        rs2 = ps2.executeQuery();
        //if there is record
        if(rs2.next()) {
            String resultEmail = rs2.getString(1);
            System.out.println(resultEmail);
            //that email exist in our DB
            if (resultEmail.equals(email)){
                System.out.println(resultEmail);
                System.out.println("Sorry User email already taken");
            }
        }
        else {
            //now insert new user Query
            String insertQuery = "INSERT INTO user(nomuser, Email, Role, password,id_promo) VALUES ('"+nom+"','"+email+"','"+type_user+"','"+passuser+"','"+id_Promo+"')";
            System.out.println(insertQuery);
            try {
                Statement stmt = connectDB.createStatement();
                stmt.executeUpdate(insertQuery);
                System.out.println("A new user was inserted successfully!");
                //Reflesh TableView
                getData();
            }
            catch (Exception e){
                e.getMessage();
            }
        }
        }





    public void getData() throws SQLException {
        //Clear TableView when there is records
        //this will insure that we don't fill the table with double records
        for ( int i = 0; i<table.getItems().size(); i++) {
            table.getItems().clear();
        }
        //listView.clear(); //Equevilant but not practical at all it clears the list not the table - this will make us to reflesh page manualy

        try {
            Database OpenConnection = new Database();
            Connection connectDB = OpenConnection.getConnection();
            //Remember the Model/TableView ? set those variable with the values from the fields
            //Models helps access database to retrieve data
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            namecol.setCellValueFactory(new PropertyValueFactory<>("name"));
            emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
            passwordcol.setCellValueFactory(new PropertyValueFactory<>("stafftype"));
            stafftypecol.setCellValueFactory(new PropertyValueFactory<>("password"));
            promocol.setCellValueFactory(new PropertyValueFactory<>("id_promo"));

            // String sql = "SELECT `id`, `nomuser`, `Email`, `Role`, `password`, `id_promo` FROM `user`";
            // String sql = "SELECT user.id, user.nomuser, user.Email, user.Role, user.password, id_promo FROM user";
            String sql = "SELECT user.id, user.nomuser, user.Email, user.Role, user.password, promo.nom_promo FROM user INNER JOIN promo on promo.id_promo = user.id_promo";

            Statement S = connectDB.createStatement();
            ResultSet Res = S.executeQuery(sql);
            while (Res.next()){
                listView.add(new TableModel(
                    Res.getString("id"),
                    Res.getString("nomuser"),
                    Res.getString("Email"),
                    Res.getString("Role"),
                    Res.getString("password"),
                    Res.getString("nom_promo")
                ));
            }
            table.setItems(listView);
            }
            catch (Exception e){
                e.getMessage();
            }


    }

}
