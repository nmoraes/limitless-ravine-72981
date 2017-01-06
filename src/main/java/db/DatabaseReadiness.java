package db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseReadiness {

	
	 final static Logger logger = LoggerFactory.getLogger(DatabaseReadiness.class);

		
		private static DatabaseReadiness dbonnection;

		// JDBCSingleton prevents the instantiation from any other class.
		private DatabaseReadiness() {
		}

		// Now we are providing global point of access.
		public static DatabaseReadiness getInstance() {
			if (dbonnection == null) {
				dbonnection = new DatabaseReadiness();
			}
			return dbonnection;
		}

		// to get the connection from methods like insert, view etc.
		private static Connection getConnection() throws ClassNotFoundException, SQLException {

			Connection con = null;

			try {

				URI dbUri;
				
			if ("true".equals(System.getenv("IS_PRODUCTION"))) {
				logger.info("[*] Production environment");
				dbUri = new URI(System.getenv("HEROKU_POSTGRESQL_ROSE_URL"));
			} else {
				logger.info("[*] Development environment");
				dbUri = new URI(System.getenv("HEROKU_POSTGRESQL_PURPLE_URL"));
			}
				
				String username = dbUri.getUserInfo().split(":")[0];
				String password = dbUri.getUserInfo().split(":")[1];
			
				String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath()
						+ "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

				Class.forName("org.postgresql.Driver");
				con = DriverManager.getConnection(dbUrl, username, password);

			} catch (URISyntaxException e) {
				logger.error("UNKNOWN ERROR TRYING TO GET THE DATABASE_URL : " + e.getMessage());
			}

			return con;

		}
		
		public String selectHTML() throws SQLException{
			String html="";
			Statement stmt = null;
			Connection connection = null;
			ResultSet rs = null;

			try {
				connection = DatabaseReadiness.getConnection();
				stmt = connection.createStatement();
				
				String autoIdQuery = "SELECT * FROM data_setup_lightningmigrationcheck where parameter='EMAIL_TEMPLATE_4';";
						
				rs = stmt.executeQuery(autoIdQuery);

				 while (rs.next()) {
						   
					  html = rs.getString("value");
						 
			     }		
				 
			}catch(SQLException e){
				logger.error("* Failed to select HTML, SQLException raised " + e.getMessage());				
			}catch (ClassNotFoundException e) {
				logger.error("* Failed to select HTML, ClassNotFoundException raised " + e.getMessage());
			}finally {
				if (stmt != null) {
					stmt.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
			
			return html;
		}	
	
}
