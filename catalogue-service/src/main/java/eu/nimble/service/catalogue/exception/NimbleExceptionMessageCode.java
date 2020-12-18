package eu.nimble.service.catalogue.exception;

public enum NimbleExceptionMessageCode {
    BAD_REQUEST_AMOUNT_CURRENCY_REQUIRED_FOR_PRICE("BAD_REQUEST.amountCurrencyRequiredForPrice"),
    BAD_REQUEST_DESERIALIZE_CATALOGUE("BAD_REQUEST.deserializeCatalogue"),
    BAD_REQUEST_DESERIALIZE_CATALOGUE_LINE("BAD_REQUEST.deserializeCatalogueLine"),
    BAD_REQUEST_GET_CATALOGUE_LINES("BAD_REQUEST.getCatalogueLines"),
    BAD_REQUEST_GET_ZIP_PACKAGE("BAD_REQUEST.getZipPackage"),
    BAD_REQUEST_HJIDS("BAD_REQUEST.hjids"),
    BAD_REQUEST_HJIDS_IN_PRICE_OPTION("BAD_REQUEST.hjidsInPriceOption"),
    BAD_REQUEST_IDS_DO_NOT_MATCH("BAD_REQUEST.idsDoNotMatch"),
    BAD_REQUEST_INVALID_DATA_TYPE_FOR_PROPERTY("BAD_REQUEST.invalidDataTypeForProperty"),
    BAD_REQUEST_INVALID_HEIGHT_DIMENSION("BAD_REQUEST.invalidHeightDimension"),
    BAD_REQUEST_INVALID_WEIGHT_DIMENSION("BAD_REQUEST.invalidWeightDimension"),
    BAD_REQUEST_INVALID_HJIDS("BAD_REQUEST.invalidHjids"),
    BAD_REQUEST_INVALID_HJIDS_IN_LINE("BAD_REQUEST.invalidHjidsInLine"),
    BAD_REQUEST_INVALID_LENGTH_DIMENSION("BAD_REQUEST.invalidLengthDimension"),
    BAD_REQUEST_INVALID_PARAMETERS_TO_GET_CATEGORIES("BAD_REQUEST.invalidParametersToGetCategories"),
    BAD_REQUEST_INVALID_REFERENCE("BAD_REQUEST.invalidReference"),
    BAD_REQUEST_INVALID_STANDARD("BAD_REQUEST.invalidStandard"),
    BAD_REQUEST_INVALID_TAXONOMY("BAD_REQUEST.invalidTaxonomy"),
    BAD_REQUEST_INVALID_TEMPLATE_FORMAT ("BAD_REQUEST.invalidTemplateFormat"),
    BAD_REQUEST_INVALID_VALUE_FOR_BOOLEAN_PROPERTY ("BAD_REQUEST.invalidValueForBooleanProperty"),
    BAD_REQUEST_INVALID_WIDTH_DIMENSION("BAD_REQUEST.invalidWidthDimension"),
    BAD_REQUEST_LARGER_THAN_ALLOWED_SIZE("BAD_REQUEST.largerThanAllowedSize"),
    BAD_REQUEST_MISSING_PARAMETERS("BAD_REQUEST.missingParameters"),
    BAD_REQUEST_MISSING_PARAMETERS_TO_GET_CATEGORIES("BAD_REQUEST.missingParametersToGetCategories"),
    BAD_REQUEST_MULTIPLE_PRODUCTS_IN_TEMPLATE("BAD_REQUEST.multipleProductsInTemplate"),
    BAD_REQUEST_NO_BINARY_CONTENT_FOR_THE_FILE("BAD_REQUEST.noBinaryContentForTheFile"),
    BAD_REQUEST_NO_COMMODITY_CLASSIFICATION("BAD_REQUEST.noCommodityClassification"),
    BAD_REQUEST_MISSING_REQUIRED_PROPERTIES("BAD_REQUEST.missingRequiredProperties"),
    BAD_REQUEST_MIXED_COMMODITY_CLASSIFICATION("BAD_REQUEST.mixedCommodityClassification"),
    BAD_REQUEST_NO_ID_FOR_CATALOGUE("BAD_REQUEST.noIdForCatalogue"),
    BAD_REQUEST_NO_ID_FOR_LINE("BAD_REQUEST.noIdForLine"),
    BAD_REQUEST_NO_MANUFACTURER_ITEM_IDENTIFICATION("BAD_REQUEST.noManufacturerItemIdentification"),
    BAD_REQUEST_NO_MANUFACTURER_PARTY("BAD_REQUEST.noManufacturerParty"),
    BAD_REQUEST_MULTIPLE_VALUES_FOR_THE_PROPERTY("BAD_REQUEST.multipleValuesForTheProperty"),
    BAD_REQUEST_NO_NAME_FOR_LINE("BAD_REQUEST.noNameForLine"),
    BAD_REQUEST_NO_NAME_FOR_ITEM("BAD_REQUEST.noNameForItem"),
    BAD_REQUEST_MULTIPLE_NAME_FOR_SAME_LANGUAGE_ID("BAD_REQUEST.multipleNamesForSameLanguageId"),
    BAD_REQUEST_NO_TRADING_DELIVERY_TERMS_FOR_ITEM("BAD_REQUEST.noTradingDeliveryTermsForItem"),
    BAD_REQUEST_NO_UPDATE_OPERATION_FOR_STANDARD("BAD_REQUEST.noUpdateOperationForStandard"),
    BAD_REQUEST_NUMBER_REQUIRED_FOR_PROPERTY("BAD_REQUEST.numberRequiredForProperty"),
    BAD_REQUEST_PARSE_LCPA_OUTPUT("BAD_REQUEST.parseLCPAOutput"),
    BAD_REQUEST_PARTY_IDS_DO_NOT_MATCH("BAD_REQUEST.partyIdsDoNotMatch"),
    BAD_REQUEST_PRECEDING_TRAILING_IN_ID("BAD_REQUEST.precedingTrailingSpaceInId"),
    BAD_REQUEST_PRODUCT_WITH_DIFFERENT_CATEGORIES("BAD_REQUEST.productWithDifferentCategories"),
    BAD_REQUEST_UNIT_REQUIRED_FOR_DELIVERY_PERIOD("BAD_REQUEST.unitRequiredForDeliveryPeriod"),
    BAD_REQUEST_UNIT_REQUIRED_FOR_MINIMUM_ORDER_QUANTITY("BAD_REQUEST.unitRequiredForMinimumOrderQuantity"),
    BAD_REQUEST_BASE_QUANTITY_REQUIRED("BAD_REQUEST.baseQuantityRequired"),
    BAD_REQUEST_SAME_UNIT_REQUIRED_FOR_BASE_AND_MINIMUM_ORDER_QUANTITIES("BAD_REQUEST.sameUnitRequiredForBaseAndMinimumOrderQuantities"),
    BAD_REQUEST_UNIT_REQUIRED_FOR_PACKAGE_QUANTITY("BAD_REQUEST.unitRequiredForPackageQuantity"),
    BAD_REQUEST_UNIT_REQUIRED_FOR_WARRANTY_PERIOD("BAD_REQUEST.unitRequiredForWarrantyPeriod"),
    BAD_REQUEST_UPLOAD_IMAGES("BAD_REQUEST.uploadImages"),
    BAD_REQUEST_INVALID_IMAGE_SIZE("BAD_REQUEST.invalidImageSize"),
    BAD_REQUEST_VALUE_AND_UNIT_REQUIRED_FOR_PROPERTY("BAD_REQUEST.valueAndUnitRequiredForProperty"),
    BAD_REQUEST_WRONG_DATA_TYPE_FOR_CUSTOM_PROPERTIES("BAD_REQUEST.wrongDataTypeForCustomProperties"),
    BAD_REQUEST_WRONG_DATA_TYPE_FOR_QUANTITY("BAD_REQUEST.wrongDataTypeForQuantity"),
    CONFLICT_CATALOGUE_ID_ALREADY_EXISTS("CONFLICT.catalogueIdAlreadyExists"),
    CONFLICT_UNIT_EXISTS("CONFLICT.unitExists"),
    CONFLICT_UNIT_LIST_EXISTS("CONFLICT.unitListExists"),
    FORBIDDEN_ACCESS_CATALOGUE("FORBIDDEN.accessCatalogue"),
    FORBIDDEN_ACCESS_CATALOGUE_BY_ID("FORBIDDEN.accessCatalogueById"),
    FORBIDDEN_ACCESS_CATALOGUE_LINE("FORBIDDEN.accessCatalogueLine"),
    FORBIDDEN_ACCESS_CATALOGUE_LINE_BY_HJID("FORBIDDEN.accessCatalogueLineByHjid"),
    FORBIDDEN_ACCESS_CATALOGUE_LINES("FORBIDDEN.accessCatalogueLines"),
    GATEWAY_TIMEOUT_WAITING_FOR_ANOTHER_SERVER("GATEWAY_TIMEOUT.waitingForAnotherServer"),
    INTERNAL_SERVER_ERROR_ADD_CATALOGUE("INTERNAL_SERVER_ERROR.addCatalogue"),
    INTERNAL_SERVER_ERROR_ADD_CATALOGUE_LINE("INTERNAL_SERVER_ERROR.addCatalogueLine"),
    INTERNAL_SERVER_ERROR_ADD_LCPA_OUTPUT("INTERNAL_SERVER_ERROR.addLCPAOutput"),
    INTERNAL_SERVER_ERROR_ADD_PRICE_OPTION("INTERNAL_SERVER_ERROR.addPriceOption"),
    INTERNAL_SERVER_ERROR_CATALOGUE_PAGINATION_RESPONSE("INTERNAL_SERVER_ERROR.cataloguePaginationResponse"),
    INTERNAL_SERVER_ERROR_CHANGE_PRODUCT_STATUS_FOR_CATALOGUES("INTERNAL_SERVER_ERROR.changeProductStatusForCatalogues"),
    INTERNAL_SERVER_ERROR_DELETE_CATALOGUE("INTERNAL_SERVER_ERROR.deleteCatalogue"),
    INTERNAL_SERVER_ERROR_DELETE_CATALOGUES("INTERNAL_SERVER_ERROR.deleteCatalogues"),
    INTERNAL_SERVER_ERROR_DELETE_CATALOGUE_LINE("INTERNAL_SERVER_ERROR.delegateCatalogueLine"),
    INTERNAL_SERVER_ERROR_DELETE_IMAGES("INTERNAL_SERVER_ERROR.deleteImages"),
    INTERNAL_SERVER_ERROR_DELETE_PRICE_OPTION("INTERNAL_SERVER_ERROR.deletePriceOption"),
    INTERNAL_SERVER_ERROR_DOWNLOAD_BOM_TEMPLATE("INTERNAL_SERVER_ERROR.downloadBOMTemplate"),
    INTERNAL_SERVER_ERROR_EXPORT_CATALOGUE("INTERNAL_SERVER_ERROR.exportCatalogue"),
    INTERNAL_SERVER_ERROR_FAILED_TO_GET_CATALOGUE("INTERNAL_SERVER_ERROR.failedToGetCatalogue"),
    INTERNAL_SERVER_ERROR_FAILED_TO_GET_CATALOGUES("INTERNAL_SERVER_ERROR.failedToGetCatalogues"),
    INTERNAL_SERVER_ERROR_FAILED_TO_GET_CATALOGUE_FOR_STANDARD("INTERNAL_SERVER_ERROR.failedToGetCatalogueForStandard"),
    INTERNAL_SERVER_ERROR_FAILED_TO_GET_PARTY("INTERNAL_SERVER_ERROR.failedToGetParty"),
    INTERNAL_SERVER_ERROR_FAILED_TO_GET_NEXT_ENTRY("INTERNAL_SERVER_ERROR.failedToGetNextEntry"),
    INTERNAL_SERVER_ERROR_GENERATE_TEMPLATE("INTERNAL_SERVER_ERROR.generateTemplate"),
    INTERNAL_SERVER_ERROR_GENERATE_URI_FOR_ITEM("INTERNAL_SERVER_ERROR.generateUriForItem"),
    INTERNAL_SERVER_ERROR_GET_AVAILABLE_TAXONOMIES("INTERNAL_SERVER_ERROR.getAvailableTaxonomies"),
    INTERNAL_SERVER_ERROR_GET_BASE_64_BINARY_CONTENT("INTERNAL_SERVER_ERROR.getBase64BinaryContent"),
    INTERNAL_SERVER_ERROR_GET_BINARY_CONTENT("INTERNAL_SERVER_ERROR.getBinaryContent"),
    INTERNAL_SERVER_ERROR_GET_BINARY_CONTENTS("INTERNAL_SERVER_ERROR.getBinaryContents"),
    INTERNAL_SERVER_ERROR_GET_CATALOGUE_LINE("INTERNAL_SERVER_ERROR.getCatalogueLine"),
    INTERNAL_SERVER_ERROR_GET_CATALOGUE_LINES("INTERNAL_SERVER_ERROR.getCatalogueLines"),
    INTERNAL_SERVER_ERROR_GET_CATALOGUE_LINE_WITH_LINE_AND_CATALOGUE_ID("INTERNAL_SERVER_ERROR.getCatalogueLineWithLineAndCatalogueId"),
    INTERNAL_SERVER_ERROR_GET_CATALOGUE_IDS("INTERNAL_SERVER_ERROR.getCatalogueIDs"),
    INTERNAL_SERVER_ERROR_GET_COLLABORATION_GROUP("INTERNAL_SERVER_ERROR.getCollaborationGroup"),
    INTERNAL_SERVER_ERROR_GET_CONTRACT_FOR_CATALOGUE("INTERNAL_SERVER_ERROR.getContractForCatalogue"),
    INTERNAL_SERVER_ERROR_GET_DIGITAL_AGREEMENT_FOR_PARTIES_AND_PRODUCT("INTERNAL_SERVER_ERROR.getDigitalAgreementForPartiesAndProduct"),
    INTERNAL_SERVER_ERROR_GET_FILE_IN_BASE_64_ENCODING("INTERNAL_SERVER_ERROR.getFileInBase64Encoding"),
    INTERNAL_SERVER_ERROR_GET_MULTIPLE_CATALOGUE_LINES("INTERNAL_SERVER_ERROR.getMultipleCatalogueLines"),
    INTERNAL_SERVER_ERROR_GET_PROCESS_COUNT("INTERNAL_SERVER_ERROR.getProcessCount"),
    INTERNAL_SERVER_ERROR_GET_PRODUCTS_WITHOUT_LCPA_PROCESSING("INTERNAL_SERVER_ERROR.getProductsWithoutLCPAProcessing"),
    INTERNAL_SERVER_ERROR_GET_PRODUCT_AND_SERVICE_COUNT("INTERNAL_SERVER_ERROR.getProductAndServiceCount"),
    INTERNAL_SERVER_ERROR_GET_STANDARDS("INTERNAL_SERVER_ERROR.getStandards"),
    INTERNAL_SERVER_ERROR_GET_VAT_RATES("INTERNAL_SERVER_ERROR.getVatRates"),
    INTERNAL_SERVER_ERROR_IMPORT_CATALOGUE("INTERNAL_SERVER_ERROR.importCatalogue"),
    INTERNAL_SERVER_ERROR_INDEX_ECLASS_CATEGORIES("INTERNAL_SERVER_ERROR.indexEclassCategories"),
    INTERNAL_SERVER_ERROR_INDEX_ECLASS_PROPERTIES("INTERNAL_SERVER_ERROR.indexEclassProperties"),
    INTERNAL_SERVER_ERROR_INDEX_ECLASS_RESOURCES("INTERNAL_SERVER_ERROR.indexEclassResources"),
    INTERNAL_SERVER_ERROR_NO_AVAILABLE_RESOURCE("INTERNAL_SERVER_ERROR.noAvailableResource"),
    INTERNAL_SERVER_ERROR_READ_TEMPLATE("INTERNAL_SERVER_ERROR.readTemplate"),
    INTERNAL_SERVER_ERROR_SET_CONTRACT_FOR_CATALOGUE("INTERNAL_SERVER_ERROR.setContractForCatalogue"),
    INTERNAL_SERVER_ERROR_START_PROCESS("INTERNAL_SERVER_ERROR.startProcess"),
    INTERNAL_SERVER_ERROR_UNEXPECTED_ERROR_WHILE_UPDATING_CATALOGUE("INTERNAL_SERVER_ERROR.unExpectedErrorWhileUpdatingCatalogue"),
    INTERNAL_SERVER_ERROR_UNEXPECTED_ERROR_WHILE_ADDING_WHITE_BLACK_LIST("INTERNAL_SERVER_ERROR.unExpectedErrorWhileAddingWhiteBlackList"),
    INTERNAL_SERVER_ERROR_UNEXPECTED_ERROR_WHILE_UPLOADING_IMAGES("INTERNAL_SERVER_ERROR.unExpectedErrorWhileUploadingImages"),
    INTERNAL_SERVER_ERROR_UPDATE_CATALOGUE("INTERNAL_SERVER_ERROR.updateCatalogue"),
    INTERNAL_SERVER_ERROR_UPDATE_CATALOGUE_LINE("INTERNAL_SERVER_ERROR.updateCatalogueLine"),
    INTERNAL_SERVER_ERROR_UPDATE_PRICE_OPTION("INTERNAL_SERVER_ERROR.updatePriceOption"),
    INTERNAL_SERVER_ERROR_HIDE_PRICE("INTERNAL_SERVER_ERROR.hidePrice"),
    INTERNAL_SERVER_ERROR_UPLOAD_IMAGES("INTERNAL_SERVER_ERROR.uploadImages"),
    INTERNAL_SERVER_ERROR_OFFER_PRODUCT_DETAILS("INTERNAL_SERVER_ERROR.offerProductDetails"),
    INTERNAL_SERVER_ERROR_REQUEST_CATALOGUE_EXCHANGE("INTERNAL_SERVER_ERROR.requestCatalogueExchange"),
    INTERNAL_SERVER_ERROR_UPLOAD_TEMPLATE("INTERNAL_SERVER_ERROR.uploadTemplate"),
    INTERNAL_SERVER_ERROR_WRITE_BINARY_CONTENT_TO_OUTPUT_STREAM("INTERNAL_SERVER_ERROR.writeBinaryContentToOutputStream"),
    INTERNAL_SERVER_ERROR_WRITE_CATALOGUE_CONTENT_TO_OUTPUT_STREAM("INTERNAL_SERVER_ERROR.writeCatalogueContentToOutputStream"),
    INTERNAL_SERVER_ERROR_WRITE_TEMPLATE_CONTENT_TO_OUTPUT_STREAM("INTERNAL_SERVER_ERROR.writeTemplateContentToOutputStream"),
    NOT_ACCEPTABLE_ALREADY_EXISTS("NOT_ACCEPTABLE.alreadyExists"),
    NOT_FOUND_NO_BINARY_CONTENT("NOT_FOUND.noBinaryContent"),
    NOT_FOUND_NO_CATALOGUE("NOT_FOUND.noCatalogue"),
    NOT_FOUND_NO_CATALOGUE_FOR_PARTY("NOT_FOUND.noCatalogueForParty"),
    NOT_FOUND_NO_CATALOGUE_LINE("NOT_FOUND.noCatalogueLine"),
    NOT_FOUND_NO_CATALOGUE_LINE_FOR_HJID("NOT_FOUND.noCatalogueLineForHjid"),
    NOT_FOUND_NO_CATALOGUE_LINE_WITH_ID("NOT_FOUND.noCatalogueLineWithId"),
    NOT_FOUND_NO_CATEGORY("NOT_FOUND.noCategory"),
    NOT_FOUND_NO_DEFAULT_CATALOGUE("NOT_FOUND.noDefaultCatalogue"),
    NOT_FOUND_NO_PRICE_OPTION("NOT_FOUND.noPriceOption"),
    NOT_FOUND_NO_UNIT("NOT_FOUND.noUnit"),
    NOT_FOUND_NO_UNIT_LIST("NOT_FOUND.noUnitList"),
    UNAUTHORIZED_ADD_MISSING_PARENT_CATEGORIES("UNAUTHORIZED.addMissingParentCategories"),
    UNAUTHORIZED_CREATE_VAT_FOR_PRODUCTS("UNAUTHORIZED.createVATForProducts"),
    UNAUTHORIZED_DELETE_INVALID_PRODUCTS("UNAUTHORIZED.deleteInvalidProducts"),
    UNAUTHORIZED_GET_PRODUCTS_WITH_MISSING_PARENT_CATEGORIES("UNAUTHORIZED.getProductsWithMissingParentCategories"),
    UNAUTHORIZED_INDEX_CATALOGUES("UNAUTHORIZED.indexCatalogues"),
    UNAUTHORIZED_INDEX_ECLASS_CATEGORIES("UNAUTHORIZED.indexEClassCategories"),
    UNAUTHORIZED_INDEX_ECLASS_PROPERTIES("UNAUTHORIZED.indexEClassProperties"),
    UNAUTHORIZED_INDEX_ECLASS_RESOURCES("UNAUTHORIZED.indexEClassResources"),
    UNAUTHORIZED_INDEX_UBL_PROPERTIES("UNAUTHORIZED.indexUBLProperties"),
    UNAUTHORIZED_INVALID_ROLE("UNAUTHORIZED.invalidRole"),
    UNAUTHORIZED_NO_USER_FOR_TOKEN("UNAUTHORIZED.noUserForToken")
    ;

    private String value;

    NimbleExceptionMessageCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}