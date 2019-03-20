import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileWalker extends SimpleFileVisitor<Path> {

    private boolean isPackagePresent;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        isPackagePresent = false;
        if (!isJava(file)) {
            return FileVisitResult.CONTINUE;
        }
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        System.out.println(String.format("Analyzing file: %s", file));
        new VoidVisitorAdapter() {
            @Override
            public void visit(PackageDeclaration n, Object arg) {
                isPackagePresent = true;
            }
        }.visit(compilationUnit, null);
        boolean warningsDetected = false;
        if (!isPackagePresent) {
            warningsDetected = true;
            System.out.println("[WARNING] No package declaration.");
        }
        if (!warningsDetected) {
            System.out.println("No warnings have been detected.");
        }
        return FileVisitResult.CONTINUE;
    }

    private boolean isJava(Path file) {
        return file.toString().endsWith("java");
    }
}
