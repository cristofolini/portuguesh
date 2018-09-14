package br.org.cristofolini;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortugueshParseTreeListener extends PortugueshBaseListener {
    Logger logger = LogManager.getLogger(getClass());

    @Override
    public void visitTerminal(TerminalNode node) {
        logger.log(Level.forName("TOKEN", 375), node.getText());
    }

//    @Override public void visitErrorNode(ErrorNode node) { logger.log(Level.forName("TOKEN ERROR", 375), node.toString()); }
}
