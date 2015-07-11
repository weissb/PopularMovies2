package com.example.benjamin.popularmovies;

/**
 * Created by benjamin on 7/11/2015.
 */
public class MovieItem {
    private String sBackdrop;
    private String sID;
    private String sOriginalTitle;
    private String sOverview;
    private String sReleaseDate;
    private String sPosterPath;
    private String sVoteAverage;

    public MovieItem(String sBackdrop, String sID, String sOriginalTitle, String sOverview, String sReleaseDate, String sPosterPath, String sVoteAverage) {
        this.sBackdrop = sBackdrop;
        this.sID = sID;
        this.sOriginalTitle = sOriginalTitle;
        this.sOverview = sOverview;
        this.sReleaseDate = sReleaseDate;
        this.sPosterPath = sPosterPath;
        this.sVoteAverage = sVoteAverage;
    }

    public MovieItem() {
    }

    public String getsBackdrop() {
        return sBackdrop;
    }

    public void setsBackdrop(String sBackdrop) {
        this.sBackdrop = sBackdrop;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsOriginalTitle() {
        return sOriginalTitle;
    }

    public void setsOriginalTitle(String sOriginalTitle) {
        this.sOriginalTitle = sOriginalTitle;
    }

    public String getsOverview() {
        return sOverview;
    }

    public void setsOverview(String sOverview) {
        this.sOverview = sOverview;
    }

    public String getsReleaseDate() {
        return sReleaseDate;
    }

    public void setsReleaseDate(String sReleaseDate) {
        this.sReleaseDate = sReleaseDate;
    }

    public String getsPosterPath() {
        return sPosterPath;
    }

    public void setsPosterPath(String sPosterPath) {
        this.sPosterPath = sPosterPath;
    }

    public String getsVoteAverage() {
        return sVoteAverage;
    }

    public void setsVoteAverage(String sVoteAverage) {
        this.sVoteAverage = sVoteAverage;
    }
}
