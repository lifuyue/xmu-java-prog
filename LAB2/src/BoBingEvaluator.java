import java.util.ArrayList;
import java.util.List;

public class BoBingEvaluator {
    public RollResult evaluate(int[] dice) {
        validateDice(dice);

        int[] counts = buildCounts(dice);
        ChampionType championType = evaluateChampion(counts);
        if (championType != ChampionType.NONE) {
            return createResult(dice, PrizeType.ZHUANGYUAN, championType, PrizeType.ZHUANGYUAN);
        }

        if (isDuiTang(counts)) {
            return createResult(dice, PrizeType.DUITANG, ChampionType.NONE, PrizeType.DUITANG);
        }

        int countOfFour = counts[4];
        if (countOfFour == 3) {
            return createResult(dice, PrizeType.SANHONG, ChampionType.NONE, PrizeType.SANHONG);
        }

        int siJinNumber = findSiJinNumber(counts);
        if (siJinNumber != -1) {
            List<PrizeType> prizes = new ArrayList<>();
            prizes.add(PrizeType.SIJIN);
            if (countOfFour == 2) {
                prizes.add(PrizeType.ERJU);
            } else if (countOfFour == 1) {
                prizes.add(PrizeType.YIXIU);
            }
            return new RollResult(dice, PrizeType.SIJIN, ChampionType.NONE, prizes);
        }

        if (countOfFour == 2) {
            return createResult(dice, PrizeType.ERJU, ChampionType.NONE, PrizeType.ERJU);
        }

        if (countOfFour == 1) {
            return createResult(dice, PrizeType.YIXIU, ChampionType.NONE, PrizeType.YIXIU);
        }

        return new RollResult(dice, PrizeType.NONE, ChampionType.NONE, new ArrayList<PrizeType>());
    }

    private RollResult createResult(int[] dice, PrizeType primaryPrize, ChampionType championType,
            PrizeType singlePrize) {
        List<PrizeType> prizes = new ArrayList<>();
        prizes.add(singlePrize);
        return new RollResult(dice, primaryPrize, championType, prizes);
    }

    private void validateDice(int[] dice) {
        if (dice == null || dice.length != 6) {
            throw new IllegalArgumentException("必须提供 6 个骰子点数。");
        }
        for (int value : dice) {
            if (value < 1 || value > 6) {
                throw new IllegalArgumentException("骰子点数必须在 1 到 6 之间。");
            }
        }
    }

    private int[] buildCounts(int[] dice) {
        int[] counts = new int[7];
        for (int value : dice) {
            counts[value]++;
        }
        return counts;
    }

    private ChampionType evaluateChampion(int[] counts) {
        if (counts[4] == 6) {
            return ChampionType.LIU_BO_HONG;
        }

        if (hasSixOfAKind(counts, 2, 3, 5, 6)) {
            return ChampionType.LIU_BO_HEI;
        }

        if (counts[1] == 6) {
            return ChampionType.BIAN_DI_JIN;
        }

        if (counts[4] == 5) {
            return ChampionType.WU_WANG;
        }

        if (hasFiveOfAKind(counts, 2, 3, 5, 6)) {
            return ChampionType.WU_ZI;
        }

        if (counts[4] == 4 && counts[1] == 2) {
            return ChampionType.CHA_JIN_HUA;
        }

        if (counts[4] == 4) {
            return ChampionType.SI_HONG;
        }

        return ChampionType.NONE;
    }

    private boolean isDuiTang(int[] counts) {
        for (int value = 1; value <= 6; value++) {
            if (counts[value] != 1) {
                return false;
            }
        }
        return true;
    }

    private int findSiJinNumber(int[] counts) {
        for (int value = 1; value <= 6; value++) {
            if (value != 4 && counts[value] == 4) {
                return value;
            }
        }
        return -1;
    }

    private boolean hasFiveOfAKind(int[] counts, int... values) {
        for (int value : values) {
            if (counts[value] == 5) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSixOfAKind(int[] counts, int... values) {
        for (int value : values) {
            if (counts[value] == 6) {
                return true;
            }
        }
        return false;
    }
}
