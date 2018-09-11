package br.org.cristofolini;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class PortugueshTest
{
    @Test
    public void testExemplo() throws Exception {
        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exemplo.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));
//        p.addErrorListener(new BaseErrorListener() {
//            @Override
//            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
//                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
//            }
//        });
        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        p.prog();
    }

    @Test
    public void testErrado() throws Exception {
        Logger logger = LogManager.getLogger(getClass());

        PortugueshLexer l = new PortugueshLexer(new ANTLRInputStream(getClass().getResourceAsStream("/exNonsense.psh")));
        PortugueshParser p = new PortugueshParser(new CommonTokenStream(l));
//        p.addErrorListener(new BaseErrorListener() {
//            @Override
//            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
//                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
//            }
//        });
        p.addErrorListener(PortugueshErrorListener.INSTANCIA);
        p.prog();
//        if (PortugueshErrorListener.INSTANCIA.mensagensDeErro.size() > 0) {
//            String mensagens = "";
//            for (int i = 0; i < PortugueshErrorListener.INSTANCIA.mensagensDeErro.size(); i++) {
//                //mensagens += PortugueshErrorListener.INSTANCIA.mensagensDeErro.get(i) + "\n";
//                logger.error(PortugueshErrorListener.INSTANCIA.mensagensDeErro.get(i));
//            }
//            throw new ParseCancellationException(mensagens);
//        }
    }
}
