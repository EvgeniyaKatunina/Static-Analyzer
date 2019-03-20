package visitors;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class UnusedVariableVisitor extends VoidVisitorAdapter {

    @Override
    public void visit(VariableDeclarator n, Object arg) {
        System.out.println(n.getName());
        System.out.println(n.getType());
        super.visit(n, arg);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        System.out.println(n.getName());
        super.visit(n, arg);
    }

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        System.out.println(n.getName());
        super.visit(n, arg);
    }
}
