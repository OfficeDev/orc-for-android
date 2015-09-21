package com.microsoft.services.odata.unittests;

import com.microsoft.sampleservice.AnotherEntity;
import com.microsoft.sampleservice.Item;
import com.microsoft.sampleservice.ItemA;
import com.microsoft.sampleservice.ItemB;
import com.microsoft.sampleservice.SampleComplexType;
import com.microsoft.sampleservice.SampleEntity;
import com.microsoft.sampleservice.fetchers.SampleContainerClient;
import com.microsoft.services.orc.core.Helpers;
import com.microsoft.services.orc.http.Credentials;
import com.microsoft.services.orc.http.impl.OAuthCredentials;
import com.microsoft.services.orc.http.impl.OkHttpTransport;
import com.microsoft.services.orc.resolvers.AuthenticationCredentials;
import com.microsoft.services.orc.resolvers.DependencyResolver;
import com.microsoft.services.orc.serialization.impl.GsonSerializer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


public class SampleServiceTests extends WireMockTestBase {

    Logger logger = LoggerFactory.getLogger(SampleServiceTests.class);

    private String url = "http://localhost:8080";
    private SampleContainerClient client;
    private DependencyResolver resolver;

    public SampleServiceTests() {

        resolver = new DependencyResolver.Builder(
                new OkHttpTransport(), new GsonSerializer(),
                new AuthenticationCredentials() {
                    @Override
                    public Credentials getCredentials() {
                        return new OAuthCredentials("foobartoken");
                    }
                }).build();

        client = new SampleContainerClient(url, resolver);
    }

    @Test
    public void testTwoParamsActionsFirstIsEntityTypeUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsEntityTypePOST.json

        Integer result = null;

        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsEntityType(getSampleEntity(), false)
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testTwoParamsActionsFirstIsComplexTypeUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsComplexTypePOST.json
        Integer result = null;
        SampleComplexType sampleComplexEntity = getSampleComplexType();
        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsComplexType(sampleComplexEntity, false)
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testTwoParamsActionsFirstIsComplexTypeRawUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsCollectionComplexTypePOST.json
        String result = null;
        SampleComplexType sampleComplexEntity = getSampleComplexType();
        String serializedEntity = new String(Helpers.serializeToJsonByteArray(sampleComplexEntity, resolver));
        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsComplexTypeRaw(serializedEntity, "false")
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testTwoParamsActionsFirstIsCollectionEntityTypeUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsCollectionEntityTypePOST.json
        List<SampleEntity> entities = new ArrayList<SampleEntity>();
        SampleEntity entity = new SampleEntity();
        entities.add(entity);
        Integer result = null;
        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsCollectionEntityType(entities, false)
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testTwoParamsActionsFirstIsCollectionEntityTypeRawUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsCollectionEntityTypePOST.json
        List<SampleEntity> entities = new ArrayList<SampleEntity>();
        SampleEntity entity = new SampleEntity();
        entities.add(entity);
        String result = null;

        String serializedEntity = new String(Helpers.serializeToJsonByteArray(entities, resolver));
        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsCollectionEntityTypeRaw(serializedEntity, "false")
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testTwoParamsActionsFirstIsCollectionComplexTypeUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsCollectionComplexTypePOST.json
        List<SampleComplexType> complexTypes = new ArrayList<SampleComplexType>();
        SampleComplexType complexType = getSampleComplexType();
        complexTypes.add(complexType);
        Integer result = null;
        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsCollectionComplexType(complexTypes, false)
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testTwoParamsActionsFirstIsCollectionComplexTypeRawUri() throws ExecutionException, InterruptedException {
        //twoParamsActionsFirstIsCollectionComplexTypePOST.json
        List<SampleComplexType> complexTypes = new ArrayList<SampleComplexType>();
        SampleComplexType complexType = getSampleComplexType();
        complexTypes.add(complexType);

        String serializedEntity = new String(Helpers.serializeToJsonByteArray(complexTypes, resolver));
        String result = null;
        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsCollectionComplexTypeRaw(serializedEntity, "false")
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }
        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testGetNavigationList() throws ExecutionException, InterruptedException {
        //getNavigationsGET.json
        List<AnotherEntity> result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .read()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    public void testGetNavigationItem() throws ExecutionException, InterruptedException {
        //getNavigationItemGET.json
        AnotherEntity result = null;
        try {
            result = client.getMe()
                    .getNavigation("SomeId")
                    .read()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.getSomeString(), is(equalToIgnoringCase(getAnotherEntity().getSomeString())));
    }

    @Test
    public void testGetNavigationItemWithSelect() throws ExecutionException, InterruptedException {
        //getNavigationItemWithSelectGET.json
        AnotherEntity result = null;
        try {
            result = client.getMe()
                    .getNavigation("SomeId")
                    .select("SomeProp, AnotherProp")
                    .read()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.getSomeString(), is(equalToIgnoringCase(getAnotherEntity().getSomeString())));
    }

    @Test
    public void testGetNavigationItemRawWithSelect() throws ExecutionException, InterruptedException {
        //getNavigationItemWithSelectGET.json
        String result = null;
        try {
            result = client.getMe()
                    .getNavigation("SomeId")
                    .select("SomeProp, AnotherProp")
                    .readRaw()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        String expectedResponseString = "{\"SomeString\":\"Some String\",\"Id\":\"3281EC0B-1AEB-49A4-A345-E64D732DA6D3\",\"@odata.type\":\"#Microsoft.SampleService.AnotherEntity\"}";
        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo(expectedResponseString)));
    }

