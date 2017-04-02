package net.csa.tweet.analysis;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.Properties;

public class ScoreAnalyser {


    private StanfordCoreNLP pipeline;

    public ScoreAnalyser() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);
    }

    public int findSentiment(String text) {

        //sentiment analysis with standford nlp
        //look at https://nlp.stanford.edu/ for more information
        //returns an integer
        //  0: very negativ
        //  1: negative
        //  2: neutral
        //  3: positiv
        //  4: very positive

        int score = 0;
        if (text != null && text.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(text);
            for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    score = sentiment;
                    longest = partText.length();
                }
            }
        }

        if(score < 0 || score > 4) return 0;
        return score;
    }

}
