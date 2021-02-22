package com.browserstack.plain;

import com.browserstack.TestBase;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

public class OrderTest extends TestBase {

    @Test
    public void orderProduct() {
        getDriver().findElement(By.xpath("//p[text() = 'iPhone 12']/../div[@class = 'shelf-item__buy-btn']")).click();
        wait.until(ExpectedConditions.textToBePresentInElement(getDriver().findElement(By.cssSelector(".float-cart .title")), "iPhone 12"));
        getDriver().findElement(By.className("float-cart__close-btn")).click();

        getDriver().findElement(By.xpath("//p[text() = 'iPhone XS']/../div[@class = 'shelf-item__buy-btn']")).click();
        getDriver().findElement(By.className("float-cart__close-btn")).click();

        getDriver().findElement(By.xpath("//p[text() = 'Galaxy S20']/../div[@class = 'shelf-item__buy-btn']")).click();
        getDriver().findElement(By.className("buy-btn")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#username input")));
        getDriver().findElement(By.cssSelector("#username input")).sendKeys("fav_user" + Keys.ENTER);
        getDriver().findElement(By.cssSelector("#password input")).sendKeys("testingisfun99" + Keys.ENTER);
        getDriver().findElement(By.id("login-btn")).click();

        getDriver().findElement(By.id("firstNameInput")).sendKeys("firstname");
        getDriver().findElement(By.id("lastNameInput")).sendKeys("lastname");
        getDriver().findElement(By.id("addressLine1Input")).sendKeys("address");
        getDriver().findElement(By.id("provinceInput")).sendKeys("state");
        getDriver().findElement(By.id("postCodeInput")).sendKeys("12345");

        getDriver().findElement(By.id("checkout-shipping-continue")).click();

        boolean isConfirmationTextPresent = wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("confirmation-message"), "Your Order has been successfully placed."));
        Assertions.assertThat(isConfirmationTextPresent).isTrue();
    }

}