    @Test
    public void testGetNavigationListWithSelectAndTop() throws ExecutionException, InterruptedException {
        //getNavigationsWithSelectAndTopGET.json
        List<AnotherEntity> result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .select("SomeProp, AnotherProp")
                    .top(1)
                    .read()
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    public void testGetNavigationListRawWithSelectAndTop() throws ExecutionException, InterruptedException {
        //getNavigationsWithSelectAndTopGET.json
        String result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .select("SomeProp, AnotherProp")
                    .top(1)
                    .readRaw()
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        String responseContent = "{\"value\" : [{\"SomeString\":\"Some String\",\"Id\":\"3281EC0B-1AEB-49A4-A345-E64D732DA6D3\",\"@odata.type\":\"#Microsoft.SampleService.AnotherEntity\"}]}";
        assertThat(result, is(notNullValue()));
        assertThat(result, is(equalTo(responseContent)));
    }

    @Test
    public void testGetCollectionWithFilterAndExpand() throws ExecutionException, InterruptedException {
        //getCollectionsWithExpandAndFilter.json
        List<SampleEntity> result = null;

        try {
            result = client.getServices()
                    .expand("SomeProp")
                    .filter("SomeProp eq 'SomeString'")
                    .read()
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    public void testGetCollectionWithHeaders() throws ExecutionException, InterruptedException {
        //getCollectionsWithHeaders.json
        List<SampleEntity> result = null;

        try {
            result = client.getServices()
                    .addHeader("Header1", "Value1")
                    .addHeader("Header2", "Value2")
                    .read()
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }
        //dummy comment for testing travis
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    public void testDefaultHeaders() throws ExecutionException, InterruptedException {
        //testDefaultHeaders.json
        List<SampleEntity> result = null;

        try {
            result = client.getServices()
                    .top(99)
                    .read()
                    .get();
        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    public void testDeleteNavigationItem() throws ExecutionException, InterruptedException {
        //deleteNavigationItemDELETE.json
        Object result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .getById("SomeId")
                    .addHeader("If-Match", "*")
                    .delete()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(nullValue()));
    }

    @Test
    public void testAddNavigationItem() throws ExecutionException, InterruptedException {
        //addNavigationItemPOST.json
        AnotherEntity result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .add(getAnotherEntity())
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testUpdateNavigationItem() throws ExecutionException, InterruptedException {
        //updateNavigationItemPATCH.json
        AnotherEntity existingEntity = getAnotherEntity();
        existingEntity.setSomeString("Some Updated String");
        AnotherEntity result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .getById(existingEntity.getId())
                    .addHeader("IsPatch", "yes")
                    .update(existingEntity)
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testPatchUpdateNavigationItem() throws ExecutionException, InterruptedException {
        //updateNavigationItemPOST.json
        AnotherEntity existingEntity = getAnotherEntity();
        existingEntity.setSomeString("Some Updated String");
        AnotherEntity result = null;
        try {
            result = client.getMe()
                    .getNavigations()
                    .getById(existingEntity.getId())
                    .addHeader("IsPatch", "yes")
                    .update(existingEntity, false)
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }


    @Test
    public void testGetNavigationsWithParameters() throws ExecutionException, InterruptedException {
        //getNavigationsWithParameters.json
        List<AnotherEntity> result = null;
        String param1 = "SomeValue";

        try {
            result = client.getMe()
                    .getNavigations()
                    .addParameter("StringParam", param1)
                    .read()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    @Test
    public void testGetAndUpdateNestedEntity() throws ExecutionException, InterruptedException {
        //getSampleEntity.json
        //updateSampleEntityPATCH.json

        String payload = new GsonSerializer().serialize(getSampleEntity());
        logger.info("EntityPayload: " + payload);
        //Get Entity
        SampleEntity result = null;
        SampleEntity updateResponse = null;
        try {
            result = client.getMe()
                    .addHeader("WithNested", "no")
                    .read()
                    .get();

            SampleEntity nestedEntity = getSampleEntity();
            result.setNestedSampleEntity(nestedEntity);

            updateResponse = client.getMe()
                    .addHeader("UpdateNested", "no")
                    .update(result).get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(updateResponse, is(notNullValue()));
    }

    @Test
    public void testGetAndUpdatePropertyInNestedEntity() throws ExecutionException, InterruptedException {
        //Get Entity
        SampleEntity result = null;
        SampleEntity updateResponse = null;
        try {
            result = client.getMe()
                    .addHeader("WithNested", "yes")
                    .read()
                    .get();

            result.getNestedSampleEntity().setDisplayName("New Name");
            updateResponse = client.getMe()
                    .addHeader("UpdateNested", "yes")
                    .update(result).get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(updateResponse, is(notNullValue()));
    }

    @Test
    public void testGetAndUpdatePropertyInList() throws ExecutionException, InterruptedException {
        //getSampleEntityWithNavigationsGET.json
        //updateSampleEntityListPropertyChangedPATCH.json

        //Get Entity
        SampleEntity result = null;
        SampleEntity updateResponse = null;
        try {
            result = client.getMe()
                    .addHeader("WithNavigations", "yes")
                    .read()
                    .get();

            result.getNavigations().get(0).setSomeString("Some New String");
            updateResponse = client.getMe()
                    .addHeader("UpdateNavigations", "yes")
                    .update(result).get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(updateResponse, is(notNullValue()));
    }

    @Test
    public void testGetAndUpdatePropertyInListOf3elements() throws ExecutionException, InterruptedException {
        //getSampleEntityWith3NavigationsGET.json
        //updateSampleEntityListWith3ElementsPropertyChangedPATCH.json

        //Get Entity
        SampleEntity result = null;
        SampleEntity updateResponse = null;
        try {
            result = client.getMe()
                    .addHeader("With3Navigations", "yes")
                    .read()
                    .get();

            result.getNavigations().get(0).setSomeString("Some New String");
            updateResponse = client.getMe()
                    .addHeader("WithNavigations3", "yes")
                    .update(result).get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(updateResponse, is(notNullValue()));
    }

    @Test
    public void testGetListWithDerivedClasses() throws ExecutionException, InterruptedException {
        //getListDerivedClassesGET.json

        //Get Entity
        List<Item> result = null;
        try {
            result = client.getMe()
                    .getItems()
                    .read()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.get(0), instanceOf(ItemA.class));
        assertThat(result.get(1), instanceOf(ItemB.class));
    }

    @Test
    public void testGetItemWithDerivedClasses() throws ExecutionException, InterruptedException {
        //getListDerivedClassesByIdGET.json

        //Get Entity
        Item result = null;
        try {
            result = client.getMe()
                    .getItems()
                    .getById("SomeId")
                    .read()
                    .get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
        assertThat(result, instanceOf(ItemA.class));
    }

    @Test
    public void testCallFunctionWithGet() throws ExecutionException, InterruptedException {
        //Get Entity

        SampleComplexType result = null;
        try {
            result = client.getMe().getNestedSampleEntityCollection().getOperations().someFunction("SomePath").get();

        } catch (Throwable t) {
            logger.error("Error executing test", t);
        }

        assertThat(result, is(notNullValue()));
    }

    private SampleEntity getSampleEntity() {
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setDisplayName("Some Display Name");
        sampleEntity.setEntityKey("Some Entity Key");
        sampleEntity.setId("5C338D75-CB90-4785-8667-CED25B3695BF");
        return sampleEntity;
    }

    private AnotherEntity getAnotherEntity() {
        AnotherEntity anotherEntity = new AnotherEntity();
        anotherEntity.setId("3281EC0B-1AEB-49A4-A345-E64D732DA6D3");
        anotherEntity.setSomeString("Some String");
        return anotherEntity;
    }

    private SampleComplexType getSampleComplexType() {
        SampleComplexType complexType = new SampleComplexType();
        complexType.setName("Some Name");
        complexType.setAnotherProperty("AnotherProperty");
        return complexType;
    }
    // dummy comment for testing travis
}
