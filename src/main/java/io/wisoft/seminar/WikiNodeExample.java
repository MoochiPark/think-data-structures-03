package io.wisoft.seminar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class WikiNodeExample {

    public static void main(final String... args) throws IOException {
        String url = "https://ko.wikipedia.org/wiki/%EC%9E%90%EB%B0%94_(%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D_%EC%96%B8%EC%96%B4)";

        // 문서를 다운로드하고 파싱하기
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();

        // 내용을 선택하고 단락 추출하기
        Element content = document.getElementById("mw-content-text");
        Elements paragraphs = content.select("p");
        final Element firstParagraph = paragraphs.first();
        final Iterable<Node> nodes = new WikiNodeIterable(firstParagraph);
        for (Node node : nodes) {
            if (node instanceof TextNode) {
                System.out.print(node);
            }
        }
        System.out.println();
        recursiveDFS(firstParagraph);
        System.out.println();
        iterativeDFS(firstParagraph);
    }

    private static void recursiveDFS(final Node node) {
        if (node instanceof TextNode) {
            System.out.print(node);
        }
        for (Node child : node.childNodes()) {
            recursiveDFS(child);
        }
    }

    private static void iterativeDFS(final Node root) {
        Deque<Node> stack = new ArrayDeque<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            Node node = stack.pop();
            if (node instanceof TextNode) {
                System.out.print(node);
            }

            List<Node> nodes = new ArrayList<>(node.childNodes());
            Collections.reverse(nodes);

            for (Node child : nodes) {
                stack.push(child);
            }
        }
    }

}
