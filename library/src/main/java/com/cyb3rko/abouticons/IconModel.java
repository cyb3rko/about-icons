package com.cyb3rko.abouticons;

public class IconModel {
    private String iconLink;
    private String message;
    private String title;

    public IconModel(String title, String message, String iconLink) {
        this.iconLink = iconLink;
        this.message = message;
        this.title = title;
    }

    public String getIconLink() {
        return iconLink;
    }

    public String getMessage() {
        return message;
    }

    public String getTitle() {
        return title;
    }
}
