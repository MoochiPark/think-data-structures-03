package io.wisoft.seminar;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class TermCounter {
    private final Map<String, Integer> map;
    private final String label;

    public TermCounter(final String label) {
        this.label = label;
        this.map = new HashMap<>();
    }

    public String getLabel() {
        return label;
    }

    public void put(final String term, final int count) {
        map.put(term, count);
    }

    public Integer get(final String term) {
        Integer count = map.get(term);
        return count == null ? 0 : count;
    }

    public Set<String> keySet() {
        return map.keySet();
    }

    public Integer size() {
        return map.size();
    }

    public void incrementTermCount(final String term) {
        put(term, get(term) + 1);
    }

    public void processElements(final Elements paragraphs) {
        for (Node node : paragraphs) {
            processTree(node);
        }
    }

    public void processTree(final Node root) {
        for (Node node : new WikiNodeIterable(root)) {
            if (node instanceof TextNode) {
                processText(((TextNode) node).text());
            }
        }
    }

    public void processText(final String text) {
        final String[] terms = text.replaceAll("\\pP", " ")
                .toLowerCase()
                .split("\\s+");

        for (String term : terms) {
            incrementTermCount(term);
        }
    }

    public void printCounts() {
        for (String key : keySet()) {
            Integer count = get(key);
            System.out.println(key + ", " + count);
        }
        System.out.println("Total of all counts = " + size());
    }

    public static void main(final String... args) throws IOException {
        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";

        WikiFetcher wf = new WikiFetcher();
        Elements paragraphs = wf.fetchWikipedia(url);

        TermCounter counter = new TermCounter(url);
        counter.processElements(paragraphs);
        counter.printCounts();
    }
}
