package zelda;

import java.sql.*;
import javafx.collections.FXCollections;

public class DBManager {
    
    String dbAddress;
    int port;
    String username, password;
    
    int limit;
    
    DBManager(String dbAddress, int port, String username, String password, int limit){
        this.dbAddress = dbAddress;
        this.port = port;
        this.username = username;
        this.password = password;
        this.limit = limit;
    }
    
    public void loadDefaultRecords() {
        Zelda.records = FXCollections.observableArrayList(
            new Record("AAA", 50),
            new Record("BBB", 40),
            new Record("CCC", 30),
            new Record("DDD", 20),
            new Record("EEE", 10)
        );
    }

    public void loadRecordsDB() {
        Zelda.records = FXCollections.observableArrayList();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://" + dbAddress + ":" + port + "/zelda", username, password);
            Statement st = co.createStatement();
        ) {
            st.setMaxRows(limit);
            ResultSet rs = st.executeQuery("SELECT * FROM record ORDER BY points DESC"); 
            while (rs.next()){ 
                Zelda.records.add(new Record(rs.getString("user"), rs.getInt("points")));
                //System.out.println("" + rs.getString("user") + rs.getInt("points"));
            }
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }

    public void registerRecordDB(Record record) {
        Zelda.records = FXCollections.observableArrayList();
        String user = new String(record.getUser());
        int points = record.getPoints();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://" + dbAddress + ":" + port + "/zelda", username, password);
            PreparedStatement ps = co.prepareStatement("INSERT INTO record VALUES (?, ?)"); 
        ) {
            ps.setString(1, user); ps.setInt(2, points);
            System.out.println("rows affected: " + ps.executeUpdate());
            loadRecordsDB();
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }

    public void deleteRecordDB(Record record) {
        Zelda.records = FXCollections.observableArrayList();
        String user = new String(record.getUser());
        int points = record.getPoints();
        try ( Connection co = DriverManager.getConnection("jdbc:mysql://" + dbAddress + ":" + port + "/zelda", username, password);
            PreparedStatement ps = co.prepareStatement("DELETE FROM record WHERE user = ? AND points = ?"); 
        ) {
            ps.setString(1, user);
            System.out.println("rows affected:" + ps.executeUpdate()); 
            loadRecordsDB();
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }
}
