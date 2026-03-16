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

    public PrimeRunStats(int testedCount, int primeCount, int totalChecks, List<Integer> primes) {
        this.testedCount = testedCount;
        this.primeCount = primeCount;
        this.totalChecks = totalChecks;
        this.primes = new ArrayList<>(primes);
    }

    public int getTestedCount() {
        return testedCount;
    }

    public int getPrimeCount() {
        return primeCount;
    }

    public int getTotalChecks() {
        return totalChecks;
    }

    public List<Integer> getPrimes() {
        return Collections.unmodifiableList(primes);
    }
}
