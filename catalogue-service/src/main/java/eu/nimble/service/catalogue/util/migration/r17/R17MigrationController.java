package eu.nimble.service.catalogue.util.migration.r17;

import eu.nimble.service.catalogue.UnitManager;
import eu.nimble.service.catalogue.config.RoleConfig;
import eu.nimble.service.model.ubl.commonaggregatecomponents.ClauseType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.DeliveryTermsType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.PriceOptionType;
import eu.nimble.service.model.ubl.commonbasiccomponents.CodeType;
import eu.nimble.service.model.ubl.commonbasiccomponents.TextType;
import eu.nimble.utility.ExecutionContext;
import eu.nimble.utility.HttpResponseUtil;
import eu.nimble.utility.persistence.GenericJPARepository;
import eu.nimble.utility.persistence.JPARepositoryFactory;
import eu.nimble.utility.validation.IValidationUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Controller
public class R17MigrationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UnitManager unitManager;
    @Autowired
    private IValidationUtil validationUtil;
    @Autowired
    private ExecutionContext executionContext;

    private final String oldIncoterm = "DAT (Delivered at Terminal)";
    private final String newIncoterm = "DPU (Delivery at Place Unloaded)";

    @ApiOperation(value = "", notes = "Add titles to each clause")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Add titles to each clause successfully"),
            @ApiResponse(code = 401, message = "Invalid role"),
            @ApiResponse(code = 500, message = "Unexpected error while adding title to the clause")
    })
    @RequestMapping(value = "/r17/migration/clause-title",
            produces = {"application/json"},
            method = RequestMethod.PATCH)
    public ResponseEntity addClauseTitles(@ApiParam(value = "The Bearer token provided by the identity service", required = true) @RequestHeader(value = "Authorization", required = true) String bearerToken
    ) {
        // set request log of ExecutionContext
        String requestLog = "Incoming request to add clause titles";
        executionContext.setRequestLog(requestLog);

        logger.info(requestLog);

        // validate role
        if(!validationUtil.validateRole(bearerToken,executionContext.getUserRoles(), RoleConfig.REQUIRED_ROLES_FOR_ADMIN_OPERATIONS)) {
            return HttpResponseUtil.createResponseEntityAndLog("Invalid role", HttpStatus.UNAUTHORIZED);
        }

        GenericJPARepository catalogueRepo = new JPARepositoryFactory().forCatalogueRepositoryMultiTransaction(true);
        try{
            List<ClauseType> clauseTypes = catalogueRepo.getEntities(ClauseType.class);
            for (ClauseType clause : clauseTypes) {
                String clauseId = clause.getID();
                String clauseTitle = clauseId.substring(clauseId.indexOf("_")+1);
                TextType title = new TextType();
                title.setLanguageID("en");
                title.setValue(clauseTitle);
                clause.getClauseTitle().add(title);
                catalogueRepo.updateEntity(clause);
            }

            catalogueRepo.commit();
        }
        catch (Exception e){
            catalogueRepo.rollback();
            String msg = "Unexpected error while adding title to the clause";
            logger.error(msg,e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }

        logger.info("Completed request to add clause titles");
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "", notes = "Updates incoterms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated the incoterms successfully"),
            @ApiResponse(code = 401, message = "Invalid role")
    })
    @RequestMapping(value = "/r17/migration/incoterms",
            produces = {"application/json"},
            method = RequestMethod.PATCH)
    public ResponseEntity updateIncoterms(@ApiParam(value = "The Bearer token provided by the identity service", required = true) @RequestHeader(value = "Authorization") String bearerToken
    ) throws IOException {
        logger.info("Incoming request to update incoterms");

        if(!validationUtil.validateRole(bearerToken, executionContext.getUserRoles(), RoleConfig.REQUIRED_ROLES_FOR_ADMIN_OPERATIONS)) {
            return HttpResponseUtil.createResponseEntityAndLog("Invalid role", HttpStatus.UNAUTHORIZED);
        }

        GenericJPARepository catalogueRepo = new JPARepositoryFactory().forCatalogueRepository(true);

        List<CodeType> codeTypeList = catalogueRepo.getEntities(CodeType.class);
        List<DeliveryTermsType> deliveryTermsTypes = catalogueRepo.getEntities(DeliveryTermsType.class);
        List<PriceOptionType> priceOptionTypes =  catalogueRepo.getEntities(PriceOptionType.class);

        for (CodeType codeType : codeTypeList) {
            if(codeType.getValue() != null && codeType.getValue().contentEquals(oldIncoterm)){
                codeType.setValue(newIncoterm);
                catalogueRepo.updateEntity(codeType);
            }
        }

        for (DeliveryTermsType deliveryTermsType : deliveryTermsTypes) {
            if(deliveryTermsType.getIncoterms() != null && deliveryTermsType.getIncoterms().contentEquals(oldIncoterm)){
                deliveryTermsType.setIncoterms(newIncoterm);
                catalogueRepo.updateEntity(deliveryTermsType);
            }
        }

        for (PriceOptionType priceOptionType : priceOptionTypes) {
            if (priceOptionType.getIncoterms().size() != 0){
                Collections.replaceAll(priceOptionType.getIncoterms(),oldIncoterm,newIncoterm);
                catalogueRepo.updateEntity(priceOptionType);
            }
        }

        logger.info("Completed request to update incoterms");
        return ResponseEntity.ok(null);
    }
}