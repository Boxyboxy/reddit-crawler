package com.eugene.redditcrawler.controller;

import com.eugene.redditcrawler.db.entity.RedditPost;
import com.eugene.redditcrawler.service.RedditCrawlerService;
import com.eugene.redditcrawler.service.RedditPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reddit")
public class RedditController {

    private final ObjectMapper objectMapper;

    public RedditController (ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Autowired
    private RedditCrawlerService redditCrawlerService;

    @Autowired
    private RedditPostService redditPostService;

    @GetMapping("/test/{subreddit}")
    public ResponseEntity<String> test(@PathVariable String subreddit) {
        try{
            redditCrawlerService.test(subreddit);
        } catch (IOException e){
            e.printStackTrace();
        }

        return new ResponseEntity<String>(subreddit, HttpStatus.OK);
    }

    @GetMapping("/test2/{subreddit}")
    public ResponseEntity<String> test2(@PathVariable String subreddit) {
        redditCrawlerService.test2(subreddit);
        return new ResponseEntity<String>(subreddit, HttpStatus.OK);
    }

    @GetMapping("/redditposts")
    public ResponseEntity<String> getAllRedditPosts(){
        List<RedditPost> redditPosts = redditPostService.getAllRedditPosts();
        try {
            String jsonString = objectMapper.writeValueAsString(redditPosts);
            return ResponseEntity.ok(jsonString);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error converting to JSON");
        }
    }


    @GetMapping("/redditposts/{subreddit}")
    public ResponseEntity<String> getAllRedditPostsBySubReddit(@PathVariable String subreddit){
        List<RedditPost> redditPosts = redditPostService.getRedditPostsBySubreddit(subreddit);
        try {
            String jsonString = objectMapper.writeValueAsString(redditPosts);
            return ResponseEntity.ok(jsonString);
        }catch (Exception e){
            return ResponseEntity.status(500).body("Error converting to JSON");
        }
    }

}
