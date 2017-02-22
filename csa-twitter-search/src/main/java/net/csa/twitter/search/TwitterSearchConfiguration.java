package net.csa.twitter.search;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.social.twitter.api.Twitter;

@Configuration
//@PropertySource("file:///twitterkeys/my_twitter_consumer_key.txt")
//@PropertySource("file:///twitterkeys/my_twitter_consumer_secret.txt")
class TwitterSearchConfiguration {

    @Value("${key}")
    private String key;

    @Value("${secret}")
    private String secret;

    @Bean
    public Twitter twitter() {
        return new CustomTwitterTemplate(key, secret);
    }

}
