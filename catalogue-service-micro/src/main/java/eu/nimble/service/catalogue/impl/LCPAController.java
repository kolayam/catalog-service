package eu.nimble.service.catalogue.impl;

import eu.nimble.service.catalogue.model.lcpa.ItemLCPAInput;
import eu.nimble.service.catalogue.persistence.util.CatalogueLinePersistenceUtil;
import eu.nimble.service.model.ubl.commonaggregatecomponents.CatalogueLineType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.LCPAOutputType;
import eu.nimble.utility.HttpResponseUtil;
import eu.nimble.utility.JsonSerializationUtility;
import eu.nimble.utility.persistence.GenericJPARepository;
import eu.nimble.utility.persistence.JPARepositoryFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by suat on 27-Mar-19.
 */
@Controller
@RequestMapping(value = "/lcpa")
public class LCPAController {
    private static Logger logger = LoggerFactory.getLogger(PriceConfigurationController.class);

    @CrossOrigin(origins = {"*"})
    @ApiOperation(value = "", notes = "Returns the catalogue uuid/catalogue line information along with the corresponding " +
            "LCPA Details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returned products with valid LCPA input", response = ItemLCPAInput.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "Invalid token. "),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/products-with-lcpa-input",
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity getProductsWithoutLCPAProcessing(@ApiParam(value = "The Bearer token provided by the identity service", required = true) @RequestHeader(value = "Authorization") String bearerToken) {
        try {
            logger.info("Incoming request to get product with LCPA input but not output");
            // check token
            ResponseEntity tokenCheck = eu.nimble.service.catalogue.util.HttpResponseUtil.checkToken(bearerToken);
            if (tokenCheck != null) {
                return tokenCheck;
            }

            List<ItemLCPAInput> results = CatalogueLinePersistenceUtil.getLinesIdsWithValidLcpaInput();

            logger.info("Completed request to get product with LCPA input but not output");
            return ResponseEntity.ok(results);

        } catch (Exception e) {
            return HttpResponseUtil.createResponseEntityAndLog("Unexpecteed error while getting product with LCPA input but not output", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CrossOrigin(origins = {"*"})
    @ApiOperation(value = "", notes = "Adds the provided LCPAOutput data the specified CatalogueLine")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Added the LCPAOutput successfully.", response = CatalogueLineType.class),
            @ApiResponse(code = 400, message = "Could not parse the provided LCPAOutput"),
            @ApiResponse(code = 401, message = "Invalid token."),
            @ApiResponse(code = 404, message = "No CatalogueLine found "),
            @ApiResponse(code = 500, message = "Unexpected error")
    })
    @RequestMapping(value = "/add-lcpa-output",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE},
            method = RequestMethod.PATCH)
    public ResponseEntity addLCPAOutputData(
            @ApiParam(value = "Hjid of the CatalogueLine to be updated", required = true) @RequestParam("catalogueLineHjid") Long catalogueLineHjid,
            @ApiParam(value = "JSON serialization of the LCPAOutput", required = true) @RequestBody String lcpaOutputJson,
            @ApiParam(value = "The Bearer token provided by the identity service", required = true) @RequestHeader(value = "Authorization") String bearerToken) {
        try {
            logger.info("Incoming request to update LCPAOutput for catalogue line with hjid: {}", catalogueLineHjid);
            // check token
            ResponseEntity tokenCheck = eu.nimble.service.catalogue.util.HttpResponseUtil.checkToken(bearerToken);
            if (tokenCheck != null) {
                return tokenCheck;
            }

            GenericJPARepository repo = new JPARepositoryFactory().forCatalogueRepository();
            CatalogueLineType catalogueLine = repo.getSingleEntityByHjid(CatalogueLineType.class, catalogueLineHjid);
            if (catalogueLine == null) {
                String msg = String.format("No CatalogueLine found for the specified hjid: %d", catalogueLineHjid);
                return HttpResponseUtil.createResponseEntityAndLog(msg, HttpStatus.NOT_FOUND);
            }

            LCPAOutputType lcpaOutput;
            try {
                lcpaOutput = JsonSerializationUtility.getObjectMapper().readValue(lcpaOutputJson, LCPAOutputType.class);
            } catch (IOException e) {
                String msg = String.format("Could not parse LCPAOutput for catalogue line with hjid: %d. LCPAOutput: %s", catalogueLineHjid, lcpaOutputJson);
                return HttpResponseUtil.createResponseEntityAndLog(msg, e, HttpStatus.BAD_REQUEST);
            }
            catalogueLine.getGoodsItem().getItem().getLifeCyclePerformanceAssessmentDetails().setLCPAOutput(lcpaOutput);
            catalogueLine = repo.updateEntity(catalogueLine);

            logger.info("Completed request to update LCPAOutput for catalogue line with hjid: {}", catalogueLineHjid);
            return ResponseEntity.ok(catalogueLine);

        } catch (Exception e) {
            String msg = String.format("Unexpected error while updating LCPAOutput for catalogue line with hjid: %d. LCPAOutput: %s", catalogueLineHjid, lcpaOutputJson);
            return HttpResponseUtil.createResponseEntityAndLog(msg, e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
