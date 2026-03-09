import java.util.Random;

public class DiceRoller {
    private final Random random;

    public DiceRoller() {
        this(new Random());
    }

    public DiceRoller(Random random) {
        this.random = random;
    }

    public int[] rollSixDice() {
        int[] dice = new int[6];
        for (int i = 0; i < dice.length; i++) {
            dice[i] = random.nextInt(6) + 1;
        }
        return dice;
    }
}
