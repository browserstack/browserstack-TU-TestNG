package com.browserstack;

import com.browserstack.local.Local;
import com.browserstack.utils.OsUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.FileReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Listeners({ScreenshotListener.class})
public class TestBase {
    // ThreadLocal gives the ability to store data individually for the current thread
    protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private Local local;
    protected WebDriverWait wait;
    protected String chromeDriverBaseLocation = Path.of(System.getProperty("user.dir"), "/src/test/resources/chromeDriver").toString();

    public WebDriver getDriver() {
        return driver.get();
    }

    @BeforeMethod
    @Parameters(value = {"configfile", "environment", "browser", "url"})
    public void setUp(@Optional("browserstack.conf.json") String configfile, @Optional("local") String environment, @Optional("chrome") String browser, @Optional("http://localhost:3000") String url) throws Exception {
        if (environment.equalsIgnoreCase("local")) {
            if (OsUtils.isMac()) {
                System.setProperty("webdriver.chrome.driver", Path.of(chromeDriverBaseLocation, "/chromeDriverMac/chromedriver").toString());
            }
            if (OsUtils.isWindows()) {
                System.setProperty("webdriver.chrome.driver", Path.of(chromeDriverBaseLocation, "/chromeDriverWin/chromedriver.exe").toString());
            }
            if (OsUtils.isUnix()) {
                System.setProperty("webdriver.chrome.driver", Path.of(chromeDriverBaseLocation, "/chromeDriverLinux/chromedriver").toString());
            }
            driver.set(new ChromeDriver());
        } else if (environment.equalsIgnoreCase("remote")) {
            JSONParser parser = new JSONParser();
            JSONObject config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + configfile));
            JSONObject envs = (JSONObject) config.get("environments");

            DesiredCapabilities capabilities = new DesiredCapabilities();

            Map<String, String> envCapabilities = (Map<String, String>) envs.get(browser);
            Iterator it = envCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
            }

            Map<String, String> commonCapabilities = (Map<String, String>) config.get("capabilities");
            it = commonCapabilities.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if (capabilities.getCapability(pair.getKey().toString()) == null) {
                    capabilities.setCapability(pair.getKey().toString(), pair.getValue().toString());
                }
            }

            String username = System.getenv("BROWSERSTACK_USERNAME");
            if (username == null) {
                username = (String) config.get("user").toString();
            }

            String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");
            if (accessKey == null) {
                accessKey = (String) config.get("key").toString();
            }

            capabilities.setCapability("browserstack.appiumLogs", "false");
            capabilities.setCapability("browserstack.seleniumLogs", "false");
            capabilities.setCapability("browserstack.geoLocation", "IN");

            if (capabilities.getCapability("browserstack.local") != null
                    && capabilities.getCapability("browserstack.local") == "true") {
                local = new Local();
                UUID uuid = UUID.randomUUID();
                capabilities.setCapability("browserstack.localIdentifier", uuid.toString());
                Map<String, String> options = new HashMap<String, String>();
                options.put("key", accessKey);
                options.put("localIdentifier", uuid.toString());
                local.start(options);
            }

            driver.set(new RemoteWebDriver(new URL("https://" + username + ":" + accessKey + "@" + config.get("server") + "/wd/hub"), capabilities));
        } else if (environment.equalsIgnoreCase("docker")) {
            DesiredCapabilities dc = new DesiredCapabilities();
            dc.setBrowserName("chrome");
            dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc));
        }
        getDriver().get(url);
        wait = new WebDriverWait(getDriver(), 50);
        getDriver().manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        if (driver != null) {
            getDriver().quit();
        }
        if (local != null) {
            local.stop();
        }
    }

}
