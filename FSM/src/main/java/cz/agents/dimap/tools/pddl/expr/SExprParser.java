package cz.agents.dimap.tools.pddl.expr;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.Terminals;
import org.codehaus.jparsec.pattern.CharPredicate;
import org.codehaus.jparsec.pattern.CharPredicates;

public class SExprParser {

	/*
	 * WHITESPACES 
	 */
	public static Parser<Void> IGNORED = Parsers.or(Scanners.WHITESPACES, Scanners.lineComment(";"));
	
	/*
	 * TOKENIZER
	 */
	public static final CharPredicate CHARACTER = CharPredicates.or(
		CharPredicates.IS_ALPHA_NUMERIC, 
		CharPredicates.among("_-:=?")
	); 
	
	public static final Parser<String> WORD = Scanners.isChar(CHARACTER).many1().source();
	
	public static final Terminals TOKEN = Terminals.caseInsensitive(
		WORD,
		new String[] { "(", ")" }, 
		new String[] { }
	);
	
	/*
	 * TERMINAL PARSERS
	 */
	public static final Parser<SExpr> IDENT = Terminals.Identifier.PARSER.map(SExpr.fromToken);
	public static final Parser<?> LEFT = TOKEN.token("(");
	public static final Parser<?> RIGHT = TOKEN.token(")");
	
	/*
	 * S-EXPRESSION PARSER
	 * 
	 * SExpr is either a value "V" or a list of SExpr's "(E1 E2 ... En)"
	 * 
	 */
	public static Parser<SExpr> PARSER; 
	
	static {
		Parser.Reference<SExpr> ref = Parser.newReference();
		Parser<SExpr> grammar = ref.lazy().many().between(LEFT, RIGHT).map(SExpr.fromList).or(IDENT);
		ref.set(grammar);
		PARSER = grammar.from(TOKEN.tokenizer(), IGNORED.skipMany());
	}
	
}
