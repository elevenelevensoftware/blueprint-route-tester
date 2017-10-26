package osgi;

import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import java.util.Locale;

public class RouteTest extends CamelBlueprintTestSupport {

    private static final String COMPANY_MESSAGE = "<company><country>%s</country></company>";
    private static final String MOCK_DIRECT_ENDPOINT = "mock:direct:";
    private static final String DIRECT_INPUT_ENDPOINT = "direct:input";

    private static final int JUST_ONE_MESSAGE = 1;

    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/route.xml";
    }

    @Override
    public String isMockEndpointsAndSkip() {
        return "((direct:error)|(direct:gb)|(direct:us)|(direct:fr)|(direct:it))";
    }

    @Test
    public void should_route_to_gb_endpoint() throws InterruptedException {
        assertMockEndpointCalledForLocale(Locale.UK);
    }

    @Test
    public void should_route_to_usa_endpoint() throws Exception {
        assertMockEndpointCalledForLocale(Locale.US);
    }

    @Test
    public void should_route_to_italy_endpoint() throws Exception {
        assertMockEndpointCalledForLocale(Locale.ITALY);
    }

    @Test
    public void should_route_to_france_endpoint() throws Exception {
        assertMockEndpointCalledForLocale(Locale.FRANCE);
    }

    private void assertMockEndpointCalledForLocale(Locale locale) throws InterruptedException {

        MockEndpoint mockEndpoint = getMockEndpoint(MOCK_DIRECT_ENDPOINT + locale.getCountry().toLowerCase());
        mockEndpoint.expectedMessageCount(JUST_ONE_MESSAGE);
        template.sendBody(DIRECT_INPUT_ENDPOINT, String.format(COMPANY_MESSAGE, locale.getCountry()));

        assertMockEndpointsSatisfied();
    }
}
