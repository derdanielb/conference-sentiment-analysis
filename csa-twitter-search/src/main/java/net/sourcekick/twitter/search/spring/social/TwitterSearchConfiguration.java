package net.sourcekick.twitter.search.spring.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.social.twitter.api.Twitter;

@Configuration
@PropertySource("file:///tmp/my_twitter_consumer_key.txt")
@PropertySource("file:///tmp/my_twitter_consumer_secret.txt")
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
