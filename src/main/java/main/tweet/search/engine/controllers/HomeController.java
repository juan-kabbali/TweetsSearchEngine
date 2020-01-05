package main.tweet.search.engine.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import main.tweet.search.engine.ApacheLuceneHandler;
import main.tweet.search.engine.Properties;
import main.tweet.search.engine.model.Tweet;
import main.tweet.search.engine.model.TweetsHandler;

/**
 * FXML Controller class
 *
 * @author Juan Pablo
 * @author Juliana Castellanos
 */
public class HomeController implements Initializable {

    @FXML
    private TableView<Tweet> tweetsTable;

    @FXML
    private TableColumn<Tweet, String> id;

    @FXML
    private TableColumn<Tweet, String> userId;

    @FXML
    private TableColumn<Tweet, Date> publicationDate;

    @FXML
    private TableColumn<Tweet, String> content;

    @FXML
    private TableColumn<Tweet, String> retweetedUserId;

    @FXML
    private TableColumn<Tweet, Float> score;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchTerms;

    @FXML
    private ChoiceBox selectedField;

    @FXML
    private TextArea zoomTweetContent;

    @FXML
    private Label foundTweets;

    private TweetsHandler tweetsHandler;
    private ApacheLuceneHandler apacheLuceneHandler;

    /**
     * This method is executed before HomeView is displayed. 
     * This is a implementation of initialize method from Initializable interface
     * 
     * @param url
     * @param rb
    **/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Allows to create new lines or wrap text when content is longer than a single line
        this.zoomTweetContent.setWrapText(true);

        // Set up choice box options
        this.selectedField.setItems(FXCollections.observableArrayList(
                "By Content", "By user id", "By retweet user id"
        ));
        this.selectedField.getSelectionModel().selectFirst();

        // Set up Table columns
        this.id.setCellValueFactory(new PropertyValueFactory<Tweet, String>("Id"));
        this.userId.setCellValueFactory(new PropertyValueFactory<Tweet, String>("UserId"));
        this.publicationDate.setCellValueFactory(new PropertyValueFactory<Tweet, Date>("PublicationDate"));
        this.content.setCellValueFactory(new PropertyValueFactory<Tweet, String>("Content"));
        this.retweetedUserId.setCellValueFactory(new PropertyValueFactory<Tweet, String>("RetweetedUserId"));
        this.score.setCellValueFactory(new PropertyValueFactory<Tweet, Float>("Score"));

        // Initialize tweetsHanlder
        this.tweetsHandler = new TweetsHandler();

        // Initialize Lucene Hanlder
        this.apacheLuceneHandler = new ApacheLuceneHandler();

