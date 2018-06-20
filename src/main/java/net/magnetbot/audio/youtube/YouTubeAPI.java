package net.magnetbot.audio.youtube;
/*
    Created by nils on 05.02.2018 at 22:37.
    
    (c) nils 2018
*/

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import net.magnetbot.utils.Secret;

import java.io.IOException;
import java.util.List;


public class YouTubeAPI {


    public static YouTube getYouTube() throws Exception{

        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {
                // do nothing
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

    public static SearchResult relatedVideo(String videoId) throws Exception {

        YouTube.Search.List search = getYouTube().search().list("id,snippet");
        search.setKey(Secret.YOUTUBE_APIKEY);
        search.setRelatedToVideoId(videoId);

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