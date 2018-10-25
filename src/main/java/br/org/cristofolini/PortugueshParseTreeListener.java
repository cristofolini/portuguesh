package br.org.cristofolini;

//import org.antlr.v4.runtime.ParserRuleContext;
//import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.symtab.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortugueshParseTreeListener extends PortugueshBaseListener {
    Logger logger = LogManager.getLogger(getClass());
    Scope currentScope;

    static final PortugueshParseTreeListener INSTANCIA = new PortugueshParseTreeListener();

    @Override
    public void visitTerminal(TerminalNode node) {
        logger.log(Level.forName("TOKEN", 375), node.getText());
    }

    @Override
    public void enterFile(@NotNull PortugueshParser.FileContext ctx) {
        GlobalScope globalScope = new GlobalScope(null);
        ctx.scope = globalScope;
        pushScope(globalScope);
    }

    @Override
    public void exitFile(@NotNull PortugueshParser.FileContext ctx) {
        popScope();
    }

    @Override
    public void enterDeclaration(@NotNull PortugueshParser.DeclarationContext ctx) {
        if (ctx.ID() != null) {
            logger.log(Level.forName("SYMBOL", 395), ctx.ID().getText());
            VariableSymbol v = new VariableSymbol(ctx.ID().getText());
//          v.setType(ctx.);
            currentScope.define(v);
        }

        //v.setType();
    }

    @Override
    public void exitDeclaration(@NotNull PortugueshParser.DeclarationContext ctx) {
        super.exitDeclaration(ctx);
    }

    @Override
    public void enterAttribution(PortugueshParser.AttributionContext ctx) {
        if (ctx.ID() != null) {
            if (currentScope.getSymbol(ctx.ID().getText()) == null) logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.ID().getText() + " is not defined");
        }
    }

    private void pushScope(Scope scope) {
        currentScope = scope;
        System.out.println("entering: " + currentScope.getName() + ":" + scope);
    }

    private void popScope() {
        currentScope = currentScope.getEnclosingScope();
        if (currentScope != null) System.out.println("leaving: " + currentScope.getName() + ":" + currentScope);
    }

//    @Override public void visitErrorNode(ErrorNode node) { logger.log(Level.forName("TOKEN ERROR", 375), node.toString()); }
}