        // If an index exists, displays all its documents
        if (this.apacheLuceneHandler.indexExists()) {
            this.apacheLuceneHandler.search("*", "*");
            this.tweetsHandler.setTweetList(FXCollections.observableArrayList(this.apacheLuceneHandler.retrieveFoundedDocs()));
            this.refresTable();
        }

    }

    /**
     * This method handles click event of search button.
     * Here, we call an implementation of a apache lucene search taking the
     * textInput string as searching terms.
     * 
     * @param event
     * @throws IOException
     * @throws URISyntaxException 
     */
    @FXML
    private void handleSearchBtn(ActionEvent event) throws IOException, URISyntaxException {
        
        // If there is an existing lucene index
        if (this.apacheLuceneHandler.indexExists()) {
            // If there are not terms to search, we collect all documents with a wildcard expression.
            if (this.searchTerms.getText().isEmpty()) {
                this.apacheLuceneHandler.search("*", "*");
            } else {
                // If there are terms, we perform a query to get relevant documents
                this.apacheLuceneHandler.search(this.getTargetFields(), this.searchTerms.getText());
            }
            // Display results
            this.tweetsTable.getItems().clear();
            this.tweetsHandler.setTweetList(FXCollections.observableArrayList(this.apacheLuceneHandler.retrieveFoundedDocs()));
            this.refresTable();
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "Wait !  First of all you have to import the tweets");
        }
    }

    /**
     * This method handles Import Tweets menu Item click event.
     * Here, we display a chooser file to select the tweets for importing and 
     * loading them to the lucene index.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    private void handleImportTweets(ActionEvent event) throws IOException {
        File selectedFile = this.chooseTweetFile();
        if (selectedFile != null) {

            // Enconding file
            FileInputStream fis = new FileInputStream(selectedFile.getAbsolutePath());
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF8"))) {

                // Go line per line
                String line = br.readLine();
                while (line != null) {

                    // Tokenize by tab
                    String tokens[] = line.split("\\t");
                    if (tokens != null) {

                        // Create Tweet Object
                        Tweet tweet = new Tweet(tokens);

                        // Index tweet
                        try {
                            if (!tweet.getContent().isEmpty()) {
                                tweet.indexTweet(this.apacheLuceneHandler.getIndexWriter());
                            }
                        } catch (NullPointerException e) {
                            System.out.println("Empty content Tweet");
                        }

                        // Add tweet to current displayed tweets
                        this.tweetsHandler.getTweetList().add(tweet);
                    }
                    line = br.readLine();
                }
                
                /* Diplay just created tweets and set values for graphics in the 
                   visualization window */
                this.refresTable();
                this.apacheLuceneHandler.getIndexWriter().commit();
                this.apacheLuceneHandler.count(Properties.LUCENE_TWEET_RETWEETED_USER_ID, GraphsController.top10RetweetedUsers, 10);
                this.apacheLuceneHandler.count(Properties.LUCENE_TWEET_USER_ID, GraphsController.top10ActiveUsers, 10);
                this.apacheLuceneHandler.count(Properties.LUCENE_TWEET_CONTENT, GraphsController.top10UsedWords, 30);
            }
        } else {
            System.out.println("Canceled");
        }
    }

    /**
     * Here, we capture the event of keyboard arrows and click over a row on the
     * Tweets table to refresh its content in the preview textArea.
     */
    @FXML
    private void handleZoomTweetContent() {
        Tweet selectedTweet = this.tweetsTable.getSelectionModel().getSelectedItem();
        if (selectedTweet != null) {
            this.zoomTweetContent.setText(selectedTweet.getContent());
        }
    }

    /**
     * This method handles Delete index menu Item click event.
     * Here, we delete the index if exits
     * 
     */
    @FXML
    private void handleDeleteIndex() {
        System.out.println("Delete index");
        if (this.apacheLuceneHandler.indexExists()) {
            this.apacheLuceneHandler.truncate();
            JOptionPane.showMessageDialog(new JFrame(), "Your index have been deleted.");
            this.tweetsHandler.getTweetList().clear();
            this.refresTable();
        } else {
            JOptionPane.showMessageDialog(new JFrame(), "There is not any index to delete");
        }
    }

    /***
     * This method handles Open Visualization menu Item click event.
     * Here, we create a new stage that will open a new windows with visualization graphs.
     * @param event 
     */
    @FXML
    private void handleOpenVisualization(javafx.event.ActionEvent event) {
        try {
            Parent blah = FXMLLoader.load(getClass().getResource("/fxml/graphs.fxml"));
            Scene scene = new Scene(blah);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(Properties.appName + " Visualization dashboard");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method handles About menu Item click event.
     * Here, we just display a little text message regarding the app itself.
     */
    @FXML
    private void handleAbout() {
        JOptionPane.showMessageDialog(new JFrame(), "This is a project created in advanced development class at université lyon II by Juan Pablo Aguirre and Juliana Castellanos");
    }

    /**
     * This method opens a File Chooser pop up to select tweets file when importing.
     * @return Chosen file
     */
    private File chooseTweetFile() {
        // Display a filechooser to select tweets file
        FileChooser fileChooser = new FileChooser();

        // Only accept txt files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Tweets files", Properties.TWEETS_ALLOWED_EXTENSION);
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showOpenDialog(null);
    }

    /**
     * This method handles refreshing tasks for displaying updated tweets content.
     */
    private void refresTable() {
        this.tweetsTable.setItems(this.tweetsHandler.getTweetList());
        this.foundTweets.setText(Integer.toString(this.tweetsHandler.getTweetList().size()));
        this.zoomTweetContent.setText("");
    }

    /**
     * This method handles selectable conversion from selected element to lucene doc field name.
     * It's extremely important to tell lucene what field will be queried
     * @return Property String with apache lucene tweet field name
     */
    private String getTargetFields() {
        switch (this.selectedField.getSelectionModel().getSelectedItem().toString()) {
            case "By Content":
                return Properties.LUCENE_TWEET_CONTENT;
            case "By user id":
                return Properties.LUCENE_TWEET_USER_ID;
            case "By retweet user id":
                return Properties.LUCENE_TWEET_RETWEETED_USER_ID;
            default:
                return null;
        }
    }

}
