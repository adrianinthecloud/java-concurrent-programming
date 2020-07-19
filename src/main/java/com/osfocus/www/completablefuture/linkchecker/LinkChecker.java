package com.osfocus.www.completablefuture.linkchecker;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LinkChecker {
    private static final int TIME_OUT = 5000;
    private String link;
    private IFetchStrategy strategy;

    public LinkChecker(String link, IFetchStrategy strategy) {
        this.link = link;
        this.strategy = strategy;
    }

    public List<ReferenceInfo> check() {
        CompletableFuture<List<ReferenceInfo>> future = CompletableFuture.supplyAsync(() -> {
            List<ReferenceInfo> refList = strategy.fetch(link);
            return refList;
        });

        CompletableFuture<List<ReferenceInfo>> futureRes = future.thenApply(referenceList -> {
            List<CompletableFuture<ReferenceInfo>> futureList = new LinkedList<>();

            for (ReferenceInfo referenceInfo : referenceList) {
                System.out.println(referenceInfo);
                futureList.add(
                    CompletableFuture.supplyAsync(() -> {
                        return referenceInfo;
                    }).thenApplyAsync(r -> {
                        r.setValidLink(validateLink(r.getLink()));
                        System.out.println(r);
                        return r;
                    })
                );
            }

            try {
                // blocking the thread till all request are completed
                CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return referenceList;
        });

        try {
            return futureRes.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Fail to check.");
    }

    private boolean validateLink(String link) {
        if (link == null) return false;

        try {
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(TIME_OUT);
            return conn.getResponseCode() == 200;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
