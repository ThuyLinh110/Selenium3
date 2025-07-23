package org.example.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
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
        for (int i = 0; i < expectedList.size(); i++) {
            GameData expectedGame = expectedList.get(i);
            GameData actualGame = actualGameMap.get(expectedGame.getTitle());
            if (actualGame == null) {
                missing.add(expectedGame.getTitle());
            } else if (!expectedGame.equals(actualGame)) {
                mismatch.add(String.format("Expected: %s", expectedGame) + "\n" + String.format("Actual: %s", actualGame));
            }
        }
        HashMap<String, List<String>> result = new HashMap<>();
        result.put("missing", missing);
        result.put("mismatch", mismatch);
        return result;
    }

    public static boolean areTwoGameListsMatched(List<GameData> actualList, List<GameData> expectedList) {
        HashMap<String, List<String>> map = getDifference(actualList, expectedList);
        boolean result = true;
        if (!map.get("missing").isEmpty()) {
            Allure.step("List games that does not appear on UI", () -> {
                map.get("missing").forEach(e -> Allure.step(e, Status.FAILED));
            });
            result = false;
        }
        if (!map.get("mismatch").isEmpty()) {
            Allure.step("List games appear on UI but mismatch info", () -> {
                map.get("mismatch").forEach(e -> Allure.step(e, Status.FAILED));
            });
            result = false;
        }
        return result;
    }

}
