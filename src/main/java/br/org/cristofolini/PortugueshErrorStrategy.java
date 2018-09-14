package br.org.cristofolini;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortugueshErrorStrategy extends DefaultErrorStrategy {

	Logger logger = LogManager.getLogger(getClass());

	@Override
	public void recover(Parser recognizer, RecognitionException e) {
		for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
			context.exception = e;
		}
		logger.log(Level.forName("PARSER", 375), e.getMessage());
		//throw new ParseCancellationException(e);
	}
//
//	@Override
//	public Token recoverInline(Parser recognizer)
//			throws RecognitionException
//	{
//		InputMismatchException e = new InputMismatchException(recognizer);
//		for (ParserRuleContext context = recognizer.getContext(); context != null; context = context.getParent()) {
//			context.exception = e;
//		}
//
//		throw new ParseCancellationException(e);
//	}
}
