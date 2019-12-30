package main.tweet.search.engine;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;
import main.tweet.search.engine.controllers.GraphsController;
import main.tweet.search.engine.model.Tweet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.misc.HighFreqTerms;
import org.apache.lucene.misc.TermStats;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * @author Juan Pablo
 * @author Juliana Castellanos
 */
public class ApacheLuceneHandler {

    private FrenchAnalyzer analyzer;
    // private StandardAnalyzer analyzer;
    private Directory directory;
    private IndexWriterConfig indexWriterConfig;
    private IndexWriter indexWriter;
    private IndexSearcher indexSearcher;
    private Query query;
    private ScoreDoc[] hits;

    public ApacheLuceneHandler() {
        try {
            this.analyzer = new FrenchAnalyzer(Version.LUCENE_42);

            // this.analyzer = new StandardAnalyzer(Version.LUCENE_42);
            // this.directory = new RAMDirectory();
            this.directory = FSDirectory.open(new File(Properties.LUCENE_INDEX_PATH));
            this.indexWriterConfig = new IndexWriterConfig(Version.LUCENE_42, this.analyzer);
            this.indexWriter = new IndexWriter(this.directory, this.indexWriterConfig);
        } catch (IOException ex) {
            // Logger.getLogger(ApacheLuceneHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void search(String fieldName, String queryString) {
        try {
            QueryParser queryParser = new QueryParser(Version.LUCENE_42, fieldName, this.analyzer);
            this.query = queryParser.parse(queryString);

            IndexReader indexReader = DirectoryReader.open(this.directory);
            TopScoreDocCollector collector = TopScoreDocCollector.create(Properties.MAX_DOCS, true);

            this.indexSearcher = new IndexSearcher(indexReader);
            this.indexSearcher.search(query, collector);
            this.hits = collector.topDocs().scoreDocs;
            
            this.count(Properties.LUCENE_TWEET_RETWEETED_USER_ID, GraphsController.top10RetweetedUsers, 10);
            this.count(Properties.LUCENE_TWEET_USER_ID, GraphsController.top10ActiveUsers, 10);
            this.count(Properties.LUCENE_TWEET_CONTENT, GraphsController.top10UsedWords, 30);
            
            System.out.println("Field: " + fieldName + "\t Terms: " + queryString);
            System.out.println("Total Hits: " + collector.getTotalHits());

        } catch (ParseException | IOException ex) {
            Logger.getLogger(ApacheLuceneHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Tweet> retrieveFoundedDocs() {
        List<Tweet> topTweets = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            for (int i = 0; i < this.hits.length; ++i) {
                int docId = this.hits[i].doc;
                Document d = this.indexSearcher.doc(docId);
                Date pubDate = new Date();
                if(d.get(Properties.LUCENE_TWEET_PUB_DATE) != null){
                    pubDate = sdf.parse(d.get(Properties.LUCENE_TWEET_PUB_DATE));
                }
                
                topTweets.add(new Tweet(
                        d.get(Properties.LUCENE_TWEET_ID),
                        d.get(Properties.LUCENE_TWEET_USER_ID),
                        pubDate,
                        d.get(Properties.LUCENE_TWEET_CONTENT),
                        d.get(Properties.LUCENE_TWEET_RETWEETED_USER_ID),
                        this.hits[i].score));
            }
        } catch (IOException | java.text.ParseException ex) {
            Logger.getLogger(ApacheLuceneHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return topTweets;
    }

    public void count(String field, HashMap data, int top) {
        try (IndexReader indexReader = DirectoryReader.open(this.directory)) {
            TermStats[] stats = HighFreqTerms.getHighFreqTerms(indexReader, top, field);
            data.clear();
            for (TermStats termStats : stats) {
                String termText = termStats.termtext.utf8ToString();
                System.out.println(termText + " " + termStats.docFreq);
                data.put(termText, termStats.docFreq);
            }
        } catch (Exception e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    public void truncate(){
        try {
            this.indexWriter.deleteAll();
            this.indexWriter.commit();
        } catch (IOException ex) {
            Logger.getLogger(ApacheLuceneHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean indexExists() {
        boolean exists = false;
        exists = DirectoryReader.indexExists(this.directory);
        return exists;
    }

    public IndexWriter getIndexWriter() {
        return indexWriter;
    }

    public void setIndexWriter(IndexWriter indexWriter) {
        this.indexWriter = indexWriter;
    }

    public ScoreDoc[] getHits() {
        return hits;
    }

    public void setHits(ScoreDoc[] hits) {
        this.hits = hits;
    }
}
