package eu.nimble.service.catalogue.config;

import eu.nimble.utility.config.BluemixDatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by suat on 10-Oct-17.
 */
@Component
@PropertySource("classpath:bootstrap.yml")
public class CatalogueServiceConfig {

    private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceConfig.class);

    @Autowired
    private Environment environment;

    @Value("${spring.application.url}")
    private String springApplicationUrl;

    @Value("${persistence.categorydb.driver}")
    private String categoryDbDriver;
    @Value("${persistence.categorydb.connection.url}")
    private String categoryDbConnectionUrl;
    @Value("${persistence.categorydb.username}")
    private String categoryDbUsername;
    @Value("${persistence.categorydb.password}")
    private String categoryDbPassword;
    @Value("${persistence.categorydb.schema}")
    private String categoryDbScheme;

    @Value("${nimble.catalog.max-file-size:5}")
    private int maxFileSize;

    @Value("${nimble.catalog.spare-part}")
    private Boolean sparePartEnabled;

    @Value("${nimble.identity.check-token}")
    private Boolean checkToken;

    @Value("${nimble.binary-content.url}")
    private String binaryContentUrl;

    @Value("${nimble.federation-instance-id}")
    private String federationInstanceId;

    @Value("${nimble.languages}")
    private String catalogueServiceLanguages;

    @PostConstruct
    private void setupDBConnections() {
        if (environment != null) {
            // check for "kubernetes" profile
            if (Arrays.stream(environment.getActiveProfiles()).anyMatch(profile -> profile.contentEquals("kubernetes"))) {

                // setup category database
                String categoryDBCredentialsJson = environment.getProperty("persistence.categorydb.bluemix.credentials_json");
                BluemixDatabaseConfig categoryDBconfig = new BluemixDatabaseConfig(categoryDBCredentialsJson);
                setCategoryDbConnectionUrl(categoryDBconfig.getUrl());
                setCategoryDbUsername(categoryDBconfig.getUsername());
                setCategoryDbPassword(categoryDBconfig.getPassword());
                setCategoryDbDriver(categoryDBconfig.getDriver());
                setCategoryDbScheme(categoryDBconfig.getSchema());
            }
        } else {
            logger.warn("Environment not initialised!");
        }
    }

    public String getSpringApplicationUrl() {
        return springApplicationUrl;
    }

    public void setSpringApplicationUrl(String springApplicationUrl) {
        this.springApplicationUrl = springApplicationUrl;
    }

    public String getCategoryDbDriver() {
        return categoryDbDriver;
    }

    public void setCategoryDbDriver(String categoryDbDriver) {
        this.categoryDbDriver = categoryDbDriver;
    }

    public String getCategoryDbConnectionUrl() {
        return categoryDbConnectionUrl;
    }

    public void setCategoryDbConnectionUrl(String categoryDbConnectionUrl) {
        this.categoryDbConnectionUrl = categoryDbConnectionUrl;
    }

    public String getCategoryDbUsername() {
        return categoryDbUsername;
    }

    public void setCategoryDbUsername(String categoryDbUsername) {
        this.categoryDbUsername = categoryDbUsername;
    }

    public String getCategoryDbPassword() {
        return categoryDbPassword;
    }

    public void setCategoryDbPassword(String categoryDbPassword) {
        this.categoryDbPassword = categoryDbPassword;
    }

    public String getCategoryDbScheme() {
        return categoryDbScheme;
    }

    public void setCategoryDbScheme(String categoryDbScheme) {
        this.categoryDbScheme = categoryDbScheme;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public Boolean getCheckToken() {
        return checkToken;
    }

    public void setCheckToken(Boolean checkToken) {
        this.checkToken = checkToken;
    }

    public String getBinaryContentUrl() {
        return binaryContentUrl;
    }

    public void setBinaryContentUrl(String binaryContentUrl) {
        this.binaryContentUrl = binaryContentUrl;
    }

    public String getFederationInstanceId() {
        return federationInstanceId;
    }

    public void setFederationInstanceId(String federationInstanceId) {
        this.federationInstanceId = federationInstanceId;
    }

    public Boolean getSparePartEnabled() {
        return sparePartEnabled;
    }

    public void setSparePartEnabled(Boolean sparePartEnabled) {
        this.sparePartEnabled = sparePartEnabled;
    }

    public List<String> getCatalogueServiceLanguages() {
        return Arrays.asList(catalogueServiceLanguages.split(","));
    }
}
