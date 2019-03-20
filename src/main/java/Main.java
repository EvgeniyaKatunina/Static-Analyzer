import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Specify full path to file or directory you want to analyze.");
                Path path = Paths.get(br.readLine());
                if (!path.isAbsolute()) {
                    System.out.println("Path must be absolute.");
                    continue;
                }
                if (!Files.exists(path)) {
                    System.out.println("File does not exist.");
                    continue;
                }
                run(path);
            }
        }
    }

    private static void run(Path path) throws IOException {
        Files.walkFileTree(path, new FileWalker());
    }
}
