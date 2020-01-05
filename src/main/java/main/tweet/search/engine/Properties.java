package main.tweet.search.engine;

/**
 * This class defines properties that will be used in different places on this project.
 * All props are public and static because we are not going to create an
 * instance of this class. Props will be accessible everywhere on this project.
 * 
 * @author Juan Pablo
 * @author Juliana Castellanos
 */
public class Properties {
    
    // GUI PROPERTIES
    public static int width = 1280;
    public static int heigth = 720;
    public static String appName = "Tweets Search Engine";
    
    // TWEET FIELDS PROPERTIES
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
    
    // FILE SYSTEM PROPERTIES
    public static String TWEETS_ALLOWED_EXTENSION = "*.txt";
    public static String LUCENE_INDEX_PATH = "tweetsIndex";
    
    // LUCENE PROPERTIES
    public static int MAX_DOCS = 1000000;
    
    
}
