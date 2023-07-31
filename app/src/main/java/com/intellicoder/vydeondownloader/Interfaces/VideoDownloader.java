package com.intellicoder.vydeondownloader.Interfaces;

public interface VideoDownloader {

    String createDirectory();

    String getVideoId(String link);

    void DownloadVideo();
}
