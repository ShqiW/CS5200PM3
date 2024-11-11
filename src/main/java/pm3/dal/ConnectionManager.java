package pm3.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager{
	private final String user = "root";
	private final String password = "----";
	private final String hostName = "localhost";
	private final int port = 3306;
	private final String schema = "CS5200Project";
	private final String timezone = "UTC";
	
	
	public Connection getConnection() throws SQLException{
		Connection connection = null;
		
		try {
			Properties connectionProperties = new Properties();
			connectionProperties.put("user", this.user);
	        connectionProperties.put("password", this.password);
	        connectionProperties.put("serverTimezone", this.timezone);

	     // Load JDBC driver
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException(e);
            }
            
	        connection = DriverManager.getConnection("jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema + "?useSSL=false",
                connectionProperties);
	        
		} catch(SQLException e){
			e.printStackTrace();
			throw e;
		}
		return connection;
	}

    public void closeConnection(Connection connection) throws SQLException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}