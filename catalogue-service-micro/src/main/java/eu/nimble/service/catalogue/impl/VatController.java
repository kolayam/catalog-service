package eu.nimble.service.catalogue.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.nimble.service.catalogue.config.RoleConfig;
import eu.nimble.service.catalogue.exception.NimbleExceptionMessageCode;
import eu.nimble.service.catalogue.index.ItemIndexClient;
import eu.nimble.utility.ExecutionContext;
import eu.nimble.utility.exception.NimbleException;
import eu.nimble.utility.validation.IValidationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suat on 21-Feb-19.
 */
@Controller
public class VatController {

    private static Logger log = LoggerFactory.getLogger(VatController.class);


    @CrossOrigin(origins = {"*"})
    @ApiOperation(value = "", notes = "Gets all the catalogue identifiers from the item index and clear all data in the index")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted the content in the index successfully"),
            @ApiResponse(code = 401, message = "Invalid token. No user was found for the provided token"),
            @ApiResponse(code = 500, message = "Failed to delete content in the index")
    })
    @RequestMapping(value = "/catalogue/vat/validate",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> clearItemIndex(@ApiParam(value = "The Bearer token provided by the identity service", required = true) @RequestHeader(value = "Authorization", required = true) String bearerToken) {

        Map<String, Object> map = new HashMap<>();
        try {
            JsonNode jsonNode = new ObjectMapper().readTree("");

            log.info("Completed request to clear the item index");

            HmrcOauthService hmrcOauthService = new HmrcOauthService();
            OAuthJSONAccessTokenResponse tokenResponse = hmrcOauthService.getTokenByClientCredential();
            System.out.println(tokenResponse.getAccessToken());
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpUriRequest request = new HttpGet(String.join("",  "https://api.service.hmrc.gov.uk/organisations/vat/check-vat-number/lookup/","220430231"));
            request.setHeader("Authorization", "Bearer " + tokenResponse.getAccessToken());

            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                System.out.println(EntityUtils.toString(response.getEntity()));
                map.put("IsValid",true);
                map.put("BusinessName",jsonNode.path("target").get("name").asText());
            }else{
                throw new RuntimeException(EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok(map);
    }

}
