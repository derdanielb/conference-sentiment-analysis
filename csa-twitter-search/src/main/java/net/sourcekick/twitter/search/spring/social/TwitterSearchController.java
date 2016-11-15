package net.sourcekick.twitter.search.spring.social;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/twitter/search")
class TwitterSearchController {

    private static final Logger log = getLogger(TwitterSearchController.class);

    /**
     * The Twitter Search API returns a maximum number of 100 tweets in response to a synchronous search request.
     */
    private static final int MAX_RESULTS = 100;

    private Twitter twitter;

    @Inject
    public TwitterSearchController(Twitter twitter) {
        this.twitter = twitter;
    }

    @RequestMapping(path = "/{hashtag}", method = RequestMethod.GET)
    public List<String> helloTwitter(@PathVariable String hashtag) throws ParseException {

        //"?f=tweets&vertical=default&q=%23" + hashtag + "+since:2016-06-13+until:2016-06-18&language=en&result_type=recent");

        //SearchParameters searchParameters = new SearchParameters("#" + hashtag + "+since:2016-06-13+until:2016-06-18")
        SearchParameters searchParameters = new SearchParameters(hashtag)
                .includeEntities(false)
                .count(MAX_RESULTS)
                .resultType(SearchParameters.ResultType.MIXED);
        //.since(new SimpleDateFormat("yyyy-MM-dd").parse("2016-06-13"))
        //.until(new SimpleDateFormat("yyyy-MM-dd").parse("2016-06-18"));

        log.debug(ReflectionToStringBuilder.toString(searchParameters, ToStringStyle.DEFAULT_STYLE));

        SearchResults searchResults = twitter.searchOperations().search(searchParameters);

        if (null != searchResults) {
            log.debug(ReflectionToStringBuilder.toString(searchResults, ToStringStyle.DEFAULT_STYLE));

            return searchResults.getTweets().stream().map(Tweet::getText).collect(Collectors.toList());

        } else {
            log.debug("No search results.");
            return Collections.emptyList();
        }

    }

}
