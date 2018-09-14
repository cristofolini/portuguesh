package br.org.cristofolini;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PortugueshErrorListener extends BaseErrorListener {
	static final PortugueshErrorListener INSTANCIA = new PortugueshErrorListener();
	boolean dirtyBit = false;

	@Override
	public void syntaxError(Recognizer<?,?> recognizer, Object simboloProblematico, int linha, int posicaoNaLinha, String msg, RecognitionException e) throws ParseCancellationException {
		Logger logger = LogManager.getLogger(getClass());

		logger.log(Level.forName("PARSER ERROR", 375), "linha:coluna -> " + linha + ":" + posicaoNaLinha + " " + msg);
		this.dirtyBit = true;
	}
}
