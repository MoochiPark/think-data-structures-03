package io.wisoft.seminar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikiPhilosophy {

    final static List<String> visited = new ArrayList<>();
    final static WikiFetcher wf = new WikiFetcher();

    public static void main(final String... args) throws IOException {

        String destination = "https://en.wikipedia.org/wiki/Philosophy";
        String source = "https://en.wikipedia.org/wiki/Java_(programming_language)";
//        String source = "https://en.wikipedia.org/wiki/Kotlin_(programming_language)";
//        String source = "https://en.wikipedia.org/wiki/Software";
//        String source = "https://en.wikipedia.org/wiki/Bts";

        testConjecture(destination, source, 30);
    }

    public static void testConjecture(final String destination, final String source, final int limit) throws IOException {
        String url = source;
        for (int i = 0; i < limit; i++) {
            if (visited.contains(url)) {
                System.err.println("We're in a loop, exiting.");
                return;
            } else {
                visited.add(url);
            }
            Element elt = getFirstValidLink(url);
            if (elt == null) {
                System.err.println("Got to a page with no valid links.");
                return;
            }

            System.out.println(i + ": **" + elt.text() + "**");
            url = elt.attr("abs:href");

            if (url.equals(destination)) {
                System.out.println("Eureka!");
                break;
            }
        }
    }

    public static Element getFirstValidLink(final String url) throws IOException {
        print(url);
        Elements paragraphs = wf.fetchWikipedia(url);
        WikiParser wp = new WikiParser(paragraphs);
        return wp.findFirstLink();
    }

    private static void print(final Object... args) {
        System.out.printf("Fetching %s...%n", args);
    }

}
