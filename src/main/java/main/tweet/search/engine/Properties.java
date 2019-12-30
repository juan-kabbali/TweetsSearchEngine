package main.tweet.search.engine;

import java.util.Date;

/**
 * @author Juan Pablo
 * @author Juliana Castellanos
 */
public class Properties {
    // GUI
    public static int width = 1280;
    public static int heigth = 720;
    public static String appName = "Tweets Search Engine";
    
    // TWEET FIELDS
    public static int TWEET_ID = 0;
    public static final String LUCENE_TWEET_ID = "id";
    
    public static int TWEET_USER_ID = 1;
    public static final String LUCENE_TWEET_USER_ID = "user_id";
    
    public static int TWEET_PUB_DATE = 2;
    public static final String  LUCENE_TWEET_PUB_DATE = "publicacion_date";
    
    public static int TWEET_CONTENT = 3;
    public static final String  LUCENE_TWEET_CONTENT = "content";
    
    public static int TWEET_RETWEETED_USER_ID = 4;
    public static final String  LUCENE_TWEET_RETWEETED_USER_ID = "retweeted_user_id";
    
    // FILE SYSTEM
    public static String TWEETS_ALLOWED_EXTENSION = "*.txt";
    public static String LUCENE_INDEX_PATH = "tweetsIndex";
    
    // LUCENE
    public static int MAX_DOCS = 1000000;
    
    
}
