import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Scanner;

public class Console {
    private Index index;

    public Console() {
        index = new Index();
    }
    
    public void run(){
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            System.out.println("Choose an option: ");
            System.out.println("1. Index a file");
            System.out.println("2. Search for a word or phrase");
            System.out.println("3. Display all files");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice){
                case 1:
                    addFile(scanner);
                    System.out.println("");
                    break;  
                case 2: 
                    searchFiles(scanner);
                    System.out.println("");
                    break;
                case 3:
                    index.display();
                    System.out.println("");
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addFile(Scanner scanner) {
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();      
        StringBuilder fileContentBuilder = new StringBuilder();
    
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContentBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
            return;
        }
    
        String fileContent = fileContentBuilder.toString();
        index.addFile(filePath, fileContent);
    
        String fileName = new File(filePath).getName();    
        System.out.println(fileName + " indexed successfully.");
    }
    

    private void searchFiles(Scanner scanner) {
        System.out.print("Enter the word or phrase to search: ");
        String input = scanner.nextLine();
        
        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
            return;
        }
        List<String> matchingFiles;

        if (input.contains(" ")) {
            matchingFiles = index.searchPhrase(input);
        } else {
            matchingFiles = index.search(input);
        }
    
        if (matchingFiles.isEmpty()) {
            System.out.println("No matching files found.");
        } else {
            System.out.println("Matching files:" + '\n');
            for (String filePath : matchingFiles) {
                File file = new File(filePath);
                System.out.println("File: " + file.getName() + '\n'); 
                System.out.println("Preview: ");
                previewFileContent(file, input); 
            }
        }
    }
    

    private void previewFileContent(File file, String searchQuery) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line; 
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(searchQuery.toLowerCase())) {
                    System.out.println(">>> " + line.replaceAll("(?i)" + searchQuery, "[" + searchQuery + "]"));
                    break;
                }
            }    
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    }

    
    

    public static void main(String[] args) throws Exception {
        Console console = new Console();
        console.run();
    }  
}
