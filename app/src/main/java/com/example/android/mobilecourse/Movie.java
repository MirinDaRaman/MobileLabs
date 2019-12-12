package com.example.android.mobilecourse;

public class Movie {

    private String title;
    private Long year;
    private Long rating;
    private String description;
    private String poster;

    public Movie(String title, Long year, Long rating, String description, String poster) {
        this.title = title;
        this.year = year;
        this.rating = rating;
        this.description = description;
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public String getDescription() {
        return description;
    }

    public Long getRating() {
        return rating;
    }

    public Long getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }
}
