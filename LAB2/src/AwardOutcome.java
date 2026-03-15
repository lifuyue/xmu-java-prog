import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 表示“本轮掷骰结束后，奖池实际处理的结果”。
 * <p>
 * 注意这里区分了两层含义：
 * 1. {@link RollResult} 表示“规则上命中了什么奖”；
 * 2. {@code AwardOutcome} 表示“结合库存以后，最终实际发出了什么奖”。
 * <p>
 * 例如：
 * - 玩家命中了“二举”；
 * - 但如果奖池里的“二举”已经发完了，那么规则上算命中，实际上却拿不到；
 * - 这时命中结果放在 {@link RollResult} 中，库存处理结果放在本类中。
 */
public class AwardOutcome {
    /**
     * 本轮真正发出去的奖项类别。
     * 这个列表主要给程序内部统计使用，例如统计某位玩家拿了几次“一秀”。
     */
    private final List<PrizeType> grantedPrizeTypes = new ArrayList<>();

    /**
     * 本轮真正发出去的奖项显示文字。
     * 这个列表主要给控制台输出使用，例如“状元（五王）”“四进”等。
     */
    private final List<String> grantedPrizeLabels = new ArrayList<>();

    /**
     * 本轮命中了但库存已经耗尽的奖项显示文字。
     * 程序会据此提示用户：“命中但奖项已发完，不得奖”。
     */
    private final List<String> exhaustedPrizeLabels = new ArrayList<>();

    /**
     * 记录一个“成功发放”的奖项。
     *
     * @param prizeType 奖项类型，供程序统计使用
     * @param label 奖项展示文字，供控制台输出使用
     */
    public void addGrantedPrize(PrizeType prizeType, String label) {
        grantedPrizeTypes.add(prizeType);
        grantedPrizeLabels.add(label);
    }

    /**
     * 记录一个“命中但库存耗尽”的奖项。
     *
     * @param label 奖项展示文字
     */
    public void addExhaustedPrize(String label) {
        exhaustedPrizeLabels.add(label);
    }

    /**
     * 返回本轮实际发出去的奖项类型列表。
     * 使用不可修改视图，避免调用方误改内部数据。
     */
    public List<PrizeType> getGrantedPrizeTypes() {
        return Collections.unmodifiableList(grantedPrizeTypes);
    }

    /**
     * 返回本轮实际发出去的奖项名称列表。
     */
    public List<String> getGrantedPrizeLabels() {
        return Collections.unmodifiableList(grantedPrizeLabels);
    }

    /**
     * 返回本轮命中但库存不足的奖项名称列表。
     */
    public List<String> getExhaustedPrizeLabels() {
        return Collections.unmodifiableList(exhaustedPrizeLabels);
    }

    /**
     * 是否至少成功发放了一个奖项。
     */
    public boolean hasGrantedPrize() {
        return !grantedPrizeLabels.isEmpty();
    }

    /**
     * 是否存在“命中但库存耗尽”的情况。
     */
    public boolean hasExhaustedPrize() {
        return !exhaustedPrizeLabels.isEmpty();
    }
}
