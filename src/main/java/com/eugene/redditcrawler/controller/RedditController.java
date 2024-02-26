package com.eugene.redditcrawler.controller;

import com.eugene.redditcrawler.db.entity.RedditPost;
import com.eugene.redditcrawler.service.RedditCrawlerService;
import com.eugene.redditcrawler.service.RedditPostCsvService;
import com.eugene.redditcrawler.service.RedditPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/reddit")
public class RedditController {
    private static Logger logger = LogManager.getLogger(RedditCrawlerService.class);

    private final ObjectMapper objectMapper;

    public RedditController (ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }
    @Autowired
    private RedditCrawlerService redditCrawlerService;

    @Autowired
    private RedditPostService redditPostService;

    @Autowired
    private RedditPostCsvService redditPostCsvService;

    @GetMapping("/test/{subreddit}")
    public ResponseEntity<String> test(@PathVariable String subreddit) {
        try{
            redditCrawlerService.test(subreddit);
        } catch (IOException e){
            e.printStackTrace();
        }

        return new ResponseEntity<String>(subreddit, HttpStatus.OK);
    }

    @GetMapping("/getTop20/{subreddit}")
    public ResponseEntity<List<RedditPost>> getTop20Posts(@PathVariable String subreddit) {
        Optional<LocalDateTime> latestRetrievalDateTimeOp =  redditPostService.getLatestRetrievalDateTimeOfASubreddit(subreddit);
        List<RedditPost> redditPostList;
        // if records are present in db
        if(latestRetrievalDateTimeOp.isPresent() && LocalDate.now(ZoneOffset.UTC).isEqual(latestRetrievalDateTimeOp.get().toLocalDate())){
            logger.info("Latest retrieval date for {} is {}", subreddit, latestRetrievalDateTimeOp.get().toLocalDate().toString());
            logger.info("Database contains records retrieved on the same date");
            // Extracting top 20 records
            redditPostList = redditPostService.getPostsByRetrievalDateAndSubredditOrderByUpvotesDesc(latestRetrievalDateTimeOp.get().toLocalDate().toString(), subreddit);
            // TODO: Add try catch block to handle exceptions like an empty list
            return new ResponseEntity<List<RedditPost>>(redditPostList, HttpStatus.OK);
        // records are not present in db, crawl to retrieve top 20 posts
        } else {
            logger.info("Latest retrieval date for {} is absent/irrelevant. Proceeding on to crawl reddit. ", subreddit);
            // If latest retrieval date is more than 24 hours ago then crawl
            redditPostList =  redditCrawlerService.crawlForTop20Posts(subreddit);

            redditPostService.saveBulkData(redditPostList);
            // TODO: try catch block to handle exceptions
            return new ResponseEntity<List<RedditPost>>(redditPostList, HttpStatus.OK);
        }
        // TODO: Generate report?
    }

    @GetMapping("/getTop20/csv/{subreddit}")
    public ResponseEntity<byte[]> getTop20PostsAsFile(@PathVariable String subreddit) {
        Optional<LocalDateTime> latestRetrievalDateTimeOp =  redditPostService.getLatestRetrievalDateTimeOfASubreddit(subreddit);
        List<RedditPost> redditPostList;
        // if records are present in db
        if(latestRetrievalDateTimeOp.isPresent() && LocalDate.now(ZoneOffset.UTC).isEqual(latestRetrievalDateTimeOp.get().toLocalDate())){
            logger.info("Latest retrieval date for {} is {}", subreddit, latestRetrievalDateTimeOp.get().toLocalDate().toString());
            logger.info("Database contains records retrieved on the same date");
            // Extracting top 20 records
            redditPostList = redditPostService.getPostsByRetrievalDateAndSubredditOrderByUpvotesDesc(latestRetrievalDateTimeOp.get().toLocalDate().toString(), subreddit);
            // TODO: Add try catch block to handle exceptions like an empty list

            // records are not present in db, crawl to retrieve top 20 posts
        } else {
            logger.info("Latest retrieval date for {} is absent/irrelevant. Proceeding on to crawl reddit. ", subreddit);
            // If latest retrieval date is more than 24 hours ago then crawl
            redditPostList =  redditCrawlerService.crawlForTop20Posts(subreddit);
            redditPostService.saveBulkData(redditPostList);
            // TODO: try catch block to handle exceptions

        }
        // TODO: Generate report?
        String csvContent = redditPostCsvService.convertRedditPostToCsv(redditPostList);
        byte[] csvBytes = csvContent.getBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "redditPosts.csv");
        return new ResponseEntity<>(csvBytes, headers, 200);
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
