package main.tweet.search.engine.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class allows to Handle tweets objects by using a List.
 * Each row on the table placed in HomeView interface is an object contained into tweetList
 * @author Juan Pablo
 */
public class TweetsHandler {
    
    private ObservableList<Tweet> tweetList;

    public TweetsHandler() {
        this.tweetList = FXCollections.observableArrayList();
    }

    public ObservableList<Tweet> getTweetList() {
        return tweetList;
    }

    public void setTweetList(ObservableList<Tweet> tweetList) {
        this.tweetList = tweetList;
    }
    
}
