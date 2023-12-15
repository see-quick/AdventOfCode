package advent.of.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day7 {

    public static void main(String[] args) {
        String[] hands = {
                "32T3K 765", "T55J5 684", "KK677 28", "KTJJT 220", "QQQJA 483"
        };
        List<Hand> handList = new ArrayList<>();
        for (String handString : hands) {
            String[] parts = handString.split(" ");
            handList.add(new Hand(parts[0], Integer.parseInt(parts[1])));
        }
        Collections.sort(handList);
        int totalWinnings = 0;
        for (int i = 0; i < handList.size(); i++) {
            totalWinnings += handList.get(i).getBid() * (handList.size() - i);
        }
        System.out.println("Total winnings: " + totalWinnings);
    }

    static class Hand implements Comparable<Hand> {
        String cards;
        int bid;
        int type;
        int[] cardRanks;

        Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;
            this.cardRanks = new int[5];
            Map<Character, Integer> cardCounts = new HashMap<>();
            for (int i = 0; i < 5; i++) {
                char c = cards.charAt(i);
                cardCounts.put(c, cardCounts.getOrDefault(c, 0) + 1);
                cardRanks[i] = "A23456789TJQK".indexOf(c);
            }
            int maxCount = Collections.max(cardCounts.values());
            this.type = maxCount - 1;
            Arrays.sort(cardRanks);
        }

        int getBid() {
            return bid;
        }

        @Override
        public int compareTo(Hand other) {
            if (this.type != other.type) {
                return Integer.compare(other.type, this.type);
            }
            for (int i = 4; i >= 0; i--) {
                if (this.cardRanks[i] != other.cardRanks[i]) {
                    return Integer.compare(other.cardRanks[i], this.cardRanks[i]);
                }
            }
            return 0;
        }
    }
}
