package br.org.cristofolini;

//import org.antlr.v4.runtime.ParserRuleContext;
//import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.symtab.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class PortugueshParseTreeListener extends PortugueshBaseListener {
    Logger logger = LogManager.getLogger(getClass());
    PrimitiveType stringType = new PrimitiveType("texto");
    PrimitiveType numberType = new PrimitiveType("número");
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
            if (currentScope.getSymbol(ctx.ID().getText()) != null) {
                logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.ID().getText() + " is already defined in this scope");
                return;
            }
            VariableSymbol v = new VariableSymbol(ctx.ID().getText());
            if (ctx.value().getText() != null) {
                if (ctx.value().getText().startsWith("\"")) v.setType(stringType);
                else v.setType(numberType);
            }
            currentScope.define(v);
            logger.log(Level.forName("SYMBOL", 395), v.getName() + " of TYPE " + v.getType());
        }
    }

    @Override
    public void exitDeclaration(@NotNull PortugueshParser.DeclarationContext ctx) {
        super.exitDeclaration(ctx);
    }

    @Override
    public void enterCondition(PortugueshParser.ConditionContext ctx) {
        if (ctx.operand(0) != null) {
            if (ctx.operand(0).ID() != null) {
                if (currentScope.getSymbol(ctx.operand(0).ID().getText()) == null) {
                    logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.operand(0).getText() + " is not defined in this scope");
                    return;
                }
            }
            if (ctx.operand(0).value() != null) {
                //TODO: compare types
            }
        }
        if (ctx.operand(1) != null) {
            if (ctx.operand(1).ID() != null) {
                if (currentScope.getSymbol(ctx.operand(1).ID().getText()) == null) {
                    logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.operand(1).getText() + " is not defined in this scope");
                    return;
                }
            }
            if (ctx.operand(1).value() != null) {
                //TODO: compare types
            }
        }
    }

    @Override
    public void enterFunction(PortugueshParser.FunctionContext ctx) {
        FunctionSymbol f = new FunctionSymbol(ctx.ID().getText());
        if (ctx.TYPE() != null) {
            if (ctx.TYPE().getText().equals(stringType.getName())) f.setType(stringType);
            if (ctx.TYPE().getText().equals(numberType.getName())) f.setType(numberType);
            logger.log(Level.forName("SYMBOL", 395), f.getName() + " of TYPE " + f.getType());
        }

        List scopeSymbols = currentScope.getAllSymbols();
        Type returnType = new PrimitiveType("");
        if (ctx.block() != null){
            if (ctx.block().ID() != null) {
                for (Iterator<VariableSymbol> symbols = scopeSymbols.iterator(); symbols.hasNext();) {
                    VariableSymbol s = symbols.next();
                    if (s.getType().equals(stringType)) returnType = stringType;
                    if (s.getType().equals(numberType)) returnType = numberType;
                }
//                String actualReturnType = a.getType().getName();
//                if (actualReturnType != null) {
//                    if (actualReturnType.equals(stringType.getName())) returnType = stringType;
//                    if (actualReturnType.equals(numberType.getName())) returnType = numberType;
//                }
            }
        }
        if (returnType != f.getType()) logger.log(Level.forName("SEMANTIC ERROR", 394), "Return variable is of the wrong type");

        f.setEnclosingScope(currentScope);
        currentScope.define(f);
        ctx.scope = f;
        pushScope(f);
    }

    @Override
    public void exitFunction(PortugueshParser.FunctionContext ctx) {
        popScope();
    }

    @Override
    public void enterBlock(PortugueshParser.BlockContext ctx) {
        LocalScope l = new LocalScope(currentScope);
        ctx.scope = l;
        pushScope(l);
    }

    @Override
    public void exitBlock(PortugueshParser.BlockContext ctx) {
        popScope();
    }

    @Override
    public void enterAttribution(PortugueshParser.AttributionContext ctx) {
        if (ctx.ID() != null) {
            if (currentScope.getSymbol(ctx.ID().getText()) == null) logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.ID().getText() + " is not defined in this scope");
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
