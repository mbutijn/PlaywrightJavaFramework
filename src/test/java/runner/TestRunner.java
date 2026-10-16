package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import stepDefinitions.CommonMethods;
import stepDefinitions.StepDefinitions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/feature/",
        plugin = {"pretty", "html:target/cucmber-html.report"},
        glue = "stepDefinitions",
        tags = "@verifyLoginToWebsite or @verifyCreateEvent"
        //tags = "@verifyLoginToWebsite"
)

public class TestRunner {
    @BeforeClass
    public static void beforeAll() {
        StepDefinitions.go_to_site();
    }

    @AfterClass
    public static void afterAll() throws InterruptedException {
        StepDefinitions.quitBrowser();
    }
}
