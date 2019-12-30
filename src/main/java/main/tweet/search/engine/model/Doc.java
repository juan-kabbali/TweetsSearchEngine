/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.tweet.search.engine.model;

/**
 *
 * @author Juan Pablo
 */
public class Doc {
    
    protected String id;
    protected float score;

    public Doc(String id, float score) {
        this.id = id;
        this.score = score;
    }
    
    public Doc(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
