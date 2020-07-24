package com.cyb3rko.abouticons;

public class IconModel {
    private boolean modified;
    private String iconLink;
    private String message;
    private String title;

    public IconModel(boolean modified, String title, String message, String iconLink) {
        this.modified = modified;
        this.iconLink = iconLink;
        this.message = message;
        this.title = title;
    }

    public boolean isModified() {
        return modified;
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
