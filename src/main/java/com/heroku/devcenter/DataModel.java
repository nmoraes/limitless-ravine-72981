package com.heroku.devcenter;

import com.force.api.ApiSession;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class DataModel {

	
	
	public static PartnerConnection createPartnerConection(String accessToken, String instanceURL, String CURRENT_VERSION) throws ConnectionException {

	   System.out.println("creating PartnerConection'");

		
		ApiSession session = new ApiSession();
		session.setAccessToken(accessToken);
		session.setApiEndpoint(instanceURL);
		accessToken = session.getAccessToken();

		ConnectorConfig enterpriseConfig = new ConnectorConfig();
		enterpriseConfig.setSessionId(accessToken);
		enterpriseConfig.setServiceEndpoint(session.getApiEndpoint() + "/services/Soap/u/" + CURRENT_VERSION);
		enterpriseConfig.setAuthEndpoint(session.getApiEndpoint() + "/services/Soap/u/" + CURRENT_VERSION);

		PartnerConnection partnerConnection = com.sforce.soap.partner.Connector.newConnection(enterpriseConfig);

		
		return partnerConnection;
		
	}
	
}
