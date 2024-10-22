import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class Index {
    private Map<String, IndexEntry> index = new HashMap<>();

    public void addFile(String filePath, String content) {
        if (filePath == null || content == null) {
            throw new IllegalArgumentException("File path and content cannot be null");
        }

        List<String> words = tokenize(content.toLowerCase());
        IndexEntry entry = new IndexEntry(filePath, words);
        index.put(filePath, entry);
    }

    public List<String> search(String word) {
        word = word.toLowerCase();
        if (word == null) {
            throw new NoSuchElementException("Word cannot be null or empty");
        }
        List<String> matchingFiles = new ArrayList<>();
        
        for (IndexEntry entry : index.values()) {
            if (entry.containsWord(word)) {
                matchingFiles.add(entry.getFilePath());
            }
        }
        return matchingFiles;
    }

    public List<String> searchPhrase(String phrase) {
        phrase = phrase.toLowerCase();
        if (phrase == null || phrase.isEmpty()) {
            throw new NoSuchElementException("Phrase cannot be null or empty");
        }
        List<String> matchingFiles = new ArrayList<>();
        List<String> phraseWords = tokenize(phrase); 

        for (IndexEntry entry : index.values()) {
            if (entry.containsPhrase(phraseWords)) {
                matchingFiles.add(entry.getFilePath());
            }
        }
        return matchingFiles;
    }

    public void display() {
        for (IndexEntry entry : index.values()) {
            System.out.println(entry.getFilePath());
        }
    }

    private List<String> tokenize(String content) {
        List<String> words = new ArrayList<>();
        String[] tokens = content.split("\\s+");

        for (String token : tokens) {
            words.add(token);
        }
        return words;
    }

    private static class IndexEntry {
        private String filePath;
        private List<String> words;

        public IndexEntry(String filePath, List<String> words) {
            this.filePath = filePath;
            this.words = words;
        }

        public String getFilePath() {
            return filePath;
        }

        public boolean containsWord(String word) {
            return words.contains(word);
        }

        public boolean containsPhrase(List<String> phraseWords) {
            int phraseLength = phraseWords.size();

            for (int i = 0; i <= words.size() - phraseLength; i++) {
                boolean match = true;
                for (int j = 0; j < phraseLength; j++) {
                    if (!words.get(i + j).equals(phraseWords.get(j))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    return true;
                }
            }
            return false; 
        }
    }
}
