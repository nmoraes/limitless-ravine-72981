package db;

public final class Util {
	
	private Util() {
		super();
	}
	
	
	public static String createHTML(String reportID) {

		String body = "<body> <table cellpadding='10px' cellspacing='0' height='100' width='100%' "
				+ "id='bodyTable' style='border-top:4px solid #2b96d4;background-color:#f4f6f9;font-family:SalesforceSans,"
				+ "Helvetica Neue,Helvetica,Arial;font-size:14px'>         <tr style='height: 50px;'>             "
				+ "<td>&nbsp; </td>         </tr>         <tr>             <td align='center' valign='top'>"
				+ "<img src='https://c.na35.content.force.com/servlet/servlet.ImageServer?id=01541000000VAQa&oid=00D410000006fO0'"
				+ " height='100' width='145'> </td>         </tr>         <tr>             <td align='center'>                 "
				+ "<table border='0' cellpadding='20' cellspacing='0' width='400' id='emailContainer' "
				+ "style='background-color: #ffffff;width: 650px;height: 200px;margin: auto;border: "
				+ "1px solid #ccc;border-radius: 10px;-webkit-border-radius: 10px;-moz-border-radius: "
				+ "10px;-webkit-box-shadow: 2px 2px 10px 2px #526690;box-shadow: 2px 2px 10px 2px #526690;'>                    "
				+ " <tr> <td colspan='3' align='center' valign='top' style='padding-bottom:0px;'>"
				+ " <!-- <p style='font-family:SalesforceSansLight,Helvetica Neue,Helvetica,Arial;color:#526690;font-size:20px;'>"
				+ "Questions about your Lightning Experience readiness report?</p> --> </td> </tr> "
				+ "<tr> <td colspan='3' align='center' valign='top' style='padding-top:0px;padding-bottom:0px;'> "
				+ " <p style='margin-top: 20px;line-height: 23px;margin-bottom: 20px; color:#526690;font-family:SalesforceSans,"
				+ "Helvetica Neue,Helvetica,Arial;font-size:14px;'>Have any questions about the readiness report we emailed you? "
				+ "We’ve got you covered. Sign up for a free consultation with one of our Lightning Experience experts. "
				+ "They’ll make sure your migration plan is headed in the right direction. If you’re stuck, "
				+ "they’ll provide you with recommendations on how to proceed.</p></td>  </tr><td align='center'> <div > "
				+ "<div style='display:inline-block'> <center style=\"margin-top: 5px;margin-bottom: 20px;\"> "
				+ "<div style='display: inline;background-color: #006ed5;padding: 10px;border-radius: 5px;font-size: 15px;font-weight: lighter;'>                              "
				+ " <a href='https://lightning-readiness-dev.herokuapp.com/audit?link=1&reportId=" + reportID
				+ "&templateId=4' "
				+ "style=\"color: white;text-decoration: none;padding: 12px;\">Schedule a 30-Minute Consultation</a></div></center>                           "
				+ "</div> </div> <!--    <p style='margin-bottom: 1rem; color:#526690;font-family:SalesforceSans,Helvetica Neue,Helvetica,Arial;font-size:14px;'>Welcome to Lightning Experience!</p> -->"
				+ "</td></tr> </table> </td> </tr> <tr> <td align='center' valign='top'> <table border='0' cellpadding='20' cellspacing='0' width='600'>                     <tr>                         "
				+ "<td align='left' valign='top'> <p style=' font-family:SalesforceSans,Helvetica Neue,Helvetica,Arial;font-size:12px;color:#798188' '>Copyright 2016 Salesforce - All rights reserved</p><p style='font-family:SalesforceSans,Helvetica Neue,Helvetica,Arial;font-size:12px;color:#798188 ''>Salesforce and the \"no software\" logo are registered trademarks of salesforce.com, inc., and salesforce.com owns the registered and unregistered trademarks. Other names used herein may be trademarks of their respective owners.</p>                             <p style='margin-bottom: 1rem; font-family:SalesforceSans,Helvetica Neue,Helvetica,Arial;font-size:12px;color:#798188'>Salesforce.com, Inc. The Landmark @ One Market, Suite 300, San Francisco, CA, 94015, United States General Enquiries: 415-901-7000 | Fax: 415-901-7040 | Sales: 1-800-NO-SOFTWARE </p>                         </td>                     </tr>                 </table>             </td>         </tr>      </table> </body>  </html> ";

		return body;

	}
	


}
