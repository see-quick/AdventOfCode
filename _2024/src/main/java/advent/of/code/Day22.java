package advent.of.code;

import java.util.List;

public class Day22 {

    private static final int STEPS = 2000;
    private static final long MOD = 16777216;

    public static void main(String[] args) {
        List<String> input = DataLoader.loadDataFromFile("_2024/src/main/java/advent/of/code/day22.txt");

        long sum = 0;
        for (String stringNumber : input) {
            long s = Long.parseLong(stringNumber);
            for(int i = 0; i < STEPS; i++){
                s ^= (s * 64);
                s %= MOD;
                s ^= (s / 32);
                s %= MOD;
                s ^= (s * 2048);
                s %= MOD;
            }
            sum += s;
        }
        System.out.println(sum);
    }
}
