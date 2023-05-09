import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;

public class Dictionary {
    // Words from: https://github.com/dwyl/english-words
    HashSet<String> wordList;

    public Dictionary() {
        loadWordList("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\words_alpha.txt");
    }

    public void loadWordList(String path) {
        wordList = new HashSet<>(300000);
        try {
            // Modified from previous project (chess)
            BufferedReader in = new BufferedReader(new FileReader(path));
            String entry = in.readLine();
            while (entry != null) {
                wordList.add(entry.strip().toLowerCase());
                entry = in.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("I/O error");
        }
        System.out.println("Word List Length:" + wordList.size());
    }

    public boolean checkWord(String word) {
        return wordList.contains(word.strip().toLowerCase());
    }
}
