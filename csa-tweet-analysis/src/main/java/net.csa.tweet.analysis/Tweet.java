package net.csa.tweet.analysis;

/**
 * @author philipp.amkreutz
 */
public class Tweet {

	//1:positive, -1:negative, 0:neutral
	private int score = -99;

	private String text;

	private int wordCount = -1;

	public Tweet(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWordCount() {
		if (wordCount != -1) {
			return wordCount;
		}
		wordCount = 0;

		boolean word = false;
		int endOfLine = text.length() - 1;

		for (int i = 0; i < text.length(); i++) {
			if (Character.isLetter(text.charAt(i)) && i != endOfLine) {
				word = true;
			} else if (!Character.isLetter(text.charAt(i)) && word) {
				wordCount++;
				word = false;
			} else if (Character.isLetter(text.charAt(i)) && i == endOfLine) {
				wordCount++;
			}
		}
		return wordCount;
	}

}
