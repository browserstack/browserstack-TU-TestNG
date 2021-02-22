package com.browserstack.plain;

import com.browserstack.TestBase;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FavouriteTest extends TestBase {

    @Test
    public void accountWithFavourites() {
        getDriver().findElement(By.id("signin")).click();
        getDriver().findElement(By.cssSelector("#username input")).sendKeys("fav_user" + Keys.ENTER);
        getDriver().findElement(By.cssSelector("#password input")).sendKeys("testingisfun99" + Keys.ENTER);
        getDriver().findElement(By.id("login-btn")).click();

        wait.until(ExpectedConditions.elementToBeClickable(getDriver().findElement(By.id("favourites")))).click();

        wait.until(ExpectedConditions.urlContains("favourites"));

        Assert.assertEquals(getDriver().findElements(By.className("shelf-item")).size(), 5);
    }

    @Test
    public void addToFavourites() {
        getDriver().findElement(By.id("signin")).click();
        getDriver().findElement(By.cssSelector("#username input")).sendKeys("existing_orders_user" + Keys.ENTER);
        getDriver().findElement(By.cssSelector("#password input")).sendKeys("testingisfun99" + Keys.ENTER);
        getDriver().findElement(By.id("login-btn")).click();

        getDriver().findElement(By.xpath("//p[text() = 'iPhone 12']/../div/button")).click();

        getDriver().findElement(By.id("favourites")).click();
        wait.until(ExpectedConditions.urlContains("favourites"));

        Assertions.assertThat(getDriver().findElements(By.cssSelector("p.shelf-item__title"))
                .stream().map(WebElement::getText)).contains("iPhone 12");
    }
}
