import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Index {
    private ConcurrentHashMap<String, IndexEntry> index = new ConcurrentHashMap<>();

    public synchronized void addFile(String filePath, String content) {
        List<String> words = tokenize(content.toLowerCase());
        IndexEntry entry = new IndexEntry(filePath, words);
        index.put(filePath, entry);
    }

    public synchronized void removeFile(String filePath) {
        index.remove(filePath);
    }

    public synchronized boolean isFileIndexed(String filePath) {
        return index.containsKey(filePath);
    }

    public List<String> search(String word) {
        word = word.toLowerCase();
        List<String> matchingFiles = new ArrayList<>();
        
        for (IndexEntry entry : index.values()) {
            if (entry.containsWord(word)) {
                matchingFiles.add(new File(entry.getFilePath()).getAbsolutePath());
            }
        }
        return matchingFiles;
    }

    public List<String> searchPhrase(String phrase) {
        phrase = phrase.toLowerCase();
        List<String> matchingFiles = new ArrayList<>();
        List<String> phraseWords = tokenize(phrase); 

        for (IndexEntry entry : index.values()) {
            if (entry.containsPhrase(phraseWords)) {
                matchingFiles.add(new File(entry.getFilePath()).getAbsolutePath());
            }
        }
        return matchingFiles;
    }

    public synchronized void display() {
        if (index.isEmpty()) {
            System.out.println("No files indexed yet.");
        }
        for (IndexEntry entry : index.values()) {
            System.out.println(new File(entry.getFilePath()).getAbsolutePath());
        }
    }

    public synchronized List<String> searchByName(String fileName) {
        List<String> matchingFiles = new ArrayList<>();
        
        for (IndexEntry entry : index.values()) {
            if (entry.getFilePath().endsWith(fileName)) {
                matchingFiles.add(new File(entry.getFilePath()).getAbsolutePath());
            }
        }
        return matchingFiles;
    }

    private List<String> tokenize(String content) {
        List<String> words = new ArrayList<>();
        String[] tokens = content.split("[^\\w'']+");
    
        for (String token : tokens) {
            if (!token.isEmpty()) {
                words.add(token);
            }
        }
        return words;
    }
    
    private static class IndexEntry {
        private String filePath;
        private List<String> words = new CopyOnWriteArrayList<>();

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
