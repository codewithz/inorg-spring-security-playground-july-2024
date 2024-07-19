package com.inorg.model;

public class Notice {
    private String noticeId;
    private String noticeTitle;

    public Notice(String noticeId, String noticeTitle) {
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
    }

    public Notice() {
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }
}
