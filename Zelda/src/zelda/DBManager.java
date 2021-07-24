package zelda;

import java.sql.*;
import javafx.collections.FXCollections;

public class DBManager {
    
    public void caricaClientiPredefiniti() {
        Zelda.records = FXCollections.observableArrayList(
            new Record("AAA", 50),
            new Record("BBB", 40),
            new Record("CCC", 30),
            new Record("DDD", 20),
            new Record("EEE", 10)
        );
    }

    public void caricaClientiDB() {
        Zelda.records = FXCollections.observableArrayList();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/zelda", "root","");
            Statement st = co.createStatement(); 
        ) {
            ResultSet rs = st.executeQuery("SELECT * FROM record"); 
            while (rs.next()){ 
                Zelda.records.add(new Record(rs.getString("user"), rs.getInt("points")));
                System.out.println("" + rs.getString("user") + rs.getInt("points"));
            }
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }

    public void registraClienteDB(Record record) {
        Zelda.records = FXCollections.observableArrayList();
        String user = new String(record.getUser());
        int points = record.getPoints();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/zelda", "root","");
            PreparedStatement ps = co.prepareStatement("INSERT INTO record VALUES (?, ?)"); 
        ) {
            ps.setString(1, user); ps.setInt(2, points);
            System.out.println("rows affected: " + ps.executeUpdate());
            caricaClientiDB();
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }

    public void eliminaClienteDB(Record record) {
        Zelda.records = FXCollections.observableArrayList();
        String user = new String(record.getUser());
        int points = record.getPoints();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/zelda", "root","");
            PreparedStatement ps = co.prepareStatement("DELETE FROM record WHERE id = ?"); 
        ) {
            ps.setString(1, user);
            System.out.println("rows affected:" + ps.executeUpdate()); 
            caricaClientiDB();
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }
    /*
    public void aggiornaClienteDB(Record record) {
        Zelda.records = FXCollections.observableArrayList();
        String user = new String(record.getUser());
        int points = record.getPoints();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/zelda", "root","");
            PreparedStatement ps = co.prepareStatement("UPDATE record SET deposito = deposito + ? WHERE id = ?"); //10
        ) {
            ps.setInt(1,200); ps.setString(2,"michael@boston.us");
            System.out.println("rows affected:" + ps.executeUpdate()); 
            caricaClientiDB();
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }
    */
}
