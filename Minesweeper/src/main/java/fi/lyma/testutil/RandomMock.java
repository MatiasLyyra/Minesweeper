package fi.lyma.testutil;

import java.util.Random;

public class RandomMock extends Random {
    private int[] numbers;
    private int currentIndex;
    public RandomMock(int ... numbers) {
        this.numbers = numbers;
    }

    public RandomMock(int lowerBound, int upperBound) {
        int i = 0;
        numbers = new int[upperBound - lowerBound + 1];
        for(int j = lowerBound; j <= upperBound; ++j) {
            numbers[i] = j;
            i++;
        }
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
