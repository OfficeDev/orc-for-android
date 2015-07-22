package com.microsoft.services.odata.unittests;

import com.microsoft.sampleservice.SampleEntity;
import com.microsoft.sampleservice.fetchers.SampleContainerClient;
import com.microsoft.services.orc.core.DependencyResolver;
import com.microsoft.services.orc.log.LogLevel;
import com.microsoft.services.orc.resolvers.OkHttpDependencyResolver;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class OkHttpTests {
    private SampleContainerClient client;
    private DependencyResolver resolver;


    public OkHttpTests() {

    }

    @Test
    public void canRunTests() throws Exception {

        MockWebServer server = new MockWebServer();
        server.start();

        resolver = new OkHttpDependencyResolver("footoken");
        client = new SampleContainerClient(server.getUrl("/").toString(), resolver);

        Integer result = null;

        try {
            result = client.getMe()
                    .getOperations()
                    .twoParamsActionsFirstIsEntityType(getSampleEntity(), false)
                    .get();

        } catch (Throwable t) {
            resolver.getLogger().log(t.getLocalizedMessage(), LogLevel.ERROR);
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


}
