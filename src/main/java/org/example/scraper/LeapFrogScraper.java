package org.example.scraper;

import lombok.extern.slf4j.Slf4j;
import org.example.data.leapfrog.GameData;
import org.example.utils.Constants;
import org.example.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LeapFrogScraper {

    /**
     * Get total page
     *
     * @return
     */
    public int getTotalPages() {
        String body = HttpUtils.sendGetRequest(String.format(Constants.LEAPFROG_URL, 1));
        Document doc = Jsoup.parse(body);
        Element selectedOption = doc.selectFirst("div.paginator.top option[selected]");
        if (selectedOption != null) {
            Matcher matcher = Pattern.compile("Page \\d+ of (\\d+)").matcher(selectedOption.text());
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        return 1;
    }

    /**
     * Get all game data for specific page
     *
     * @param pageNumber the page number
     * @return
     */
    public List<GameData> getAllGameDataFromPageNumber(int pageNumber) {
        String body = HttpUtils.sendGetRequest(String.format(Constants.LEAPFROG_URL, pageNumber));
        Document doc = Jsoup.parse(body);
        Elements items = doc.select(".resultList .catalog-product");
        List<GameData> gameDataList = new ArrayList<>();
        for (Element item : items) {
            String title = item.selectFirst("p.heading").text().trim();
            String age = item.selectFirst("p.ageDisplay").text().trim().replaceAll("\\s*-\\s*", "-");
            String price = item.selectFirst("span.single.price:not(.strike)").text().trim().replace("Price: ", "").trim();
            gameDataList.add(new GameData(title, age, price));
        }
        return gameDataList;
    }

    /**
     * Get all game data for all pages
     *
     * @return
     */
    public List<GameData> getAllGameData() {
        int totalPages = getTotalPages();
        ExecutorService executor = Executors.newFixedThreadPool(totalPages);
        List<Future<List<GameData>>> futures = new ArrayList<>();

        for (int page = 1; page <= totalPages; page++) {
            int finalPage = page;
            futures.add(executor.submit(() -> getAllGameDataFromPageNumber(finalPage)));
        }

        List<GameData> allGames = new ArrayList<>();
        for (Future<List<GameData>> future : futures) {
            try {
                List<GameData> games = future.get();
                allGames.addAll(games);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted: " + e.getMessage());
            } catch (ExecutionException e) {
                log.error("Error occurred while fetching page: " + e.getCause().getMessage());
            }
        }
        executor.shutdown();
        return allGames;
    }
}
