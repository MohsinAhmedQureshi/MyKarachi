package com.fyp.mykarachi;

public class News {

    private String Location;
    private String Cause;
    private String Details;
    private String ImageURL;
    private String Author;
    private String AuthorName;
    private String Reputation;

    public News() {
    }

    public News(String location, String cause, String details, String imageURL, String author, String authorName, String reputation) {
        Location = location;
        Cause = cause;
        Details = details;
        ImageURL = imageURL;
        Author = author;
        AuthorName = authorName;
        Reputation = reputation;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getCause() {
        return Cause;
    }

    public void setCause(String cause) {
        Cause = cause;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getReputation() {
        return Reputation;
    }

    public void setReputation(String reputation) {
        Reputation = reputation;
    }

    @Override
    public String toString() {
        return "News{" +
                "Location='" + Location + '\'' +
                ", Cause='" + Cause + '\'' +
                ", Details='" + Details + '\'' +
                ", ImageURL='" + ImageURL + '\'' +
                ", Author='" + Author + '\'' +
                ", Reputation=" + Reputation +
                '}';
    }
}