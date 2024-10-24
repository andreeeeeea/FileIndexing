# Java File Indexing Application #

This Java application allows users to index .txt files, search for words and phrases across these files, and interact with the indexed data using a console

## Features
- Index Text Files: Index any number of .txt files, making their contents searchable.
- Remove Text Files from Index: Remove a given .txt file.
- Search by Word or Phrase: Search for specific words or phrases in indexed files.
- Search by File Name: Retrieve indexed files by name.
- Multi-threading Support: Indexing, file removal, and search are all handled concurrently for efficiency.

## Requirements
- Java JDK 23 or later

## Installation
1. **Download or Clone the Repository**:

   git clone https://github.com/andreeeeeea/FileIndexing.git

2. **Navigate to the Project Directory**:

   cd FileIndexing\src

3. Compile the Java Files:

   javac Console.java
   
5. Run the Application:

   java Console

## Usage
After starting the application, you will see a menu with options:

```
1. Index a file
2. Remove a file
3. Search for a word or phrase
4. Search for an indexed file
5. Display all files
6. Exit
```

## Example Commands

- To index the File1.txt file in the Test 1 folder, choose option 1 and type the file path:
  
  `..\Test-1\File1.txt` to index File1.txt from the provided Test-1 folder
  
- To search for a word/phrase, choose option 3 and type the wanted word/phrase:
  
  `village square`
  
- To search for a certain file, choose option 4 and type the wanted file's name:
  
  `File1`
  
- To remove the File1.txt file from the Test 1 folder, choose option 2 and type the file path:
  
  `..\Test-1\File1.txt`
  

## Troubleshooting
- UnsupportedClassVersionError: Ensure you are running the application with the same Java version that was used to compile it. Update your Java installation if necessary.
- File Not Found: Make sure the file paths entered are correct and the files exist.



