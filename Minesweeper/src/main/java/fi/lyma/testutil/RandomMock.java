package fi.lyma.testutil;

import java.util.Random;

/**
 * Class used in unit test for {@link fi.lyma.minesweeper.logic.Minefield} for seeding predetermined locations for mines.
 */
public class RandomMock extends Random {
    private int[] numbers;
    private int currentIndex;

    /**
     * Constructs {@link RandomMock} with given numbers that are used to give "random" numbers.
     *
     * @param numbers Numbers that are returned from nextInt() and nextInt(int)
     */
    public RandomMock(int... numbers) {
        this.numbers = numbers;
    }

    @Override
    public int nextInt(int i) {
        return nextInt();
    }

    @Override
    public int nextInt() {
        int number = numbers[currentIndex++];
        if (currentIndex >= numbers.length) {
            currentIndex = 0;
        }
        return number;
    }
}
