package appointmentBookingApp.model;

import appointmentBookingApp.util.DbUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DbTableSaveLoad {
    final String table;
    final ResultSet rs;
    final ResultSetMetaData meta;
    final List<String> columns = new ArrayList<>();

    public DbTableSaveLoad(){
        this(null,null,null,new ArrayList<>());
    }

    public DbTableSaveLoad(String table, ResultSet rs, ResultSetMetaData meta, List<String> columns){
        this.table = table;
        this.rs = rs;
        this.meta = meta;
        this.columns.addAll(columns);
    }

    public static DbTableSaveLoad saveTable(String table){
        PreparedStatement pstmt;
        ResultSet rs;
        ResultSetMetaData meta;
        List<String> columns = new ArrayList<>();
        try {
            String sql = "SELECT * FROM "+table;
            pstmt = DbUtil.getConnection().prepareStatement(sql);
            rs = pstmt.executeQuery();
            meta =rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++)
                columns.add(meta.getColumnName(i));
            sql = "DELETE FROM "+table;
            pstmt = DbUtil.getConnection().prepareStatement(sql);
            pstmt.executeUpdate();
            return new DbTableSaveLoad(table,rs,meta,columns);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Nulled");
        return new DbTableSaveLoad();
    }

    public void loadTable(){
        String sql = "INSERT INTO "+table+" ("+columns.stream().collect(Collectors.joining(", "))
                + ") VALUES ("+ columns.stream().map(c -> "?").collect(Collectors.joining(","))+ ")";
        try (PreparedStatement pstmt = DbUtil.getConnection().prepareStatement(sql
        )) {

            while (rs.next()) {
                for (int i = 1; i <= meta.getColumnCount(); i++){
                    pstmt.setObject(i, rs.getObject(i));
                    System.out.print(rs.getObject(i));
                }
                pstmt.addBatch();
            }

            pstmt.executeLargeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
