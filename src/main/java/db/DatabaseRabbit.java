package db;

import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heroku.devcenter.DataModel;
import com.heroku.devcenter.MailUtil;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

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


	
	public synchronized void insert(String message) throws SQLException {
		
		Statement stmt = null;
		Connection connection = null;
		PreparedStatement ps = null;

		try {		
			connection = DatabaseRabbit.getConnection();
			stmt = connection.createStatement();

			SimpleDateFormat ft = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
			String date = ft.format(Calendar.getInstance().getTime());

			String dateInt = dateToNum(date);			
			
			String autoIdQuery = "INSERT INTO users (date, json_user) VALUES('" + dateInt+ "','" + message +"');";

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
	
	public synchronized List<String> select() throws SQLException{
		
		Statement stmt = null;
		Connection connection = null;
		ResultSet rs = null;
		
		List<String> idsToDelete = new ArrayList<String>(); 
		try {
			connection = DatabaseRabbit.getConnection();
			stmt = connection.createStatement();

			String autoIdQuery = "SELECT * FROM users;";

			//system time
			SimpleDateFormat ft = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
			String date = ft.format(Calendar.getInstance().getTime());
			String sysDate = dateToNum(date);								
			BigInteger sysDateInt = new BigInteger(sysDate);
					
			rs = stmt.executeQuery(autoIdQuery);

			 while (rs.next()) {
					    String dateDB = rs.getString("date");
					    String sysDateDB = dateToNum(dateDB);
					    
					    //database time
					    BigInteger sysDateIntDB = new BigInteger(sysDateDB);
					
					    //system time - database time 
						BigInteger total =sysDateInt.subtract(sysDateIntDB);
						long total_long = total.longValue();
						
						//Send message if condition true
					    if(total_long >= 500){
					    	//1.send
					    	System.out.println("* Sending email to ID = " + rs.getString("id"));
					    	send(rs.getString("json_user"));
					    	
					    	//2.add to list to delete later
					    	idsToDelete.add(rs.getString("id"));
					    	
					    }
					    			    
					  }	  	  
					
		   

		}catch(Exception e){
			System.out.println("ERROR SELECT METHOD EMAIL");
			
		}finally {
			if (stmt != null) {
				stmt.close();
			}
			if (connection != null) {
				connection.close();
			}
		}
			
		//Delete all record which email was sent
		//delete(idsToDelete);		
		return idsToDelete;
	}
	
					
					
					
					
					
	public synchronized void delete(List<String> idsToDelete) {

		String autoIdQuery = "";
		Statement stmt = null;
		Connection connection = null;
		try {
			connection = DatabaseRabbit.getConnection();
			stmt = connection.createStatement();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int k = 0; k < idsToDelete.size(); k++) {

			System.out.println("Deleting IDS: " + idsToDelete.get(k));
			autoIdQuery = autoIdQuery + "DELETE FROM users WHERE id=" + idsToDelete.get(k) + ";";
		}
		try {
			stmt.execute(autoIdQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	

	}
	

	
	private static String dateToNum(String date){

		String aux = date.replaceAll(" ", "");
		aux = aux.replaceAll(":", "");
					
	return aux;
	
	}
	
	
	
	private void send (String message){
			     
      JSONObject jsonObj = new JSONObject(message);      
      String token = (String) jsonObj.get("token");
      String instance_url = (String) jsonObj.get("instance_url");
      String current_version = (String) jsonObj.get("current_version");
      
      //String email_id = (String) jsonObj.get("email_id");
      String org_id = (String) jsonObj.get("org_id");
      String user_id = (String) jsonObj.get("user_id");
      
      String created_date = (String) jsonObj.get("created_date");
     // String content_version_uploader = (String) jsonObj.get("content_version_uploader");
     // String reportIdDb = (String) jsonObj.get("reportIdDb");
     
      PartnerConnection partnerConection = null;    
      
      try {
		 partnerConection = DataModel.createPartnerConection(token, instance_url, current_version);
	} catch (ConnectionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      
	  MailUtil mailUtil = new MailUtil();
	  
	  try {
		mailUtil.sendMailAPI(partnerConection,"New Feature Lightning Readiness", "BIEN HECHO RABBIT", "rabbitmq", "", null, null, org_id, user_id, null, created_date);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

		
		
	}
	
	
}
