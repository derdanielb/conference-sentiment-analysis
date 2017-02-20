package net.csa.conference_tweets.rest;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

/**
 * Created by zelle on 13.12.2016.
 */
public class ErrorGenerator {
    public static class Error{
        public String error;
        public int code;
    }

    public static ResponseEntity<Error> createError(String error, int code){
        Error e = new Error();
        e.code = code;
        e.error = error;
        return ResponseEntity.status(code).body(e);
    }

    public static ResponseEntity<Error> createError(String error, HttpStatus statusCode){
        return createError(error, statusCode.value());
    }

    public static ResponseEntity<Error> createError(String error){
        return createError(error, HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Error> createNotFound(String resource){
        return createError("Resource not found: " + resource, HttpStatus.NOT_FOUND);
    }

    public static ResponseEntity<Error> createServerError(Throwable throwable){
        return createError(throwable.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<Error> createServerError(String error){
        return createError(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
