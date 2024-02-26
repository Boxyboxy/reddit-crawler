package com.eugene.redditcrawler.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "reddit_post")
public class RedditPost {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subreddit;
    private String title;
    private String author;
    private Integer upvotes;
    private Integer comments;
    private String link;
    private LocalDateTime creationDateTime;
    private LocalDateTime retrievalDateTime;



    // Constructor without id (for inserting new records)
    public RedditPost(String subreddit, String title, String author, Integer upvotes, Integer comments, String link, LocalDateTime creationDateTime, LocalDateTime retrievalDateTime) {
        this.subreddit = subreddit;
        this.title = title;
        this.author = author;
        this.upvotes = upvotes;
        this.comments = comments;
        this.link = link;
        this.creationDateTime = creationDateTime;
        this.retrievalDateTime = retrievalDateTime;
    }


}
