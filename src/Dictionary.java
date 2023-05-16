import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

public class Dictionary {
    // Words from: https://github.com/dwyl/english-words
    HashSet<String> validWords;
    HashSet<String> usedWords;
    String[] promptList;

    public Dictionary() {
        loadWordList("data/words_alpha.txt");
        // loadPromptList("C:\\Users\\ultra\\IdeaProjects\\APCS Final Proj 2023\\data\\prompts.txt");
        autoPromptList("data/20k.txt");
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
    public void autoPromptList(String path) {
        System.out.println("why");

        String[] easyWordList = new String[20005];
        int head = 0;
        try {
            // Modified from previous project (chess)
            BufferedReader in = new BufferedReader(new FileReader(path));
            String entry = in.readLine();
            while (entry != null) {
                System.out.println(head);
                entry = entry.strip().toUpperCase();
                boolean valid = true;
                if (entry.length() < 5) valid = false;
                if (valid && !validWords.contains(entry)) valid = false;
                if (valid) for (int i = 0; i < entry.length(); i++) switch (entry.charAt(i)) {
                    case '_' | '-' | '\'' | ' ':
                        valid = false;
                        break;
                }
                if (valid) easyWordList[head++] = entry;
                entry = in.readLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("I/O error");
        }
        ArrayList<String>[] prompts = new ArrayList[8];
        for (int i = 0; i < 8; i++) prompts[i] = new ArrayList<>(10);

        System.out.println("please god");

        boolean complete = false;
        while (!complete) {
            String prompt = easyWordList[(int)(Math.random()*head)];
            int len = (int)(Math.random()*3)+2;
            int offset = (int)(Math.random() * (prompt.length() - len));
            prompt = prompt.substring(offset, offset + len);
            if ((int)(Math.random()*4) == 0) {
                int blank = (int)(Math.random()*len);
                prompt = prompt.substring(0, blank-1) + '_' + prompt.substring(blank);
            }
            int count = 0;
            Count:
            for (int j = 0; j < head; j++) {
                int p = 0;
                for (int i = 0; i < easyWordList[j].length(); i++) {
                    if (prompt.charAt(p) == easyWordList[j].charAt(i) || prompt.charAt(p) == '_') {
                        p++;
                        if (p >= prompt.length()) {
                            count++;
                            continue Count;
                        }
                    } else {
                        p = 0;
                    }
                }
            }
            int level;
            if (count < 10) continue;
            else if (count > 2000) level = 0;
            else if (count > 1500) level = 1;
            else if (count > 1000) level = 2;
            else if (count > 500) level = 3;
            else if (count > 300) level = 4;
            else if (count > 150) level = 5;
            else if (count > 70) level = 6;
            else if (count > 30) level = 7;
            else level = 8;

            System.out.print("Level " + level);

            if (prompts[level].size() < 10) {
                prompts[level].add(prompt);
                System.out.println(" added");
            }
            else {
                System.out.println(" rejected");
                complete = true;
                for (ArrayList<String> list : prompts)
                    if (list.size() < 10) {
                        complete = false;
                        break;
                    }
            }
        }

        promptList = new String[80];
        for (int i = 0; i < 80; i++) promptList[i] = prompts[i/10].get(i%10);
        System.out.println(Arrays.toString(promptList));
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
