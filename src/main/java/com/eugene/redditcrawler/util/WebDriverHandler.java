package com.eugene.redditcrawler.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class WebDriverHandler {

    public static void scrollToBottom(WebDriver driver) {
        // Cast WebDriver to JavascriptExecutor
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // Execute JavaScript code to scroll to the bottom of the page
        jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    public static void waitForPageToLoad(WebDriver driver, long seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds)); // Adjust the timeout as needed
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete';"));
    }
}
