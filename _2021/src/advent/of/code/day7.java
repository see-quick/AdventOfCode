package advent.of.code;

public class day7 {

    public static void main(String[] args) {
        String input = Utils.constructStringFromFile("day7.txt");
        System.out.println(input);

        String[] numbers = input.split(",");

        int best = Integer.MAX_VALUE;

        for (int i = 0; i < 1728; i++) {
            int currentSumFuelCost = 0;
            for (int j = 0; j < numbers.length; j++) {
                int fuelCost = Math.abs(Integer.parseInt(numbers[j].trim()) - i);
                currentSumFuelCost += fuelCost;
            }
            System.out.println("for (" + i + ".) destination is: " + currentSumFuelCost);
            if (best > currentSumFuelCost) {
                best = currentSumFuelCost;
            }
        }
        System.out.println(best);
    }
}
