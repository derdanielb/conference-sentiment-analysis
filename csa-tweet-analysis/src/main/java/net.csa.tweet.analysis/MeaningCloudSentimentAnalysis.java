package net.csa.tweet.analysis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author philipp.amkreutz
 */
public class MeaningCloudSentimentAnalysis implements SentimentAnalysis {

	private final String apiKey = "8119eeaf0a690d4e3ea55125810249bf";

	private String createRequestUrl(String textToByAnalyzed) throws UnsupportedEncodingException {
		String url = "https://api.meaningcloud.com/sentiment-2.1" + "?key=" + apiKey + "&of=json" + "&lang=en" + "&model=general" + "&txt=" + URLEncoder.encode(textToByAnalyzed, "UTF-8");
		return url;
	}

	public Tweet analyzeTweet(Tweet tweet) {
		try {
			URL url = new URL(createRequestUrl(tweet.getText()));
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "*/*");
			BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String output = bf.readLine();
			StringBuilder sb = new StringBuilder();
			while (output != null) {
				sb.append(output);
				output = bf.readLine();
			}
			String result = sb.toString();
			JsonObject json = new Gson().fromJson(result, JsonObject.class);
			String score = json.get("score_tag").getAsString();
			if (score.equals("P+") || score.equals("P")) {
				tweet.setScore(1);
			} else if (score.equals("NEU") || score.equals("NONE")) {
				tweet.setScore(0);
			} else if (score.equals("N") || score.equals("N+")) {
				tweet.setScore(-1);
			}
			return tweet;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
