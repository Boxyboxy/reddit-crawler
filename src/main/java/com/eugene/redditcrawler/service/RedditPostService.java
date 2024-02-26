package com.eugene.redditcrawler.service;

import com.eugene.redditcrawler.db.entity.RedditPost;
import com.eugene.redditcrawler.db.repository.RedditPostRepository;
import com.eugene.redditcrawler.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class RedditPostService {

    @Autowired
    private RedditPostRepository redditPostRepository;

    public List<RedditPost> getAllRedditPosts(){
        return redditPostRepository.findAll();
    }

    public RedditPost getRedditPostId(Long redditPostId){
        return redditPostRepository.findById(redditPostId).orElseThrow(() -> new NotFoundException("Reddit post not found"));
    }

    public List<RedditPost> getRedditPostsBySubreddit(String subreddit){
        return redditPostRepository.findBySubreddit(subreddit);
    }

    public void saveRedditPost(RedditPost redditPost){
        redditPostRepository.save(redditPost);
    }

    public void saveBulkData( List<RedditPost> redditPostList){
        redditPostRepository.saveAll(redditPostList);
    }

    public Optional<LocalDateTime> getLatestRetrievalDateTimeOfASubreddit(String subreddit) {
        return redditPostRepository.findLatestRetrievalDateTimeForSubreddit(subreddit);
    }

    public List<RedditPost> getPostsByRetrievalDateAndSubredditOrderByUpvotesDesc(String retrievalDate, String subreddit){
        return redditPostRepository.findByRetrievalDateAndSubredditOrderByUpvotesDesc(retrievalDate, subreddit);
    }

}
