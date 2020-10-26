package eu.nimble.service.catalogue;

import eu.nimble.service.catalogue.model.catalogue.CatalogueLineSortOptions;
import eu.nimble.service.catalogue.model.catalogue.CataloguePaginationResponse;
import eu.nimble.service.catalogue.model.statistics.ProductAndServiceStatistics;
import eu.nimble.service.model.ubl.catalogue.CatalogueType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.CatalogueLineType;
import eu.nimble.service.model.ubl.commonaggregatecomponents.PartyType;
import eu.nimble.service.model.ubl.commonbasiccomponents.BinaryObjectType;
import eu.nimble.utility.Configuration;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

public interface CatalogueService {

    public CatalogueType addCatalogue(CatalogueType catalogue);

    public CatalogueType addCatalogue(String catalogueXml);

    public CatalogueType getCatalogue(String uuid);

    public CatalogueType getCatalogue(String id, String partyId);

    public CataloguePaginationResponse getCataloguePaginationResponse(String catalogueId, String partyId, String categoryName,String searchText, String languageId, CatalogueLineSortOptions sortOption, int limit, int offset);

    public CatalogueType updateCatalogue(CatalogueType catalogue);

    public void deleteCatalogue(String uuid);

    public void deleteCatalogue(String id, String partyId);

    public <T> T addCatalogue(String catalogueXML, Configuration.Standard standard);

    public <T> T addCatalogue(T catalogue, Configuration.Standard standard);

    public <T> T addCatalogueWithUUID(T catalogue, Configuration.Standard standard, String uuid);

    public <T> T getCatalogue(String uuid, Configuration.Standard standard);

    public <T> T getCatalogue(String id, String partyId, Configuration.Standard standard);

    public <T> T getCataloguePaginationResponse(String id, String partyId,String categoryName, Configuration.Standard standard,String searchText,String languageId,CatalogueLineSortOptions sortOption, int limit, int offset);

    public void deleteCatalogue(String uuid, Configuration.Standard standard);

    /**
     * Returns the supported standards by this {@link CatalogueService}
     *
     * @return
     */
    public List<Configuration.Standard> getSupportedStandards();

    /**
     * @return
     */
    public Map<Workbook,String> generateTemplateForCatalogue(CatalogueType catalogue, String languageId);

    /**
     * @return
     */
    public Workbook generateTemplateForCategory(List<String> categoryId, List<String> taxonomyIds,String templateLanguage);

    /**
     * Adds the catalogue given through the NIMBLE-specific, Excel-based template.
     *
     * @param catalogueTemplate
     * @param party
     */
    public CatalogueType parseCatalogue(InputStream catalogueTemplate, String uploadMode, PartyType party, Boolean includeVat);

    /**
     * Adds the provided images to the relevant products in the specified catalogue of the specified party
     *
     * @param imagePackage
     * @param catalogueUuid
     * @return
     */
    void addImagesToProducts(ZipInputStream imagePackage, String catalogueUuid);

    CatalogueType removeAllImagesFromCatalogue(CatalogueType catalogueType);

    Map<String,List<BinaryObjectType>> getAllImagesFromCatalogue(CatalogueType catalogue);

    /*
     * Catalogue-line level endpoints
     */

    CatalogueLineType getCatalogueLine(long hjid);

    List<CatalogueLineType> getCatalogueLines(List<Long> hjids,CatalogueLineSortOptions sortOption,int limit, int pageNo);

    <T> T getCatalogueLine(String catalogueId, String catalogueLineId);

    <T> T getCatalogueLines(String catalogueId, List<String> catalogueLineIds);

    CatalogueLineType addLineToCatalogue(String catalogueUuid, String catalogueProviderId, CatalogueLineType catalogueLine);

    CatalogueLineType updateLinesCatalogue(String newCatalogueUuid, String oldeCatalogueUuid,CatalogueLineType catalogueLin);

    CatalogueLineType updateCatalogueLine(CatalogueLineType catalogueLine);

    void deleteCatalogueLineById(String catalogueId, String lineId);

    List<String> getCatalogueIdsForParty(String partyId);

    List<Object[]> getCatalogueIdAndNameForParty(String partyId);

    ProductAndServiceStatistics getProductAndServiceCount();


}
