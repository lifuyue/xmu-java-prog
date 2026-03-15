import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 表示“一次掷骰经过规则判定后的结果”。
 * <p>
 * 这个类只描述“规则上命中了什么”，不处理库存。
 * 例如玩家命中“二举”，即使奖池里二举已经发完，这里仍然会记录为命中“二举”。
 * 真正是否发得出去，由 {@link PrizePool} 再做判断。
 */
public class RollResult {
    /**
     * 本次掷出的 6 个骰子点数。
     */
    private final int[] dice;

    /**
     * 本次命中的主奖项。
     * 对于组合奖，例如“四进 + 二举”，主奖项仍然记为“四进”。
     */
    private final PrizeType primaryPrize;

    /**
     * 如果主奖项是状元，则这里记录具体的状元类型；
     * 否则为 {@link ChampionType#NONE}。
     */
    private final ChampionType championType;

    /**
     * 本次实际命中的所有奖项。
     * 普通情况下只有一个元素；
     * 组合奖时可能有多个元素，例如“四进 + 一秀”。
     */
    private final List<PrizeType> hitPrizes;

    public RollResult(int[] dice, PrizeType primaryPrize, ChampionType championType, List<PrizeType> hitPrizes) {
        // 使用拷贝而不是直接引用，避免外部修改传入数组影响内部状态。
        this.dice = Arrays.copyOf(dice, dice.length);
        this.primaryPrize = primaryPrize;
        this.championType = championType;

        // 使用不可修改列表，保证结果对象一旦创建后不被外部篡改。
        this.hitPrizes = Collections.unmodifiableList(new ArrayList<>(hitPrizes));
    }

    /**
     * 返回本次骰子点数的副本。
     */
    public int[] getDice() {
        return Arrays.copyOf(dice, dice.length);
    }

    /**
     * 返回主奖项。
     */
    public PrizeType getPrimaryPrize() {
        return primaryPrize;
    }

    /**
     * 返回状元细分类别。
     */
    public ChampionType getChampionType() {
        return championType;
    }

    /**
     * 返回本次命中的所有奖项。
     */
    public List<PrizeType> getHitPrizes() {
        return hitPrizes;
    }

    /**
     * 是否命中了任意奖项。
     */
    public boolean hasPrize() {
        return primaryPrize != PrizeType.NONE;
    }

    /**
     * 生成适合直接展示给用户看的结果描述。
     * 例如：
     * - 状元档：五王
     * - 四进 + 一秀
     * - 无奖
     */
    public String getResultDescription() {
        if (primaryPrize == PrizeType.ZHUANGYUAN) {
            return "状元档：" + championType.getDisplayName();
        }
        if (hitPrizes.isEmpty()) {
            return PrizeType.NONE.getDisplayName();
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < hitPrizes.size(); i++) {
            if (i > 0) {
                builder.append(" + ");
            }
            builder.append(hitPrizes.get(i).getDisplayName());
        }
        return builder.toString();
    }

    /**
     * 根据奖项类型返回用于展示的标签。
     * <p>
     * 状元需要带上细分说明，所以会格式化成“状元（五王）”；
     * 其他奖项直接返回奖项名称。
     */
    public String getDisplayLabelForPrize(PrizeType prizeType) {
        if (prizeType == PrizeType.ZHUANGYUAN) {
            return PrizeType.ZHUANGYUAN.getDisplayName() + "（" + championType.getDisplayName() + "）";
        }
        return prizeType.getDisplayName();
    }
}
