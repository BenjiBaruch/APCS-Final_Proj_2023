import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;

public class Dictionary {
    // Words from: https://github.com/dwyl/english-words
    HashSet<String> validWords;
    HashSet<String> usedWords;
    String[] promptList;

    public Dictionary() {
        loadWordList("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\words_alpha.txt");
        loadPromptList("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\prompts.txt");
        usedWords = new HashSet<>(50);
    }

    public void loadWordList(String path) {
        validWords = new HashSet<>(300000);
        try {
            // Modified from previous project (chess)
            BufferedReader in = new BufferedReader(new FileReader(path));
            String entry = in.readLine();
            while (entry != null) {
                validWords.add(entry.strip().toUpperCase());
                entry = in.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("I/O error");
        }
        System.out.println("Word List Length:" + validWords.size());
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
        System.out.println("Prompt List Length:" + validWords.size());
    }

    public boolean checkWord(String word, String prompt) {
        if (!validWords.contains(word.strip().toUpperCase())) return false;
        if (usedWords.contains(word.strip().toUpperCase())) return false;
        int p = 0;
        for (int i = 0; i < word.length(); i++) {
            if (prompt.charAt(p) == word.charAt(i) || prompt.charAt(p) == '_') {
                p++;
                if (p >= prompt.length()) {
                    usedWords.add(word);
                    return true;
                }
            } else {
                p = 0;
            }
        }
        return false;
    }

    public String randomPrompt(int round) {
        int index = (int)(Math.random() * Math.min(round+10, promptList.length));
        return promptList[index];
    }
}
