package product.productSystem.view.beans;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

public class DBConnection {
    public DBConnection() {
        super();
    }
    
    public Connection getDataSourceConnection(String dataSourceName) throws Exception {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(dataSourceName);
        return ds.getConnection();
    }
    
    
    public  Connection getConnection()throws Exception 
     {
        return getDataSourceConnection("java:comp/env/jdbc/productConnDS");
     }
}
