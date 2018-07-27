package main.java.borysov.task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sun.org.apache.xpath.internal.operations.Div;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class JsoupFindByIdSnippet {

    private static final Logger LOGGER = Logger.getLogger(JsoupFindByIdSnippet.class);

    private static String CHARSET_NAME = "utf8";

    public static void main(String[] args) {
        String targetElementId = "Make everything OK"; // our button for search
        Document htmlFile = null;


        for (String filePath:args) {
            ArrayList<String> path = getPath(filePath, targetElementId, htmlFile);
            Collections.reverse(path);
            LOGGER.info("resourcePath - " + filePath);
            LOGGER.info(path);
        }

    }

    private static ArrayList<String> getPath(String resourcePath, String targetElementId, Document htmlFile) {
        try {
            htmlFile = Jsoup.parse(new File(resourcePath), "ISO-8859-1");
        } catch (IOException e) {
            LOGGER.error("Jsoup.parse error", e);
        }
        Elements elements = htmlFile.getElementsMatchingOwnText(targetElementId);
        ArrayList<String> path = new ArrayList<>();
        for (Element element : elements) {
            while (element.parent() != null) {
                path.add(element.tagName() + " > ");
                element = element.parent();
            }
        }
        return path;
    }

    private static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc.getElementById(targetElementId));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", e);
            return Optional.empty();
        }
    }
}