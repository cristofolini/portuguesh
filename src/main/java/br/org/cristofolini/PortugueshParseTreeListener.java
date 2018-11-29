package br.org.cristofolini;

import org.antlr.symtab.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class PortugueshParseTreeListener extends PortugueshBaseListener {
    Logger logger = LogManager.getLogger(getClass());
    PrimitiveType stringType = new PrimitiveType("texto");
    PrimitiveType numberType = new PrimitiveType("número");
    Scope currentScope;
    private Map<String, String> variableValues = new HashMap<>();

    private static final String fileName = "out/program.j";
    private FileWriter fileWriter;
    private PrintWriter printWriter;

    private static final String JASMIN_INTEGER_FIELD = " I ";
    private static final String JASMIN_STRING_FIELD = " Ljava/lang/String; ";

    private int condCount = 1;
    private int loopCount = 1;
    private int localVariableCount = 1;

    static final PortugueshParseTreeListener INSTANCIA = new PortugueshParseTreeListener();

    @Override
    public void visitTerminal(TerminalNode node) {
        logger.log(Level.forName("TOKEN", 375), node.getText());
    }

    @Override
    public void enterFile(@NotNull PortugueshParser.FileContext ctx) {
        GlobalScope globalScope = new GlobalScope(null);
        ctx.scope = globalScope;

        try {
            fileWriter = new FileWriter(fileName);
            printWriter = new PrintWriter(fileWriter);
            printWriter.println(".class public program/Main");
            printWriter.println(".super java/lang/Object\n");
            printWriter.println(".method public static main([Ljava/lang/String;)V");
        } catch (IOException e) {
            e.printStackTrace();
        }

        pushScope(globalScope);
    }

    @Override
    public void exitFile(@NotNull PortugueshParser.FileContext ctx) {
        printWriter.println("return");
        printWriter.println(".limit stack 10");
        printWriter.println(".limit locals 10");
        printWriter.println(".end method");
        printWriter.close();
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
                if (ctx.value().getText().startsWith("\"")) {
                    v.setType(stringType);
                    printWriter.println("ldc " + ctx.value().getText());
                }
                else {
                    v.setType(numberType);
                    printWriter.println("ldc " + ctx.value().getText());
                    printWriter.println("istore " + localVariableCount);
                    localVariableCount++;
                    variableValues.put(v.getName(), ctx.value().getText());
                }
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
    public void enterConditional(PortugueshParser.ConditionalContext ctx) {
        printWriter.println("ldc " + variableValues.get(ctx.condition().operand(0).getText()));
        printWriter.println("ldc " + variableValues.get(ctx.condition().operand(1).getText()));
        String comparator = ctx.condition().comparator().getText();
        String jasminComparator = getJasminComparator(comparator);
        printWriter.println(jasminComparator + "CondOut" + condCount);
    }

    @Override
    public void exitConditional(PortugueshParser.ConditionalContext ctx) {
        printWriter.println("CondOut" + condCount + ":");
        condCount++;
    }

    @Override
    public void enterCondition(PortugueshParser.ConditionContext ctx) {
        VariableSymbol operand0 = new VariableSymbol("");
        VariableSymbol operand1  = new VariableSymbol("");

        if (ctx.operand(0) != null) {
            if (ctx.operand(0).ID() != null) {
                if (currentScope.getSymbol(ctx.operand(0).ID().getText()) == null) {
                    logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.operand(0).getText() + " is not defined in this scope");
                    return;
                } else {
                    operand0 = (VariableSymbol) currentScope.getSymbol(ctx.operand(0).ID().getText());
                }
            }
        }
        if (ctx.operand(1) != null) {
            if (ctx.operand(1).ID() != null) {
                if (currentScope.getSymbol(ctx.operand(1).ID().getText()) == null) {
                    logger.log(Level.forName("SEMANTIC ERROR", 394), ctx.operand(1).getText() + " is not defined in this scope");
                    return;
                } else {
                    operand1 = (VariableSymbol) currentScope.getSymbol(ctx.operand(1).ID().getText());
                }
            }
        }
        if (operand0.getType() != operand1.getType()) logger.log(Level.forName("SEMANTIC ERROR", 394), "Operands (" + operand0.getName() + ", " + operand1.getName() + ") are of different types");
    }

    @Override
    public void exitCondition(PortugueshParser.ConditionContext ctx) {
//        printWriter.println("CondOut" + condCount + ":");
//        condCount++;
    }

    @Override
    public void enterFunction(PortugueshParser.FunctionContext ctx) {
        FunctionSymbol f = new FunctionSymbol(ctx.ID().getText());
        if (ctx.TYPE() != null) {
            if (ctx.TYPE().getText().equals(stringType.getName())) f.setType(stringType);
            if (ctx.TYPE().getText().equals(numberType.getName())) f.setType(numberType);
            logger.log(Level.forName("SYMBOL", 395), f.getName() + " - FUNCTION of TYPE " + f.getType());
        }

        f.setEnclosingScope(currentScope);
        currentScope.define(f);
        ctx.scope = f;
        pushScope(f);
    }

    @Override
    public void exitFunction(PortugueshParser.FunctionContext ctx) {
        FunctionSymbol functionSymbol = (FunctionSymbol) currentScope.getEnclosingScope().getSymbol(currentScope.getName());
        if (ctx.block() != null){
            if (ctx.block().ID() != null) {
                String returnName = ctx.block().ID().getText();
                VariableSymbol returnSymbol = (VariableSymbol) ctx.block().scope.getSymbol(returnName);
                if (returnSymbol == null) returnSymbol = (VariableSymbol) currentScope.getEnclosingScope().getSymbol(returnName);
                if (returnSymbol == null) logger.log(Level.forName("SEMANTIC ERROR", 394), "Return variable (" + returnSymbol.getName() + ") is not present in the current scope");
                else {
                    if (returnSymbol.getName().equals(returnName)) {
                        if (functionSymbol.getType() != returnSymbol.getType()) logger.log(Level.forName("SEMANTIC ERROR", 394), "Return variable (" + returnSymbol.getName() + ") is of the wrong type");
                    }
                }
            }
        }

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
            variableValues.put(ctx.ID().getText(), ctx.value().getText());
        }
    }

    @Override
    public void enterLoop(PortugueshParser.LoopContext ctx) {
        printWriter.println("Loop" + loopCount + ":");
    }

    @Override
    public void exitLoop(PortugueshParser.LoopContext ctx) {
        printWriter.println("ldc " + variableValues.get(ctx.condition().operand(0).getText()));
        printWriter.println("ldc " + variableValues.get(ctx.condition().operand(1).getText()));

        String comparator = ctx.condition().comparator().getText();
        String jasminComparator = getJasminComparator(comparator);
        printWriter.println(jasminComparator + "Loop" + loopCount);
        loopCount++;
    }


    private void pushScope(Scope scope) {
        currentScope = scope;
        System.out.println("entering: " + currentScope.getName() + ":" + scope);
    }

    private void popScope() {
        currentScope = currentScope.getEnclosingScope();
        if (currentScope != null) System.out.println("leaving: " + currentScope.getName() + ":" + currentScope);
    }

    private String getJasminComparator(String comparator) {
        switch (comparator) {
            case "!=":
                return "if_icmpeq ";
            case "==":
                return  "if_icmpne ";
            case "<":
                return  "if_icmpge ";
            case ">":
                return  "if_icmple ";
            case "<=":
                return  "if_icmpgt ";
            case ">=":
                return  "if_icmplt ";

            default:
                return  "if_acmpeq";
        }
    }
}
