package com.eugene.redditcrawler.db.repository;

import com.eugene.redditcrawler.db.entity.RedditPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RedditPostRepository extends JpaRepository<RedditPost, Long> {
    // You can add custom query methods if needed

    List<RedditPost> findBySubreddit(String subreddit);

    Optional<RedditPost> findTopBySubredditOrderByRetrievalDateTimeDesc(String subreddit);

    // Retrieve records based on RetrievalDateTime and Subreddit, ordered by Upvotes
    @Query(value = "SELECT * FROM reddit_post e WHERE e.subreddit = :subreddit and  DATE(e.retrieval_date_time) = CAST(:retrievalDate AS DATE)", nativeQuery = true)
    List<RedditPost> findByRetrievalDateAndSubredditOrderByUpvotesDesc(@Param("retrievalDate") String retrievalDate, @Param("subreddit") String subreddit);

    @Query("SELECT MAX(e.retrievalDateTime) FROM RedditPost e WHERE e.subreddit = :subreddit")
    Optional<LocalDateTime> findLatestRetrievalDateTimeForSubreddit(String subreddit);
}