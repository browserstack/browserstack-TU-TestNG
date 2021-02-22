package com.browserstack.pom;

import com.browserstack.pages.HomePage;
import com.browserstack.pages.SignInPage;
import com.browserstack.utils.OsUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class LoginTest {

    private WebDriver driver;
    protected String chromeDriverBaseLocation = Path.of(System.getProperty("user.dir"), "/src/test/resources/chromeDriver").toString();

    @BeforeMethod
    public void setUp() {
        if (OsUtils.isMac()) {
            System.setProperty("webdriver.chrome.driver", Path.of(chromeDriverBaseLocation, "/chromeDriverMac/chromedriver").toString());
        }
        if (OsUtils.isWindows()) {
            System.setProperty("webdriver.chrome.driver", Path.of(chromeDriverBaseLocation, "/chromeDriverWin/chromedriver.exe").toString());
        }
        if (OsUtils.isUnix()) {
            System.setProperty("webdriver.chrome.driver", Path.of(chromeDriverBaseLocation, "/chromeDriverLinux/chromedriver").toString());
        }
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("http://localhost:3000");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void login() {
        SignInPage page = new HomePage(driver)
                .navigateToSignIn()
                .loginWith("fav_user", "testingisfun99");
        Assert.assertEquals(page.getSignedInUsername(), "fav_user");
    }
}