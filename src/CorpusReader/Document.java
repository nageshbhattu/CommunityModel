/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CorpusReader;

import cc.mallet.types.Alphabet;
import cc.mallet.types.AlphabetCarrying;
import cc.mallet.types.FeatureSequence;
import cc.mallet.types.LabelSequence;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class Document implements Serializable, AlphabetCarrying {
    FeatureSequence text;
    LabelSequence topicSequence;
    int sender;
    int senderCommunity;
    int[] recievers;
    int[] recieverCommunities;
    Alphabet userAlphabet;
    Alphabet dataAlphabet;
    int[][] communityTopicCounts;
    
    Document(Alphabet dict){
        dataAlphabet = dict;
    }
    Document(int sender, int[] recievers, FeatureSequence text){
        this.text = text;
        this.sender = sender;
        this.recievers = recievers;
        userAlphabet = new Alphabet();
    }
    Document(int sender, ArrayList<Integer> recievers, FeatureSequence text,Alphabet userDict){
        this.text = text;
        this.sender = sender;
        this.recievers = new int[recievers.size()];
        for(int ri= 0;ri<recievers.size();ri++){
            this.recievers[ri]  = recievers.get(ri);
        }
        dataAlphabet = text.getAlphabet();
        userAlphabet = userDict;
    }
    
    @Override
    public Alphabet getAlphabet() {
        return dataAlphabet;
    }
    
    @Override
    public Alphabet[] getAlphabets() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public FeatureSequence getText(){
        return text;
    }
    public FeatureSequence getTopicSequence(){
        return topicSequence;
    }
    public int getSender(){
        return sender;
    }
    public int getSenderCommunity(){
        return senderCommunity;
    }
    public int[] getRecievers(){
        return recievers;
    }
    public int[] getRecipientCommunities(){
        return recieverCommunities;
    }
}
