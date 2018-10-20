package br.org.cristofolini;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import javax.swing.*;
import java.util.Arrays;

public class PortugueshTest
{
    Logger logger = LogManager.getLogger(getClass());

    @Test
    public void testExemplo() throws Exception {
        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exemplo2.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));

        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        p.addParseListener(new PortugueshParseTreeListener());
        p.setBuildParseTree(true);
        ParseTree tree = p.prog();
        //System.out.println(tree.toStringTree(p));
        //logger.log(Level.forName("PARSE TREE", 375), tree.toStringTree(p));
        if (PortugueshErrorListener.INSTANCIA.dirtyBit) throw new ParseCancellationException();

        JFrame frame = new JFrame("Antlr AST");
        JPanel panel = new JPanel();
        TreeViewer viewr = new TreeViewer(Arrays.asList(
                p.getRuleNames()),tree);
        viewr.setScale(1.5);//scale a little
        panel.add(viewr);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.setVisible(true);
        logger.log(Level.forName("PARSE TREE", 375), tree.toStringTree(p));
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
