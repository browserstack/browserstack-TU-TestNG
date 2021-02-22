package com.browserstack.pages;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SignInPage extends BasePage {

    @FindBy(css = "#username input")
    private WebElement usernameInput;

    @FindBy(css = "#password input")
    private WebElement passwordInput;

    @FindBy(id = "login-btn")
    private WebElement logInButton;

    @FindBy(css = ".username")
    private WebElement signedInUsername;

    public SignInPage(WebDriver driver) {
        super(driver);
    }

    public SignInPage loginWith(String username, String password) {
        usernameInput.sendKeys(username + Keys.ENTER);
        passwordInput.sendKeys(password + Keys.ENTER);
        logInButton.click();
        return this;
    }

    public String getSignedInUsername() {
        return signedInUsername.getText();
    }
}
