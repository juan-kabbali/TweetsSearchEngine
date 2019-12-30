package main.tweet.search.engine.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.tweet.search.engine.Properties;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;

/**
 * @author Juan Pablo
 * @author Juliana Castellanos
 */
public class Tweet extends Doc{

    private String userId;
    private Date publicationDate;
    private String content;
    private String retweetedUserId;
    
    public Tweet() {
    }

    public Tweet(String[] fields) {
        try {
            this.id = (tweetHasField(fields, Properties.TWEET_ID)) ? fields[Properties.TWEET_ID] : null;
            this.userId = (tweetHasField(fields, Properties.TWEET_USER_ID)) ? fields[Properties.TWEET_USER_ID].toLowerCase() : null;

            // parse date string with specified format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSSSSS");
            this.publicationDate = (tweetHasField(fields, Properties.TWEET_PUB_DATE)) ? sdf.parse(fields[Properties.TWEET_PUB_DATE]) : null;
            this.content = (tweetHasField(fields, Properties.TWEET_CONTENT)) ? fields[Properties.TWEET_CONTENT] : null;
            this.retweetedUserId = (tweetHasField(fields, Properties.TWEET_RETWEETED_USER_ID)) ? fields[Properties.TWEET_RETWEETED_USER_ID].toLowerCase() : null;
        } catch (ParseException ex) {
            // Logger.getLogger(Tweet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("There was a problem parsing a tweet");
        }
    }

    public Tweet(String id, String userId, Date publicationDate, String content, String retweetedUserId, float score) {
        this.id = id;
        this.userId = userId;
        this.publicationDate = publicationDate;
        this.content = content;
        this.retweetedUserId = retweetedUserId;
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRetweetedUserId() {
        return retweetedUserId;
    }

    public void setRetweetedUserId(String retweetedUserId) {
        this.retweetedUserId = retweetedUserId;
    }

    @Override
    public String toString() {
        return "Tweet{" + "id=" + id + ", userId=" + userId + ", publicationDate=" + publicationDate + ", content=" + content + ", retweetedUserId=" + retweetedUserId + ", score=" + score + '}';
    }
    
    private boolean tweetHasField(String[] fields, int fieldPosition) {
        return fieldPosition >= 0 && fieldPosition < fields.length;
    }

    public void indexTweet(IndexWriter indexWriter) throws IOException {
        Document doc = new Document();
        // Lucene Field types https://lucene.apache.org/core/7_0_0/core/org/apache/lucene/document/class-use/Field.html
        
        if(this.getId() != null){
            doc.add(new StringField(Properties.LUCENE_TWEET_ID, this.getId(), Field.Store.YES));
        }
        
        if(this.getUserId() != null){
            doc.add(new StringField(Properties.LUCENE_TWEET_USER_ID, this.getUserId(), Field.Store.YES));
        }
        
        if(this.getPublicationDate() != null){
            doc.add(new StringField(Properties.LUCENE_TWEET_PUB_DATE, DateTools.dateToString(this.getPublicationDate(), DateTools.Resolution.DAY), Field.Store.YES));
        }
        
        if(this.getContent() != null){
            doc.add(new TextField(Properties.LUCENE_TWEET_CONTENT, this.getContent(), Field.Store.YES));
        }
        
        if(this.getRetweetedUserId() != null){
            doc.add(new StringField(Properties.LUCENE_TWEET_RETWEETED_USER_ID, this.getRetweetedUserId(), Field.Store.YES));
        }
        indexWriter.addDocument(doc);
    }
}
