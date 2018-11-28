package br.org.cristofolini;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
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
        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exemplo.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));

        ParseTree tree = p.file();
        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        p.addParseListener(PortugueshParseTreeListener.INSTANCIA);
        p.setBuildParseTree(true);

        //System.out.println(tree.toStringTree(p));
        //logger.log(Level.forName("PARSE TREE", 375), tree.toStringTree(p));
        if (PortugueshErrorListener.INSTANCIA.dirtyBit) throw new ParseCancellationException();

//        JFrame frame = new JFrame("Antlr AST");
//        JPanel panel = new JPanel();
//        TreeViewer viewr = new TreeViewer(Arrays.asList(
//                p.getRuleNames()),tree);
//        viewr.setScale(1.5);//scale a little
//        panel.add(viewr);
//        frame.add(panel);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(200,200);
//        frame.setVisible(true);
        logger.log(Level.forName("PARSE TREE", 375), tree.toStringTree(p));
        PortugueshParseTreeListener walkerListener = new PortugueshParseTreeListener();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(walkerListener, tree);
    }

    @Test
    public void testErrado() throws Exception {

        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exNonsense.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));
        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        //p.setErrorHandler(new PortugueshErrorStrategy());
        p.addParseListener(new PortugueshParseTreeListener());
        p.file();
        if (PortugueshErrorListener.INSTANCIA.dirtyBit) throw new ParseCancellationException();
    }
}
