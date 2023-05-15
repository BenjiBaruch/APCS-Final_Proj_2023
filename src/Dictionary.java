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
                wordList.add(entry.strip().toUpperCase());
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

    public boolean checkWord(String word, String prompt) {
        if (!wordList.contains(word.strip().toUpperCase())) return false;
        int p = 0;
        for (int i = 0; i < word.length(); i++) {
            if (prompt.charAt(p) == word.charAt(i) || prompt.charAt(p) == '_') {
                p++;
                if (p >= word.length()-1) {
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
