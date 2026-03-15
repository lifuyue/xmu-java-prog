import java.util.ArrayList;
import java.util.List;

/**
 * 博饼判奖器。
 * <p>
 * 这个类只负责一件事：接收 6 个骰子点数，然后严格按照题目要求的优先级，
 * 判断这组点数命中了什么奖项。
 * <p>
 * 判奖顺序非常关键，顺序错了就会误判。例如：
 * - “五子”如果放在“四进”后面判断，就可能被错误拆成“四进 + 一秀”；
 * - “状元插金花”如果放在普通“四红”后面判断，就会被低优先级规则提前截住。
 * <p>
 * 所以本类的核心思想是：
 * 1. 先统计每个点数出现了几次；
 * 2. 再按优先级从高到低逐项判断；
 * 3. 一旦命中高优先级奖项，立即返回结果。
 */
public class BoBingEvaluator {
    /**
     * 对一次掷骰结果进行判奖。
     *
     * @param dice 长度必须为 6 的骰子数组，每个元素必须在 1~6
     * @return 判奖结果对象，里面既包含主奖项，也包含可能的组合奖项
     */
    public RollResult evaluate(int[] dice) {
        validateDice(dice);

        // counts[value] 表示点数 value 出现了多少次。
        // 例如 counts[4] == 3，表示 4 点出现了 3 次，也就是“三红”。
        int[] counts = buildCounts(dice);

        // 第一步：先判断所有状元档。
        // 因为状元档优先级最高，一旦命中，就不允许继续往下判普通奖项。
        ChampionType championType = evaluateChampion(counts);
        if (championType != ChampionType.NONE) {
            return createResult(dice, PrizeType.ZHUANGYUAN, championType, PrizeType.ZHUANGYUAN);
        }

        // 第二步：判断对堂，即 1~6 各出现一次。
        if (isDuiTang(counts)) {
            return createResult(dice, PrizeType.DUITANG, ChampionType.NONE, PrizeType.DUITANG);
        }

        // 第三步：判断三红，即 4 点恰好出现 3 次。
        int countOfFour = counts[4];
        if (countOfFour == 3) {
            return createResult(dice, PrizeType.SANHONG, ChampionType.NONE, PrizeType.SANHONG);
        }

        // 第四步：判断四进。
        // 四进要求“有某个非 4 点数出现 4 次”。
        // 如果同时还出现 1 个 4，则附带一秀；
        // 如果同时还出现 2 个 4，则附带二举。
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

        // 第五步：判断二举，即两个 4。
        if (countOfFour == 2) {
            return createResult(dice, PrizeType.ERJU, ChampionType.NONE, PrizeType.ERJU);
        }

        // 第六步：判断一秀，即一个 4。
        if (countOfFour == 1) {
            return createResult(dice, PrizeType.YIXIU, ChampionType.NONE, PrizeType.YIXIU);
        }

        // 最后仍然没有命中，则为无奖。
        return new RollResult(dice, PrizeType.NONE, ChampionType.NONE, new ArrayList<PrizeType>());
    }

    /**
     * 创建“只有一个奖项”的常规结果。
     * 这样可以避免在多处重复创建列表的样板代码。
     */
    private RollResult createResult(int[] dice, PrizeType primaryPrize, ChampionType championType,
            PrizeType singlePrize) {
        List<PrizeType> prizes = new ArrayList<>();
        prizes.add(singlePrize);
        return new RollResult(dice, primaryPrize, championType, prizes);
    }

    /**
     * 校验输入是否合法。
     * 如果不是 6 个骰子，或者点数不在 1~6，就直接抛异常，避免后续逻辑建立在错误数据上。
     */
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

    /**
     * 将骰子数组转成“频次统计数组”。
     * <p>
     * 下标 0 不使用；
     * counts[1] ~ counts[6] 分别表示 1 点到 6 点的出现次数。
     */
    private int[] buildCounts(int[] dice) {
        int[] counts = new int[7];
        for (int value : dice) {
            counts[value]++;
        }
        return counts;
    }

    /**
     * 判断是否命中状元档。
     * <p>
     * 状元档按高优先级情况单独处理，返回具体的状元细分类型。
     * 如果没有命中状元档，则返回 {@link ChampionType#NONE}。
     */
    private ChampionType evaluateChampion(int[] counts) {
        // 六个 4，最高档之一：六勃红。
        if (counts[4] == 6) {
            return ChampionType.LIU_BO_HONG;
        }

        // 六个相同且不是 1、4 的情况：六勃黑。
        if (hasSixOfAKind(counts, 2, 3, 5, 6)) {
            return ChampionType.LIU_BO_HEI;
        }

        // 六个 1：遍地锦。
        if (counts[1] == 6) {
            return ChampionType.BIAN_DI_JIN;
        }

        // 五个 4：五王。
        if (counts[4] == 5) {
            return ChampionType.WU_WANG;
        }

        // 五个相同且不是 1、4：五子。
        if (hasFiveOfAKind(counts, 2, 3, 5, 6)) {
            return ChampionType.WU_ZI;
        }

        // 四个 4 加两个 1：状元插金花。
        if (counts[4] == 4 && counts[1] == 2) {
            return ChampionType.CHA_JIN_HUA;
        }

        // 四个 4：四红。
        if (counts[4] == 4) {
            return ChampionType.SI_HONG;
        }

        return ChampionType.NONE;
    }

    /**
     * 判断是否为对堂：1、2、3、4、5、6 各出现一次。
     */
    private boolean isDuiTang(int[] counts) {
        for (int value = 1; value <= 6; value++) {
            if (counts[value] != 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查找是否存在“四进”。
     *
     * @return 如果存在某个非 4 点数恰好出现 4 次，则返回这个点数；否则返回 -1
     */
    private int findSiJinNumber(int[] counts) {
        for (int value = 1; value <= 6; value++) {
            if (value != 4 && counts[value] == 4) {
                return value;
            }
        }
        return -1;
    }

    /**
     * 判断指定候选点数中，是否存在“五个相同”的情况。
     */
    private boolean hasFiveOfAKind(int[] counts, int... values) {
        for (int value : values) {
            if (counts[value] == 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断指定候选点数中，是否存在“六个相同”的情况。
     */
    private boolean hasSixOfAKind(int[] counts, int... values) {
        for (int value : values) {
            if (counts[value] == 6) {
                return true;
            }
        }
        return false;
    }
}
