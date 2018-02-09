package audio.youtube;
/*
    Created by nils on 05.02.2018 at 22:37.
    
    (c) nils 2018
*/

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;
import commands.chat.commands.testing.Ping;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import utils.Secret;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static utils.Static.PREFIX;


public class YouTubeAPI {


    public static YouTube getYouTube() throws Exception{

        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("magnetbot-discord").build();

    }

    public static List<SearchResult> searchVideos(String queryTerm, long returnNumber) throws Exception{

        YouTube.Search.List search = getYouTube().search().list("id,snippet");
        search.setKey(Secret.YOUTUBE_APIKEY);
        search.setQ(queryTerm);

        search.setType("video");

        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults(returnNumber);

        SearchListResponse searchResponse = search.execute();
        if (searchResponse.getItems().size() == 0)
            throw new YouTubeAPI.NoResultException();
        return searchResponse.getItems();
    }

    public static SearchResult searchVideo(String queryTerm) throws Exception {

        YouTube.Search.List search = getYouTube().search().list("id,snippet");
        search.setKey(Secret.YOUTUBE_APIKEY);
        search.setQ(queryTerm);

        search.setType("video");

        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults(1L);

        SearchListResponse searchResponse = search.execute();
        if (searchResponse.getItems().size() == 0)
            throw new YouTubeAPI.NoResultException();
        return searchResponse.getItems().get(0);
    }

    public static SearchResult searchPlaylist(String queryTerm) throws Exception {
        YouTube.Search.List search = getYouTube().search().list("id,snippet");
        search.setKey(Secret.YOUTUBE_APIKEY);
        search.setQ(queryTerm);

        search.setType("playlist");

        search.setFields("items(id/kind,id/playlistId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults(1L);

        SearchListResponse searchResponse = search.execute();
        if (searchResponse.getItems().size() == 0)
            throw new YouTubeAPI.NoResultException();
        return searchResponse.getItems().get(0);
    }

    public static class NoResultException extends Exception { }

}