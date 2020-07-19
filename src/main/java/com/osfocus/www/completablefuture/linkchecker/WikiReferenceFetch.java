package com.osfocus.www.completablefuture.linkchecker;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WikiReferenceFetch implements IFetchStrategy {
    private static String[] schemes = { "http", "https" };
    private static UrlValidator urlValidator = new UrlValidator(schemes);

    @Override
    public List<ReferenceInfo> fetch(String url) {
        List<ReferenceInfo> refList = new LinkedList<>();

        try {
            Document doc = Jsoup.connect(url).get();

            Element referenceNode = doc.getElementsByClass("references").get(1);
            for (Element reference : referenceNode.getElementsByTag("li")) {
                String text = reference.getElementsByClass("reference-text").text();
                for (Element aTag : reference.getElementsByTag("a")) {
                    String link = aTag.attr("href");
                    if (urlValidator.isValid(link)) {
                        refList.add(new ReferenceInfo(text, aTag.attr("href")));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " going to return with list size " + refList.size());

        return refList;
    }
}
