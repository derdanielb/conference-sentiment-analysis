package net.csa.twitter.search;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.*;

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
    @CrossOrigin(origins = "*")
    public List<String> helloTwitter(@PathVariable String hashtag) throws ParseException {


        // to further develop this search functionality consult the following links
        // https://dev.twitter.com/rest/public/search
        // https://dev.twitter.com/rest/reference/get/search/tweets
        // http://docs.spring.io/spring-social-twitter/docs/1.1.2.RELEASE/reference/htmlsingle/
        // http://docs.spring.io/spring-social-twitter/docs/1.1.2.RELEASE/apidocs/

        // also try to use the public twitter search web application and check the query strings in the request URLs
        // https://twitter.com/search-home
        // https://dev.twitter.com/rest/public/search
        // https://dev.twitter.com/rest/reference/get/search/tweets

        // consider parameters customizing the language, the time frame or even the geo location
        // also consider parameters for using the paging mechanism to query more tweets in subsequent requests
        // something that happens if you scroll down in the main Twitter web user interface as well
        // https://dev.twitter.com/rest/public/search > Iterating in a result set
        // https://dev.twitter.com/rest/reference/get/search/tweets


        //"?f=tweets&vertical=default&q=%23" + hashtag + "+since:2016-06-13+until:2016-06-18&language=en&result_type=recent");

        //SearchParameters searchParameters = new SearchParameters("#" + hashtag + "+since:2016-06-13+until:2016-06-18")
        SearchParameters searchParameters = new SearchParameters("#" + hashtag)
                .includeEntities(false)
                .lang("en")
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
