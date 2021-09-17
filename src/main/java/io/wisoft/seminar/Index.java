package io.wisoft.seminar;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Index {

    private final Map<String, Set<TermCounter>> index = new HashMap<>();

    public void add(final String term, final TermCounter tc) {
        Set<TermCounter> set = get(term);

        // 어떤 검색어를 처음 찾으면 새로운 Set을 생성한다.
        if (set == null) {
            set = new HashSet<>();
            index.put(term, set);
        }
        // 그렇지 않으면 기존 Set에 추가한다.
        set.add(tc);
    }

    public Set<TermCounter> get(final String term) {
        return index.get(term);
    }

    public Set<String> keySet() {
        return index.keySet();
    }

    public void printIndex() {
        // 검색어에 반복문을 실행한다.
        for (String term : keySet()) {
            System.out.println(term);

            // 단어별 등장하는 페이지와 등장 횟수를 표시한다.
            Set<TermCounter> tcs = get(term);
            for (TermCounter tc : tcs) {
                Integer count = tc.get(term);
                System.out.println(" " + tc.getLabel() + " " + count);
            }
        }
    }

    public void indexPage(String url, Elements paragraphs) {
        TermCounter tc = new TermCounter(url);
        tc.processElements(paragraphs);

        for (String term : tc.keySet()) {
            add(term, tc);
        }
    }

    public static void main(final String... args) throws IOException {
        WikiFetcher wf = new WikiFetcher();
        Index indexer = new Index();

        String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
        Elements paragraphs = wf.fetchWikipedia(url);
        indexer.indexPage(url, paragraphs);

        url = "https://en.wikipedia.org/wiki/Programming_language";
        paragraphs = wf.fetchWikipedia(url);
        indexer.indexPage(url, paragraphs);

        indexer.printIndex();

        Set<TermCounter> set = indexer.get("java");

        for (TermCounter tc : set) {
            final Integer javaCount = tc.get("java");
            final Integer programmingCount = tc.get("programming");
            System.out.println("java count: " + javaCount);
            System.out.println("programming count: " + programmingCount);
        }
    }

}
