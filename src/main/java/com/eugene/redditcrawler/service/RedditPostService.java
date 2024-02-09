package com.eugene.redditcrawler.service;

import com.eugene.redditcrawler.db.entity.RedditPost;
import com.eugene.redditcrawler.db.repository.RedditPostRepository;
import com.eugene.redditcrawler.exception.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
