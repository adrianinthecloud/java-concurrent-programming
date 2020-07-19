package com.osfocus.www.completablefuture.linkchecker;

import java.util.List;

/***
 * Author: Adrian LIU
 * Date: 2020-07-19
 * Desc: Simple link checker to demo completableFuture usage
 * This is for demo and study purpose only. The author is not responsible for any malicious usage.
 */
public class LinkCheckerTester {
    static String link = "https://en.wikipedia.org/wiki/Wiki";

    public static void main(String[] args) {
        LinkChecker linkChecker = new LinkChecker(link, new WikiReferenceFetch());
        List<ReferenceInfo> refList = linkChecker.check();
        System.out.println("Completed validating " + refList.size() + " links.");
    }
}
