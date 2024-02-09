package com.eugene.redditcrawler.db.repository;

import com.eugene.redditcrawler.db.entity.RedditPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RedditPostRepository extends JpaRepository<RedditPost, Long> {
    // You can add custom query methods if needed

    List<RedditPost> findBySubreddit(String value);

}