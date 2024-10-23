import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Console {
    private Index index;
    private ExecutorService executor;

    public Console() {
        executor = Executors.newFixedThreadPool(5);
        index = new Index();
    }
    
    public void run(){
        Scanner scanner = new Scanner(System.in);
        
        while(true){
            System.out.println("\n Choose an option: ");
            System.out.println("1. Index a file");
            System.out.println("2. Remove a file");
            System.out.println("3. Search for a word or phrase");
            System.out.println("4. Search for an indexed file");
            System.out.println("5. Display all files");
            System.out.println("6. Exit \n");

            System.out.print("Enter your choice: ");
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                scanner.nextLine(); 
                continue;
            }

            switch(choice){
                case 1:
                    addFile(scanner);
                    break;  
                case 2:
                    removeFile(scanner);
                    break;
                case 3: 
                    searchFiles(scanner);
                    break;
                case 4:
                    searchFilesByName(scanner);
                    break;
                case 5:
                    index.display();
                    break;
                case 6:
                    System.out.println("Exiting...");
                    executor.shutdown();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addFile(Scanner scanner) {
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine().replaceAll("^\"|\"$", "").trim();             
        
        if(filePath == null || filePath.isEmpty()) {
            System.out.println("File path cannot be empty.");
            return;
        }

        if (!filePath.endsWith(".txt")) {
            System.out.println("Invalid file type. Only .txt files are supported.");
            return;
        }

        Future<?> future = executor.submit(() -> {
            long startTime = System.currentTimeMillis();

            StringBuilder fileContentBuilder = new StringBuilder();
                
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContentBuilder.append(line).append(System.lineSeparator());
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
                return;
            }
        
            String fileContent = fileContentBuilder.toString();

            try {
                index.addFile(filePath, fileContent);
            } catch (Exception e) {
                System.out.println("An error occurred while indexing the file: " + e.getMessage());
                return;
            }
    
            long endTime = System.currentTimeMillis();
            String fileName = new File(filePath).getName();    
            System.out.println(fileName + " indexed successfully. Time taken: " + (endTime - startTime) + "ms");

        });

        try {
            future.get();
        } catch (Exception e) {
            System.out.println("An error occurred while indexing the file: " + e.getMessage());
        }
    }

    private void removeFile(Scanner scanner) {
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine().replaceAll("^\"|\"$", "").trim();             

        if(filePath == null || filePath.isEmpty()) {
            System.out.println("File path cannot be empty.");
            return;
        }

        if (!filePath.endsWith(".txt")) {
            System.out.println("Invalid file type. Only .txt files are supported.");
            return;
        }

        Future<?> future = executor.submit(() -> {
            long startTime = System.currentTimeMillis();

            try {
                index.removeFile(filePath);
            } catch (Exception e) {
                System.out.println("An error occurred while removing the file: " + e.getMessage());
                return;
            }

            long endTime = System.currentTimeMillis();
            String fileName = new File(filePath).getName();    
            System.out.println(fileName + " removed successfully. Time taken: " + (endTime - startTime) + "ms");

        });

        try {
            future.get();
        } catch (Exception e) {
            System.out.println("An error occurred while removing the file: " + e.getMessage());
        }
    }
    
    private void searchFiles(Scanner scanner) {
        System.out.print("Enter the word or phrase to search: ");
        String input = scanner.nextLine();
        
        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
            return;
        }

        Future<?> future = executor.submit(() -> {
            long startTime = System.currentTimeMillis();

            List<String> matchingFiles;

            if (input.contains(" ")) {
                matchingFiles = index.searchPhrase(input);
            } else {
                matchingFiles = index.search(input);
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Time taken to search: " + (endTime - startTime) + "ms");
        
            if (matchingFiles.isEmpty()) {
                System.out.println("No matching files found.");
            } else {
                System.out.println("Matching files:" + "\n");
                for (String filePath : matchingFiles) {
                    File file = new File(filePath);
                    System.out.println("File: " + file.getName()); 
                    System.out.println("Preview: ");
                    previewFileContent(file, input); 
                }
            }
        });     

        try {
            future.get();
        } catch (Exception e) {
            System.out.println("An error occurred while searching the files: " + e.getMessage());
        }
    }
    

    private void previewFileContent(File file, String searchQuery) {
        Future<?> future = executor.submit(() -> {
            long startTime = System.currentTimeMillis();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                String line; 
                long endTime = System.currentTimeMillis();
                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().contains(searchQuery.toLowerCase())) {
                        System.out.println(">>> " + line.replaceAll("(?i)" + searchQuery, "[" + searchQuery + "]"));
                        break;
                    }
                }
                System.out.println("Time taken to preview: " + (endTime - startTime) + "ms" + "\n");
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }           
        });

        try {
            future.get();
        } catch (Exception e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
    } 

    private void searchFilesByName(Scanner scanner) {
        System.out.print("Enter the file name to search: ");
        String input = scanner.nextLine();

        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
            return;
        }

        input = input.trim();
        String baseName = input.endsWith(".txt") ? input : input + ".txt";

        Future<?> future = executor.submit(() -> {
            List<String> matchingFiles = new ArrayList<>();
    
            matchingFiles.addAll(index.searchByName(baseName));
    
            if (matchingFiles.isEmpty()) {
                System.out.println("No matching files found.");
            } else {
                System.out.println("Matching files:" + '\n');
                for (String filePath : matchingFiles) {
                    File file = new File(filePath);
                    System.out.println(file.getName() + ": " + file.getAbsolutePath());
                }
            }
        });
    
        try {
            future.get();
        } catch (Exception e) {
            System.out.println("An error occurred while searching the files: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) throws Exception {
        Console console = new Console();
        console.run();
    }  
}
