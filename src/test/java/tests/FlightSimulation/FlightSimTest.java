package tests.FlightSimulation;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FlightSimTest {
    Page page;

    @BeforeMethod
    public void setup() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
//        Browser browser = playwright.firefox().launch();
        page = browser.newPage();
        page.setDefaultTimeout(5000);
        page.navigate("https://mbutijn.github.io/FlightSimulationWind/index.html");
        page.waitForTimeout(1000);
    }

    @Test(description = "Move the mouse to control the pitch angle of the aircraft")
    public void ManualPilotElevatorToPitchTest() {
        // Go to mouse navigation mode
        page.mouse().click(500, 500);
        page.mouse().click(500, 500);

        int beginTime = (int) System.currentTimeMillis();
        int time = (int) System.currentTimeMillis();

        while (time - beginTime < 15000) {
            time = (int) System.currentTimeMillis();
            page.mouse().move(500, 400 + 200 * Math.sin(time * 0.001));
        }
    }

    @Test (description = "Push the throttle to control the speed of the aircraft")
    public void ManualPilotThrottleToSpeedTest() {
        // Maximum throttle
        page.keyboard().down("PageUp");
        page.waitForTimeout(15000);
        page.keyboard().up("PageUp");

    }
}
