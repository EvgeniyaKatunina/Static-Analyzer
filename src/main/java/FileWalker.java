import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileWalker extends SimpleFileVisitor<Path> {

    private boolean isPackagePresent;
    private boolean areWarningsDetected;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        isPackagePresent = false;
        areWarningsDetected = false;
        if (!isJava(file)) {
            return FileVisitResult.CONTINUE;
        }
        CompilationUnit compilationUnit = StaticJavaParser.parse(file);
        System.out.println(String.format("Analyzing file: %s", file));
        new VoidVisitorAdapter() {
            @Override
            public void visit(PackageDeclaration n, Object arg) {
                isPackagePresent = true;
                super.visit(n, arg);
            }

            @Override
            public void visit(DoStmt n, Object arg) {
                isEmptyBlock(n.getBody());
                super.visit(n, arg);
            }

            @Override
            public void visit(WhileStmt n, Object arg) {
                isEmptyBlock(n.getBody());
                super.visit(n, arg);
            }

            @Override
            public void visit(ForEachStmt n, Object arg) {
                isEmptyBlock(n.getBody());
                super.visit(n, arg);
            }

            @Override
            public void visit(ForStmt n, Object arg) {
                isEmptyBlock(n.getBody());
                super.visit(n, arg);
            }

            @Override
            public void visit(SynchronizedStmt n, Object arg) {
                isEmptyBlock(n.getBody());
                super.visit(n, arg);
            }

            @Override
            public void visit(IfStmt n, Object arg) {
                isEmptyBlock(n.getThenStmt(), n.getElseStmt().orElse(null));
                n.getElseStmt().filter(elseStmt -> n.getThenStmt().equals(elseStmt)).ifPresent(elseStmt -> {
                    areWarningsDetected = true;
                    System.out.println(String.format("[WARNING] 'Else' branch at %s is equal to 'then' branch.",
                            elseStmt.getRange().get()));
                });
                super.visit(n, arg);
            }

            @Override
            public void visit(TryStmt n, Object arg) {
                List<Node> catchClauses =
                        new ArrayList<>(n.getCatchClauses().stream().flatMap(x -> x.getChildNodes().stream()).collect(Collectors.toList()));
                catchClauses.add(n.getTryBlock());
                isEmptyBlock(catchClauses.toArray(new Node[catchClauses.size()]));
                super.visit(n, arg);
            }

            @Override
            public void visit(FieldDeclaration n, Object arg) {
                if (n.isStatic() && n.isFinal()) {
                    n.getVariables().forEach(v -> {
                        String varName = v.getNameAsString();
                        if (!varName.equals(varName.toUpperCase())) {
                            areWarningsDetected = true;
                            System.out.println(String.format("[WARNING] Static final variable '%s' must be in " +
                                    "uppercase" + ".", varName));
                        }
                    });
                }
                super.visit(n, arg);
            }

            @Override
            public void visit(VariableDeclarator n, Object arg) {
                String varName = n.getNameAsString();
                if (varName.contains("_")) {
                    areWarningsDetected = true;
                    System.out.println(String.format("[WARNING] Variable name '%s' should be in lower camel case, " +
                            "detected at position: %s", varName, n.getRange().get()));
                }
                super.visit(n, arg);
            }
        }.visit(compilationUnit, null);
        if (!isPackagePresent) {
            areWarningsDetected = true;
            System.out.println("[WARNING] No package declaration.");
        }
        if (!areWarningsDetected) {
            System.out.println("No warnings have been detected.");
        }
        return FileVisitResult.CONTINUE;
    }

    private boolean isJava(Path file) {
        return file.toString().endsWith("java");
    }

    private void isEmptyBlock(Node... args) {
        for (Node arg : args) {
            if (arg != null && arg.getChildNodes().stream().allMatch(x -> x instanceof EmptyStmt)) {
                areWarningsDetected = true;
                System.out.println(String.format("[WARNING] Empty block at position: %s", arg.getRange().get()));
            }
        }
    }
}
