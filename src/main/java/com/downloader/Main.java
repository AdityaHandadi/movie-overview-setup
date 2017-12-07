package com.downloader;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException, InterruptedException {
        DownloadImdbData.downloadFromS3();
    }
}
