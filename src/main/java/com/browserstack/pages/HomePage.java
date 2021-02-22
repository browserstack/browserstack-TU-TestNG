package com.browserstack.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {

    @FindBy(id = "signin")
    private WebElement signInLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public SignInPage navigateToSignIn() {
        signInLink.click();
        return new SignInPage(driver);
    }
}
