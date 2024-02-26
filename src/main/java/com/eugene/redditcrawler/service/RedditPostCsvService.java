package com.eugene.redditcrawler.service;

import com.eugene.redditcrawler.db.entity.RedditPost;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Log4j2
@Service
public class RedditPostCsvService {
    private static Logger logger = LogManager.getLogger(RedditCrawlerService.class);
    public String convertRedditPostToCsv(List<RedditPost> redditPosts){
        try(
                StringWriter writer = new StringWriter();
                CSVWriter csvWriter = new CSVWriter(
                        writer,
                        CSVWriter.DEFAULT_SEPARATOR,
                        CSVWriter.NO_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)) {
            String[] header = {"subreddit","title","author","upvotes","comments","link","creation_date_time","retrieval_date_time"};

            csvWriter.writeNext(header);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            for(RedditPost post: redditPosts){
                String[] record = {
                        post.getSubreddit(),
                        post.getTitle(),
                        post.getAuthor(),
                        String.valueOf(post.getUpvotes()),
                        String.valueOf(post.getComments()),
                        post.getLink(),
                        post.getCreationDateTime().format(formatter),
                        post.getRetrievalDateTime().format(formatter)
                };
                csvWriter.writeNext(record);
            }
            return writer.toString();

        } catch (Exception e){
            logger.info(e.getMessage());
            return null;
        }
    }
}
