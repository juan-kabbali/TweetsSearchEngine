package main.tweet.search.engine.model;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

/**
 *
 * @author Juan Pablo
 */
public class TweetsHandler {
    
    private ObservableList<Tweet> tweetList;

    public TweetsHandler() {
        this.tweetList = FXCollections.observableArrayList();
        /* this.tweetList.addListener(new ListChangeListener<Tweet>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends Tweet> c) {
                for(Tweet tweet : c.getList() ){
                    tweetsTableView.getItems().add(tweet);
                }
            }
        
        }); */
    }

    public ObservableList<Tweet> getTweetList() {
        return tweetList;
    }

    public void setTweetList(ObservableList<Tweet> tweetList) {
        this.tweetList = tweetList;
    }
    
}
