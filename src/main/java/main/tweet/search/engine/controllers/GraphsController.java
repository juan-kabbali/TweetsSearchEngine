package main.tweet.search.engine.controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * FXML Controller class
 * @author Juan Pablo
 * @author Juliana Castellanos
 */
public class GraphsController implements Initializable {

    public static HashMap<String, Integer> top10RetweetedUsers;
    public static HashMap<String, Integer> top10ActiveUsers;
    public static HashMap<String, Integer> top10UsedWords;

    /**
     * Initialize HashMaps in a static context to be set by ApacheLucenHandler
    **/
    static {
        GraphsController.top10RetweetedUsers = new HashMap<>();
        GraphsController.top10ActiveUsers = new HashMap<>();
        GraphsController.top10UsedWords = new HashMap<>();
    }
            
    @FXML
    private BarChart topRetweetedUsers;
    
    @FXML
    private BarChart topActiveUsers;
    
    @FXML
    private BarChart topUsedWords;
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Create graph of Top 10 Retweeted users
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("User ID");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Retweeted times");

        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Top 10 retweeted users");
        
        for (Map.Entry<String, Integer> entry : top10RetweetedUsers.entrySet()) {
            dataSeries1.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
            System.out.println("BAR ----> " + entry.getKey() + " " + entry.getValue());
        }

        this.topRetweetedUsers.getData().add(dataSeries1);    
        
        // Create graph of Top 10 active users
        CategoryAxis xAxis2 = new CategoryAxis();
        xAxis2.setLabel("User ID");

        NumberAxis yAxis2 = new NumberAxis();
        yAxis2.setLabel("Posted tweets");

        XYChart.Series<String, Number> dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Top 10 active users");
        
        for (Map.Entry<String, Integer> entry : top10ActiveUsers.entrySet()) {
            dataSeries2.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
            System.out.println("BAR ----> " + entry.getKey() + " " + entry.getValue());
        }

        this.topActiveUsers.getData().add(dataSeries2);    
        
        // Create graph of Top 30 used words
        CategoryAxis xAxis3 = new CategoryAxis();
        xAxis3.setLabel("Words");

        NumberAxis yAxis3 = new NumberAxis();
        yAxis3.setLabel("Count");

        XYChart.Series<String, Number> dataSeries3 = new XYChart.Series();
        dataSeries3.setName("Top 30 used words");
        
        for (Map.Entry<String, Integer> entry : top10UsedWords.entrySet()) {
            dataSeries3.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
            System.out.println("BAR ----> " + entry.getKey() + " " + entry.getValue());
        }

        this.topUsedWords.getData().add(dataSeries3);
       
        
    }    
    
}

