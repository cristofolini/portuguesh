package br.org.cristofolini;

//import org.antlr.v4.runtime.ParserRuleContext;
//import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.symtab.FunctionSymbol;
import org.antlr.symtab.GlobalScope;
import org.antlr.symtab.LocalScope;
import org.antlr.symtab.Scope;
import org.antlr.symtab.VariableSymbol;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortugueshParseTreeListener extends PortugueshBaseListener {
    Logger logger = LogManager.getLogger(getClass());
    Scope currentScope;

    @Override
    public void visitTerminal(TerminalNode node) {
        logger.log(Level.forName("TOKEN", 375), node.getText());
    }

    @Override
    public void enterProg(@NotNull PortugueshParser.ProgContext ctx) {
        GlobalScope globalScope = new GlobalScope(null);
        ctx.scope = globalScope;
        pushScope(globalScope);
    }

    @Override
    public void exitProg(@NotNull PortugueshParser.ProgContext ctx) {
        popScope();
    }

    @Override
    public void enterDeclaration(@NotNull PortugueshParser.DeclarationContext ctx) {
        if (ctx.name.getText() != null) {
            System.out.println(ctx.name.getText());
            VariableSymbol v = new VariableSymbol(ctx.name.getText());
        }

        //v.setType();
    }

    @Override
    public void exitDeclaration(@NotNull PortugueshParser.DeclarationContext ctx) {
        super.exitDeclaration(ctx);
    }

    private void pushScope(Scope scope) {
        currentScope = scope;
    }

    private void popScope() {
        currentScope = currentScope.getEnclosingScope();
    }

//    @Override public void visitErrorNode(ErrorNode node) { logger.log(Level.forName("TOKEN ERROR", 375), node.toString()); }
}
