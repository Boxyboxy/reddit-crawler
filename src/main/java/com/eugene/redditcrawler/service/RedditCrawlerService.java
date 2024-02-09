package com.eugene.redditcrawler.service;

import com.eugene.redditcrawler.db.entity.RedditPost;
import com.eugene.redditcrawler.util.WebDriverHandler;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Log4j2
@Service
public class RedditCrawlerService {
    private static Logger logger = LogManager.getLogger(RedditCrawlerService.class);

    public static void test(String subreddit) throws IOException {
        try{
            String redditUrl = String.format("https://www.reddit.com/r/%s/top/?t=day", subreddit);
            logger.info("test() - redditUrl: {}", redditUrl);
            Document document = Jsoup.connect(redditUrl).get();
            Elements shRedditPosts = document.select("shreddit-post");
            logger.info("test() - Length of elements: {}", shRedditPosts.size());
            // Iterate through the selected elements
            for (Element shRedditPost : shRedditPosts) {
                // Print or process each shreddit-post element
                logger.info("Shreddit Post: {}", shRedditPost);
            }
        } catch (IOException e){
            logger.error("test() - Exception: {}" , e.getMessage());
            throw e;
        }
    }

    public static void test2(String subreddit) {
        String redditUrl = String.format("https://www.reddit.com/r/%s/top/?feedViewType=classicView", subreddit);
        logger.info("test2() - redditUrl: {}", redditUrl);
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        // Create Thenan instance of ChromeDriver
        WebDriver webDriver = new ChromeDriver();

        webDriver.get(redditUrl);

        WebDriverHandler webDriverHandler = new WebDriverHandler();
        // Cast WebDriver to JavascriptExecutor
        webDriverHandler.waitForPageToLoad(webDriver, 30);
        webDriverHandler.scrollToBottom(webDriver);
        // Delay for 10 seconds
        try {
            Thread.sleep(15000); // 10 seconds in milliseconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // Handle the exception if needed
        }
        webDriverHandler.waitForPageToLoad(webDriver, 30);
        String pageSource = webDriver.getPageSource();
        webDriver.quit();
        //logger.info("{}",pageSource);
        Document document = Jsoup.parse(pageSource);


        Elements shRedditPosts = document.select("shreddit-post");
        logger.info("test2() - Length of elements: {}", shRedditPosts.size());
        // Iterate through the selected elements
        for (Element shRedditPost : shRedditPosts) {
            // Print or process each shreddit-post element


            String title = shRedditPost.attr("post-title");
            String author = shRedditPost.attr("author");
            String upvotes = shRedditPost.attr("score");
            String comments = shRedditPost.attr("comment-count");
            String link =  "https://www.reddit.com" + shRedditPost.attr("permalink");
            String creationDatetime = shRedditPost.attr("created-timestamp");
            String retrievalDatetime = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            logger.info("title={}, author={}, upvotes={}, comments={}, link={}, creationDatetime={}, retrievalDatetime={}", title, author, upvotes, comments, link, creationDatetime, retrievalDatetime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            LocalDateTime creationDateTime2 =LocalDateTime.parse(shRedditPost.attr("created-timestamp").substring(0,26), formatter);
            ZoneId zoneId = ZoneId.of("UTC");
            ZonedDateTime currentZonedDateTime = ZonedDateTime.now(zoneId);
            //Convert to LocalDateTime if needed
            LocalDateTime retrievalDateTime2 = currentZonedDateTime.toLocalDateTime();

            RedditPost newPost = new RedditPost(subreddit, title, author, Integer.parseInt(upvotes),Integer.parseInt(comments), link, creationDateTime2, retrievalDateTime2);
            logger.info("RedditPost.toString(): {}", newPost.toString());



        }

        // Document document = Jsoup.connect(redditUrl).get();
        // Elements shRedditPosts = document.select("shreddit-post");

        // Iterate through the selected elements


    }


}
