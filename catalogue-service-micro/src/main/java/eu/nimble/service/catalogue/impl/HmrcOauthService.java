package eu.nimble.service.catalogue.impl;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class HmrcOauthService {
	private EntityManager entityManager;

	private OAuthClient oAuthClient=null;
	@Value("${hmrc.client-id}")
	private String clientId;
	@Value("${hmrc.client-secret}")
	private String clientSecret;
	private String authorizeUrl;
	private String tokenUrl;
	private String callbackUrl;
	private String scope;
	private String username;
	private String state;
	@Value("${hmrc.api-base}")
	private String baseUrl;
	
	public HmrcOauthService() {
		this.oAuthClient = new OAuthClient(new URLConnectionClient());
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public OAuthJSONAccessTokenResponse getTokenByClientCredential() {
		try {
			OAuthClientRequest request = OAuthClientRequest.tokenLocation(this.baseUrl + "/oauth/token")
					.setGrantType(GrantType.CLIENT_CREDENTIALS).setClientId(clientId).setClientSecret(clientSecret)
					.buildBodyMessage();
			OAuthJSONAccessTokenResponse token = this.oAuthClient.accessToken(request);
			return token;
		} catch (OAuthSystemException | OAuthProblemException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

	}
}
