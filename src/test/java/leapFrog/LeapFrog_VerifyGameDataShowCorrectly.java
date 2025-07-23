package leapFrog;

import org.example.data.leapfrog.GameData;
import org.example.scraper.LeapFrogScraper;
import org.example.utils.Assertion;
import org.example.utils.Constants;
import org.example.utils.ExcelUtils;
import org.example.utils.GameUtils;
import org.testng.annotations.Test;

import java.util.List;

public class LeapFrog_VerifyGameDataShowCorrectly {

    @Test(groups = {"regression"}, description = "LeapFrog - Verify game date show correctly")
    public void leapFrod_VerifyGameDataShowCorrectly() {
        gameDataList = leapFrogScraper.getAllGameData();
        gameDataList1 = ExcelUtils.readGameDataFromExcel(Constants.LEAPFROG_DATA_PATH);
        Assertion.assertTrue(GameUtils.areTwoGameListsMatched(gameDataList, gameDataList1), "Verify 2 list are equal");

        Assertion.assertAll("Complete running test case");
    }

    LeapFrogScraper leapFrogScraper = new LeapFrogScraper();
    List<GameData> gameDataList, gameDataList1;

}
