package org.example.utils;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.example.data.leapfrog.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class GameUtils {
    /**
     * Get the list game data with:
     * - key: game title
     * - value: all information of game
     *
     * @param gameDataList
     * @return
     */
    public static HashMap<String, GameData> mapGameDataByTitle(List<GameData> gameDataList) {
        return (HashMap<String, GameData>) gameDataList.stream()
                .collect(Collectors.toMap(
                        GameData::getTitle,
                        Function.identity()
                ));
    }

    /**
     * Compare 2 list game data and returns missing or mismatched entries by title.
     *
     * @param actualList
     * @param expectedList
     * @return a map with:
     * key "missing" - value: the game title
     * key "mismatch" - value: the actual and expected game data
     */
    public static HashMap<String, List<String>> getDifference(List<GameData> actualList, List<GameData> expectedList) {
        HashMap<String, GameData> actualGameMap = mapGameDataByTitle(actualList);
        List<String> mismatch = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        expectedList.forEach(expectedGame -> {
            GameData actualGame = actualGameMap.get(expectedGame.getTitle());
            if (actualGame == null) {
                missing.add(String.format("Title: %s", expectedGame.getTitle()));
            } else if (!expectedGame.equals(actualGame)) {
                mismatch.add(actualGame.getDifferentInfo(expectedGame));
            }
        });
        HashMap<String, List<String>> result = new HashMap<>();
        result.put("missing", missing);
        result.put("mismatch", mismatch);
        return result;
    }

    public static boolean areTwoGameListsMatched(List<GameData> actualList, List<GameData> expectedList) {
        HashMap<String, List<String>> map = getDifference(actualList, expectedList);
        boolean result = true;
        if (!map.get("missing").isEmpty()) {
            Allure.step(String.format("List %d games that does not appear on UI", map.get("missing").size()), () -> {
                String missingText = String.join("\n\n", map.get("missing"));
                Allure.attachment("Missing Details", missingText.trim());
            });
            result = false;
        }
        if (!map.get("mismatch").isEmpty()) {
            Allure.step(String.format("List %d games appear on UI but mismatch info", map.get("mismatch").size()), () -> {
                String mismatch = String.join("\n\n", map.get("mismatch"));
                Allure.attachment("Mismatch Details", mismatch.trim());
            });
            result = false;
        }
        return result;
    }

}
