import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 保存一次素数实验的统计结果。
 */
public class PrimeRunStats {
    private final int testedCount;
    private final int primeCount;
    private final int totalChecks;
    private final List<Integer> primes;

    /**
     * 构造一次实验的统计结果对象。
     * <p>
     * 这里会把传入的素数列表复制一份保存下来，
     * 避免外部修改原列表后影响本对象中的数据。
     *
     * @param testedCount 本次一共测试了多少个整数
     * @param primeCount 本次一共找到了多少个素数
     * @param totalChecks 本次累计做了多少次试除判断
     * @param primes 本次找到的全部素数列表
     */
    public PrimeRunStats(int testedCount, int primeCount, int totalChecks, List<Integer> primes) {
        this.testedCount = testedCount;
        this.primeCount = primeCount;
        this.totalChecks = totalChecks;
        this.primes = new ArrayList<>(primes);
    }

    /**
     * 返回本次实验被测试的整数个数。
     *
     * @return 被测试的整数个数
     */
    public int getTestedCount() {
        return testedCount;
    }

    /**
     * 返回本次实验找到的素数个数。
     *
     * @return 素数个数
     */
    public int getPrimeCount() {
        return primeCount;
    }

    /**
     * 返回本次实验累计进行了多少次试除。
     * <p>
     * 这个值可以用来比较不同算法的效率差异。
     *
     * @return 总试除次数
     */
    public int getTotalChecks() {
        return totalChecks;
    }

    /**
     * 返回只读的素数列表。
     * <p>
     * 这里使用不可修改视图，避免外部代码直接改动内部保存的数据。
     *
     * @return 不可修改的素数列表
     */
    public List<Integer> getPrimes() {
        return Collections.unmodifiableList(primes);
    }
}
