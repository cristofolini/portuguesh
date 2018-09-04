package br.org.cristofolini;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.List;

public class PortugueshErrorListener extends BaseErrorListener {
	static final PortugueshErrorListener INSTANCIA = new PortugueshErrorListener();

	public List<String> mensagensDeErro = new ArrayList<>();

	@Override
	public void syntaxError(Recognizer<?,?> recognizer, Object simboloProblematico, int linha, int posicaoNaLinha, String msg, RecognitionException e) throws ParseCancellationException {
		mensagensDeErro.add("linha:coluna -> " + linha + ":" + posicaoNaLinha + " " + msg);
		//throw new ParseCancellationException("linha:coluna -> " + linha + ":" + posicaoNaLinha + " " + msg);
	}
}
