package com.fyp.mykarachi;

public class Tweet {

    private String name, profile_image_url, screen_name, time_stamp, update;

    public Tweet() {
    }

    public Tweet(String name, String profile_image_url, String screen_name, String time_stamp, String update) {
        this.name = name;
        this.profile_image_url = profile_image_url;
        this.screen_name = screen_name;
        this.time_stamp = time_stamp;
        this.update = update;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }
}
