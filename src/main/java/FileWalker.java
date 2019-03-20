import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import visitors.UnusedVariableVisitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class FileWalker extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if (!isJava(file)) {
            return FileVisitResult.CONTINUE;
        }
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        UnusedVariableVisitor unusedVariableVisitor = new UnusedVariableVisitor();
        unusedVariableVisitor.visit(compilationUnit, null);
        return FileVisitResult.CONTINUE;
    }

    private boolean isJava(Path file) {
        return file.toString().endsWith("java");
    }
}
