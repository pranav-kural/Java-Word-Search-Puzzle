import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author krupa
 */
public class WordSearch {

    private String[] wordList;
    private String[][] wordPuzzle;
    private SecureRandom randomGenerator = new SecureRandom();
    private Scanner input = new Scanner(System.in);

    public WordSearch() {
        int numberOfWords = getNumberOfWords();
        wordList = new String[numberOfWords];
        wordPuzzle = new String[numberOfWords][numberOfWords];
        for (String[] row : wordPuzzle) {
            Arrays.fill(row, " ");
        }
    }

    public static void main(String[] args) {


        System.out.println(args[0]);

        WordSearch newSearch = new WordSearch();
        newSearch.getWords();
        newSearch.addWordsToPuzzle();
        newSearch.fillPuzzle();
        String puzzle = newSearch.displayPuzzle();
        String wordsToFind = newSearch.wordsToFind();
        System.out.printf("%n %s %n %s %n", puzzle, wordsToFind);
        newSearch.writeToFile(puzzle + "\n" + wordsToFind);
    }

    private int getNumberOfWords() {
        int numberOfWords = 0;
        boolean flag = false;
        do {
            try {
                System.out.printf("\nHow many words you would like to add?: ");
                numberOfWords = Integer.parseInt(input.nextLine());
                flag = false;
            } catch (NumberFormatException e) {
                System.out.println(e + ": You didn't entered a number. Please enter a valid number.");
                flag = true;
            } finally {
                if (!flag && numberOfWords < 5 || numberOfWords > 20) {
                    System.out.printf("\nThe number you entered is " + (numberOfWords < 5 ? "too small" : "too large") + ".\nYou need to enter minimum 5 words and a maximum of 20.");
                    flag = true;
                }
            }
        } while (flag);
        return numberOfWords;
    }

    private void getWords() {
        System.out.println("Enter the name of words: ");
        for (int i = 0; i < wordList.length ; i++) {
            boolean flag = true;
            while (flag){
                try {
                    System.out.printf("\n Enter the name of the " + (i + 1) + (String.valueOf(i + 1).contains("1") ? "st" : (String.valueOf(i + 1).contains("2") ? "nd" : (String.valueOf(i + 1).contains("3") ? "rd" : (((String.valueOf(i + 1).contains("7")) || (String.valueOf(i + 1).contains("9")) || (String.valueOf(i + 1).contains("0"))) ? "nth" : "th")))) + " word: ");
                    String wordName = input.nextLine();
                    //using if..else method to imply the exceptions so that the word puzzle build up is strong
                    if (wordName.contains(" ")) {
                        // if the word entered contains space or more then one word
                        throw new IllegalArgumentException("Name of the word cannot contain a space. It must be one word.");
                    } else if (wordName.length() > wordPuzzle.length) {
                        // if the input is too long
                        throw new IllegalArgumentException("Name of the word cannot be greater then " + wordPuzzle.length+ " letters");
                    } else if (wordName.length() < 2) {
                        // if its less than 2 letters
                        throw new IllegalArgumentException("Name of the word cannot be less then 2 letters");
                    } else if (Arrays.asList(wordList).contains(wordName)) {
                        // check if the name entered already exists in the array.
                        throw new IllegalArgumentException("This word already exists in the list. Please enter a new word.");
                    } else {
                        wordList[i] = wordName;
                        flag = false;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println(e);

                }
            }
        }
        Arrays.sort(wordList);
    }

    private void addWordsToPuzzle() {
        for (int col = 0; col < wordPuzzle.length; col++) {
            int rowNumber = randomGenerator.nextInt(wordPuzzle.length - (wordList[col].length() - 1));
            String[] placeLetters = wordList[col].split("");
            for (String placeLetter : placeLetters) {
                if (rowNumber < wordPuzzle.length) {
                    wordPuzzle[rowNumber][col] = placeLetter;
                    rowNumber++;
                }
            }
        }
    }



    public void fillPuzzle() {
        char[] letters = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
        for (String[] row: wordPuzzle) {
            for (int col = 0; col < row.length; col++) {
                if (row[col].equals(" ")) {
                    row[col] = String.valueOf(letters[randomGenerator.nextInt(letters.length)]);
                }
            }
        }
    }

    public String displayPuzzle() {
        String result = "\n";
        for (String[] row : wordPuzzle) {
            for (int col = 0; col < wordPuzzle.length; col++) {
                result += (col != row.length - 1) ? row[col] + " | " : row[col];

            }
            result += "\n";
        }
        return result;
    }

    public String wordsToFind() {
        String result = "";
        result += "\nHere are the list of places to find:\n";
        for (String place: wordList) {
            result += place + "\n";
        }
        return result;
    }

    public void writeToFile(String text) {
        BufferedWriter output = null;
        try {
            File file = new File("myWordPuzzle.txt");
            output = new BufferedWriter(new FileWriter(file));
            output.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
