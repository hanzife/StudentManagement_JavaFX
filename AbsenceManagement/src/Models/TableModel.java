package Models;

public class TableModel {
    String id;
    String name;
    String email;
    String password;
    String stafftype;
    String id_promo;

    public TableModel(String id, String name, String email, String password, String stafftype, String id_promo){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.stafftype = stafftype;
        this.id_promo = id_promo;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStafftype() {
        return stafftype;
    }

    public void setStafftype(String stafftype) {
        this.stafftype = stafftype;
    }

    public String getId_promo() {
        return id_promo;
    }

    public void setId_promo(String id_promo) {
        this.id_promo = id_promo;
    }
}
