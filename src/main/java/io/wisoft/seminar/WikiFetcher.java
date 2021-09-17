package io.wisoft.seminar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class WikiFetcher {
    private long lastRequestTime = -1;
    private final long minInterval = 1000;

    public Elements fetchWikipedia(final String url) throws IOException {
        sleepIfNeeded();

        final Connection connection = Jsoup.connect(url);
        final Document document = connection.get();
        final Element content = document.getElementById("mw-content-text");
        final Elements paragraphs = content.select("p");
        return paragraphs;
    }

    private void sleepIfNeeded() {
        if (lastRequestTime != -1) {
            long currentTime = System.currentTimeMillis();
            long nextRequestTime = lastRequestTime + minInterval;
            if (currentTime < nextRequestTime) {
                try {
                    Thread.sleep(nextRequestTime - currentTime);
                } catch (InterruptedException e) {
                    System.err.println("Warning: sleep interrupted in fetchWikipedia.");
                }
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }

    public static void main(final String... args) throws IOException {
        WikiFetcher wf = new WikiFetcher();
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        Elements paragraphs = wf.fetchWikipedia(url);

        for (Element paragraph: paragraphs) {
            System.out.println(paragraph);
        }
    }
}
