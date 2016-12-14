package db;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseRabbit {

	 final static Logger logger = LoggerFactory.getLogger(DatabaseRabbit.class);

	
	private static DatabaseRabbit dbonnection;

	// JDBCSingleton prevents the instantiation from any other class.
	private DatabaseRabbit() {
	}

	// Now we are providing global point of access.
	public static DatabaseRabbit getInstance() {
		if (dbonnection == null) {
			dbonnection = new DatabaseRabbit();
		}
		return dbonnection;
	}

	// to get the connection from methods like insert, view etc.
	private static Connection getConnection() throws ClassNotFoundException, SQLException {

		Connection con = null;

		try {

			URI dbUri = new URI(System.getenv("DATABASE_URL"));
			
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


	
	public void insert(String message) throws SQLException {
		
		Statement stmt = null;
		Connection connection = null;
		PreparedStatement ps = null;

		try {		
			connection = DatabaseRabbit.getConnection();
			stmt = connection.createStatement();

			SimpleDateFormat ft = new SimpleDateFormat("dd MMM yyyy dd hh:mm:ss");
			String date = ft.format(Calendar.getInstance().getTime());

			
			System.out.println(date);
			
			String autoIdQuery = "INSERT INTO users (date, json_user) VALUES('" + date+ "','" + message +"');";

			stmt.execute(autoIdQuery);
			
		} catch (Exception e) {
			logger.error(" Failed to insert data data " + e.getMessage());
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (connection != null) {
				connection.close();
			}
		}

	}
	
	public void select(){}
	
	public void delete(){}
	
	public void deleteOldReports() throws SQLException {
//		Statement stmt = null;
//		Connection connection = null;
//		ResultSet rs = null;
//		try {
//			connection = DatabaseRabbit.getConnection();
//			stmt = connection.createStatement();
//
//			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_YEAR, -90);
//			String dt = ft.format(cal.getTime());
//
//			// get the reports Id in the DB
//			String autoIdQuery = "SELECT id FROM report WHERE date<'" + dt.toString() + "';";
//
//			rs = stmt.executeQuery(autoIdQuery);
//
//			List<String> reportIdList = new ArrayList<String>();
//			while (rs.next()) {
//				reportIdList.add(rs.getString("id"));
//			}
//
//			for (String reportId : reportIdList) {
//
//				logger.info(reportId);
//
//				autoIdQuery = "";
//
//				// TabsAndObjects
//				autoIdQuery += "DELETE FROM tabsandobjects WHERE reportid=" + reportId + ";";
//
//				// CustomButtonsJS
//				autoIdQuery += "DELETE FROM custombuttonsjs WHERE reportid=" + reportId + ";";
//
//				// CustomButtonsLinks
//				autoIdQuery += "DELETE FROM custombuttonslinks WHERE reportid=" + reportId + ";";
//
//				// RelatedLists
//				autoIdQuery += "DELETE FROM relatedlists WHERE reportid=" + reportId + ";";
//
//				// SharingButtons
//				autoIdQuery += "DELETE FROM sharingbuttons WHERE reportid=" + reportId + ";";
//
//				// Cases
//				autoIdQuery += "DELETE FROM cases WHERE reportid=" + reportId + ";";
//
//				// CTI
//				autoIdQuery += "DELETE FROM cti WHERE reportid=" + reportId + ";";
//
//				// MyDomain
//				autoIdQuery += "DELETE FROM mydomain WHERE reportid=" + reportId + ";";
//				
//				// Data.Com
//				autoIdQuery += "DELETE FROM datacom WHERE reportid=" + reportId + ";";
//
//				// delete sub tables
//				stmt.execute(autoIdQuery);
//
//				// delete main tables
//				autoIdQuery = "DELETE FROM report WHERE id=" + reportId + ";";
//				stmt.execute(autoIdQuery);
//
//			}
//
//			// update purge date
//			autoIdQuery = "UPDATE transactions_info SET value='"
//					+ ft.format(Calendar.getInstance().getTime()).toString() + "' WHERE name='date_purge';";
//
//			stmt.execute(autoIdQuery);
//
//		} catch (Exception e) {
//			logger.error(" Failed to delete old reports : " + e.getMessage());
//		} finally {
//			if (rs != null) {
//				rs.close();
//			}
//			if (stmt != null) {
//				stmt.close();
//			}
//			if (connection != null) {
//				connection.close();
//			}
//		}

	}
	
	
	

	
	

}
