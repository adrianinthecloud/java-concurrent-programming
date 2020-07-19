package com.osfocus.www.completablefuture.linkchecker;

public class ReferenceInfo {
    private String text;
    private String link;
    private boolean isValidLink;

    public ReferenceInfo(String text, String link) {
        this.text = text;
        this.link = link;
        this.isValidLink = false;
    }

    public ReferenceInfo(String text, String url, boolean isValidLink) {
        this.text = text;
        this.link = url;
        this.isValidLink = isValidLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isValidLink() {
        return isValidLink;
    }

    public void setValidLink(boolean validLink) {
        isValidLink = validLink;
    }

    @Override
    public String toString() {
        return "link='" + link + '\'' +
                ", isValidLink=" + isValidLink +
                ", text='" + text + '\'';
    }
}
