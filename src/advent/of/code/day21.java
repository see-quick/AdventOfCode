package advent.of.code;

import java.util.Arrays;
import java.util.List;

public class day21 {

    public static class Player {
        public int space;
        public int score;

        public Player(int space) {
            this.space = space;
            this.score = 0;
        }
    }

    static class Dice {
        // modified spaces
        final int[] positions = new int[]{10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int diceRolls;
        int counter;

        public Dice() {
            this.counter = 1;
            this.diceRolls = 0;
        }

        public int getThreeRolls() {
            return ((diceRolls % 100) + 1) + ((diceRolls % 100) + 2) + ((diceRolls % 100) +3);
        }

        public int rollThreeTimes(int playerSumDice) {
            for (int i = 0; i < 3; i++) {
                playerSumDice += this.getThreeRolls();
            }
            return playerSumDice;
        }

        public int returnSpaceByIndex(int currentSpace) {
            return positions[currentSpace];
        }
    }

    public static void main(String[] args) {
        final String input = Utils.constructStringFromFile("day21.txt");

        System.out.println(input);

        final String[] lines = input.split("\n");
        final int playerOneInitPos = Integer.parseInt(lines[0].split(" ")[4].trim());
        final int playerSecondInitPos = Integer.parseInt(lines[1].split(" ")[4].trim());

        System.out.println(playerOneInitPos);
        System.out.println(playerSecondInitPos);

        // II. algorithm...
        Dice dice = new Dice();
        boolean exit = false;

        Player playerOne = new Player(playerOneInitPos);
        Player playerTwo = new Player(playerSecondInitPos);
        List<Player> players = Arrays.asList(playerOne, playerTwo);

        do {
            for (Player player : players) {
                int spacesToMove = dice.getThreeRolls();
                player.space = dice.returnSpaceByIndex(((player.space + spacesToMove) % 10));
                player.score += player.space;

                dice.diceRolls += 3;

                if (player.score >= 1000) {
                    exit = true;
                    break;
                }
            }
        } while (!exit);

        System.out.println("Dice rolls: " + dice.diceRolls);
        System.out.println("Result: " + (dice.diceRolls * Math.min(players.get(0).score, players.get(1).score)));
    }
}
