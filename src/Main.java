package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Set<String> wordSet;
    private final static Set<String> oneLetterWords = new HashSet<>(Arrays.asList("A", "I"));

    public static void main(String[] args) throws IOException {
        wordSet = loadAllWords();

        long startTime = System.nanoTime();
        List<String> reducibleWords = findReducibleWords();
        long endTime = System.nanoTime();
        long findWordsDuration = (endTime - startTime) / 1_000_000;
        System.out.println("Time until words are found: " + findWordsDuration + " ms");

        System.out.println("Total count of words: " + reducibleWords.size());
        System.out.println("9-letter words that can be reduced to 1 letter: ");

        for (String word : reducibleWords) {
            System.out.println(word);
        }
    }

    private static boolean canReduceToSingleLetter(String word, Set<String> memo) {
        if (word.length() == 1) {
            return oneLetterWords.contains(word);
        }
        if (memo.contains(word)) {
            return false;
        }
        if (!wordSet.contains(word)) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            String reducedWord = word.substring(0, i) + word.substring(i + 1);
            if (canReduceToSingleLetter(reducedWord, memo)) {
                return true;
            }
        }
        memo.add(word);
        return false;
    }

    private static List<String> findReducibleWords() {
        List<String> result = new ArrayList<>();
        Set<String> memo = new HashSet<>();
        for (String word : wordSet) {
            if (word.length() == 9 && canReduceToSingleLetter(word, memo)) {
                result.add(word);
            }
        }
        return result;
    }

    public static Set<String> loadAllWords() throws IOException {
        URL wordsUrl = new URL("https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {
            return br.lines().skip(2).collect(Collectors.toSet());
        }
    }
}




