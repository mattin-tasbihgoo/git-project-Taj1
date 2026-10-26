import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//elle made tester

public class Verify {
    public static void main(String[] args) {

        gitRepository git = new gitRepository();

        try {
            // Test 1: Initialize repository
            git.initializeGit();

            // Test 2: Verify required files/directories exist
            Path directory = Path.of("git");
            Path objects = directory.resolve("objects");
            Path index = directory.resolve("index");
            Path head = directory.resolve("HEAD");

            if (Files.isDirectory(directory)
                    && Files.isDirectory(objects)
                    && Files.isRegularFile(index)
                    && Files.isRegularFile(head)) {

                System.out.println("Test Passed: Git repository initialized");

            } else {
                System.out.println("Test failed: repository is missing files/directories");
            }

        } catch (IOException e) {
            System.out.println("Test Failed: " + e.getMessage());
        }

        try {
            String hash = FileHasher.hashFile("JavaFileSystem/backup/backup.txt");
            System.out.println("SHA-1: " + hash);
        } catch (IOException e) {
            System.out.println("Hash error: " + e.getMessage());
        }
        // test truee

        // tests 3.2, 3.3

        List<String> workingList = new ArrayList<>();
        workingList.add("blob abc123 myProgram/docs/Hello.txt");
        workingList.add("blob def456 myProgram/docs/World.txt");
        workingList.add("blob ghi789 myProgram/README.md");

        try {
            // 3.2
            String treehash = git.createTree(workingList, "myProgram/docs");
            System.out.println("Tree Hash: " + treehash);
            System.out.println(Files.readString(Paths.get("git/objects/" + treehash)));

            // 3.3
            Files.createDirectories(Path.of("myProgram/docs"));
            Files.writeString(Path.of("myProgram/docs/Hello.txt"), "hello");
            Files.writeString(Path.of("myProgram/docs/World.txt"), "world");
            Files.writeString(Path.of("myProgram/README.md"), "readme");

            for (String e : List.of("myProgram/docs/Hello.txt",
                    "myProgram/docs/World.txt",
                    "myProgram/README.md")) {
                git.createBlob(e);
                git.writeInIndex(e);
            }

            String rootHash = git.createTreefromIndex();
            System.out.println("Root Tree Hash: " + rootHash);
            Path tree = Paths.get("git/objects", rootHash);

            if (!rootHash.isEmpty() && Files.isRegularFile(tree)) {
                System.out.println("yayyyy");
            } else {
                System.out.println("failed");
            }

        } catch (IOException e) {
            System.out.println("Test Failed: " + e.getMessage());
        }

    }
}