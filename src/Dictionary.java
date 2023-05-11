import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;

public class Dictionary {
    // Words from: https://github.com/dwyl/english-words
    HashSet<String> wordList;
    String[] promptList;

    public Dictionary() {
        loadWordList("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\words_alpha.txt");
        loadPromptList("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\prompts.txt");
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

    public void loadPromptList(String path) {
        promptList = new String[77];
        int head = 0;
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String entry = in.readLine();
            while (entry != null) {
                promptList[head++] = entry;
                entry = in.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("I/O error");
        }
        System.out.println("Prompt List Length:" + wordList.size());
    }

    public boolean checkWord(String word) {
        return wordList.contains(word.strip().toLowerCase());
    }

    public String randomPrompt(int round) {
        int index = (int)(Math.random() * Math.min(round+10, promptList.length));
        return promptList[index];
    }
}
