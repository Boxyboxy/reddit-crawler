package com.eugene.redditcrawler.controller;

import com.eugene.redditcrawler.configuration.WebDriverConfig;
import com.eugene.redditcrawler.service.RedditCrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/reddit")
public class RedditController {

    @Autowired
    private RedditCrawlerService redditCrawlerService;
    @GetMapping("/{subreddit}/test")
    public ResponseEntity<String> test(@PathVariable String subreddit) {
        try{
            redditCrawlerService.test(subreddit);
        } catch (IOException e){
            e.printStackTrace();
        }

        return new ResponseEntity<String>(subreddit, HttpStatus.OK);
    }

    @GetMapping("/{subreddit}/test2")
    public ResponseEntity<String> test2(@PathVariable String subreddit) {
        redditCrawlerService.test2(subreddit);


        return new ResponseEntity<String>(subreddit, HttpStatus.OK);
    }

}
