package com.ABH.Auction.utility;

import java.util.Arrays;

public class LevenshteinDistance {

    public static int calculate(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int replace = dp[i - 1][j - 1]
                            + numOfReplacement(str1.charAt(i - 1), str2.charAt(j - 1));
                    int delete = dp[i - 1][j] + 1;
                    int insert = dp[i][j - 1] + 1;
                    dp[i][j] = minEdits(replace, delete, insert);
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }

    private static int numOfReplacement(char c1, char c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    private static int minEdits(int... nums)
    {
        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }
}
