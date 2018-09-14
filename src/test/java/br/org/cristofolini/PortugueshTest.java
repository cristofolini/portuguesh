package br.org.cristofolini;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class PortugueshTest
{
    Logger logger = LogManager.getLogger(getClass());

    @Test
    public void testExemplo() throws Exception {
        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exemplo.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));

        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        p.addParseListener(new PortugueshParseTreeListener());
        p.setBuildParseTree(true);
        RuleContext tree = p.prog();
        System.out.println(tree.toStringTree(p));
        logger.log(Level.forName("PARSE TREE", 375), tree.toStringTree(p));
        if (PortugueshErrorListener.INSTANCIA.dirtyBit) throw new ParseCancellationException();
    }

    @Test
    public void testErrado() throws Exception {

        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exNonsense.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));
        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        //p.setErrorHandler(new PortugueshErrorStrategy());
        p.addParseListener(new PortugueshParseTreeListener());
        p.prog();
        if (PortugueshErrorListener.INSTANCIA.dirtyBit) throw new ParseCancellationException();
    }
}
