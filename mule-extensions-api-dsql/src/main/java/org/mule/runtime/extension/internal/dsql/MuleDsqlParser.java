// $ANTLR 3.5.2 MuleDsql.g 2021-07-05 19:20:54

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.dsql;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

import org.antlr.runtime.tree.*;


@SuppressWarnings("all")
public class MuleDsqlParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "AND", "ASC", "ASTERIX", "A_", 
		"BOOLEAN_LITERAL", "BY", "B_", "CLOSING_PARENTHESIS", "COMMENT", "COMPARATOR", 
		"C_", "DATE_LITERAL", "DATE_TIME_LITERAL", "DESC", "DOUBLE_LITERAL", "D_", 
		"ESCAPE_SEQUENCE", "E_", "FROM", "F_", "G_", "HEX_DIGIT", "H_", "IDENT", 
		"INTEGER_LITERAL", "I_", "J_", "K_", "LIMIT", "L_", "MULE_EXPRESSION", 
		"M_", "NESTED_MULE_EXPRESSION", "NOT", "NULL_LITERAL", "N_", "OFFSET", 
		"OPENING_PARENTHESIS", "OPERATOR", "OR", "ORDER", "O_", "P_", "Q_", "R_", 
		"SELECT", "STRING_LITERAL", "S_", "TIME_ZONE", "TWO_DIGIT", "T_", "UNICODE_ESCAPE", 
		"U_", "V_", "WHERE", "WS", "W_", "X_", "Y_", "Z_", "','"
	};
	public static final int EOF=-1;
	public static final int T__64=64;
	public static final int AND=4;
	public static final int ASC=5;
	public static final int ASTERIX=6;
	public static final int A_=7;
	public static final int BOOLEAN_LITERAL=8;
	public static final int BY=9;
	public static final int B_=10;
	public static final int CLOSING_PARENTHESIS=11;
	public static final int COMMENT=12;
	public static final int COMPARATOR=13;
	public static final int C_=14;
	public static final int DATE_LITERAL=15;
	public static final int DATE_TIME_LITERAL=16;
	public static final int DESC=17;
	public static final int DOUBLE_LITERAL=18;
	public static final int D_=19;
	public static final int ESCAPE_SEQUENCE=20;
	public static final int E_=21;
	public static final int FROM=22;
	public static final int F_=23;
	public static final int G_=24;
	public static final int HEX_DIGIT=25;
	public static final int H_=26;
	public static final int IDENT=27;
	public static final int INTEGER_LITERAL=28;
	public static final int I_=29;
	public static final int J_=30;
	public static final int K_=31;
	public static final int LIMIT=32;
	public static final int L_=33;
	public static final int MULE_EXPRESSION=34;
	public static final int M_=35;
	public static final int NESTED_MULE_EXPRESSION=36;
	public static final int NOT=37;
	public static final int NULL_LITERAL=38;
	public static final int N_=39;
	public static final int OFFSET=40;
	public static final int OPENING_PARENTHESIS=41;
	public static final int OPERATOR=42;
	public static final int OR=43;
	public static final int ORDER=44;
	public static final int O_=45;
	public static final int P_=46;
	public static final int Q_=47;
	public static final int R_=48;
	public static final int SELECT=49;
	public static final int STRING_LITERAL=50;
	public static final int S_=51;
	public static final int TIME_ZONE=52;
	public static final int TWO_DIGIT=53;
	public static final int T_=54;
	public static final int UNICODE_ESCAPE=55;
	public static final int U_=56;
	public static final int V_=57;
	public static final int WHERE=58;
	public static final int WS=59;
	public static final int W_=60;
	public static final int X_=61;
	public static final int Y_=62;
	public static final int Z_=63;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public MuleDsqlParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public MuleDsqlParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	protected TreeAdaptor adaptor = new CommonTreeAdaptor();

	public void setTreeAdaptor(TreeAdaptor adaptor) {
		this.adaptor = adaptor;
	}
	public TreeAdaptor getTreeAdaptor() {
		return adaptor;
	}
	@Override public String[] getTokenNames() { return MuleDsqlParser.tokenNames; }
	@Override public String getGrammarFileName() { return "MuleDsql.g"; }


		protected Object recoverFromMismatchedToken(IntStream input,int ttype, BitSet follow) throws RecognitionException {
		    //System.out.println("recoverFromMismatechedToken");
	        MismatchedTokenException ex = new MismatchedTokenException(ttype, input);
			throw new org.mule.runtime.extension.internal.exception.DsqlParsingException("Invalid token at " + ex.line + ":" + ex.charPositionInLine);
		}
		
		protected void mismatch(IntStream input, int ttype, BitSet follow) throws RecognitionException {
			//System.out.println("mismatch");
	        throw new MismatchedTokenException(ttype, input);
	    }

	    public Object recoverFromMismatchedSet(IntStream input, RecognitionException e, BitSet follow) throws RecognitionException {
	    	//System.out.println("recoverFromMismatechedSet");
	        throw e;
	    }

	    public void reportError(RecognitionException e) {
	    	//System.out.println("reportError");
	        throw new org.mule.runtime.extension.internal.exception.DsqlParsingException(e);
	    }

	    public void recover(RecognitionException e) {
	    	//System.out.println("recover");
	        throw new org.mule.runtime.extension.internal.exception.DsqlParsingException(e);
	    }


	public static class select_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "select"
	// MuleDsql.g:63:1: select : SELECT ^ ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* | ASTERIX ) from ( where )? ( orderBy )? ( limit )? ( offset )? EOF EOF ;
	public final MuleDsqlParser.select_return select() throws RecognitionException {
		MuleDsqlParser.select_return retval = new MuleDsqlParser.select_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token SELECT1=null;
		Token set2=null;
		Token char_literal3=null;
		Token set4=null;
		Token ASTERIX5=null;
		Token EOF11=null;
		Token EOF12=null;
		ParserRuleReturnScope from6 =null;
		ParserRuleReturnScope where7 =null;
		ParserRuleReturnScope orderBy8 =null;
		ParserRuleReturnScope limit9 =null;
		ParserRuleReturnScope offset10 =null;

		CommonTree SELECT1_tree=null;
		CommonTree set2_tree=null;
		CommonTree char_literal3_tree=null;
		CommonTree set4_tree=null;
		CommonTree ASTERIX5_tree=null;
		CommonTree EOF11_tree=null;
		CommonTree EOF12_tree=null;

		try {
			// MuleDsql.g:63:7: ( SELECT ^ ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* | ASTERIX ) from ( where )? ( orderBy )? ( limit )? ( offset )? EOF EOF )
			// MuleDsql.g:64:7: SELECT ^ ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* | ASTERIX ) from ( where )? ( orderBy )? ( limit )? ( offset )? EOF EOF
			{
			root_0 = (CommonTree)adaptor.nil();


			SELECT1=(Token)match(input,SELECT,FOLLOW_SELECT_in_select73); 
			SELECT1_tree = (CommonTree)adaptor.create(SELECT1);
			root_0 = (CommonTree)adaptor.becomeRoot(SELECT1_tree, root_0);

			// MuleDsql.g:65:7: ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* | ASTERIX )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0==IDENT||LA2_0==STRING_LITERAL) ) {
				alt2=1;
			}
			else if ( (LA2_0==ASTERIX) ) {
				alt2=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}

			switch (alt2) {
				case 1 :
					// MuleDsql.g:65:8: ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )*
					{
					set2=input.LT(1);
					if ( input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL ) {
						input.consume();
						adaptor.addChild(root_0, (CommonTree)adaptor.create(set2));
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					// MuleDsql.g:65:32: ( ',' ! ( IDENT | STRING_LITERAL ) )*
					loop1:
					while (true) {
						int alt1=2;
						int LA1_0 = input.LA(1);
						if ( (LA1_0==64) ) {
							alt1=1;
						}

						switch (alt1) {
						case 1 :
							// MuleDsql.g:65:33: ',' ! ( IDENT | STRING_LITERAL )
							{
							char_literal3=(Token)match(input,64,FOLLOW_64_in_select91); 
							set4=input.LT(1);
							if ( input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL ) {
								input.consume();
								adaptor.addChild(root_0, (CommonTree)adaptor.create(set4));
								state.errorRecovery=false;
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								throw mse;
							}
							}
							break;

						default :
							break loop1;
						}
					}

					}
					break;
				case 2 :
					// MuleDsql.g:65:65: ASTERIX
					{
					ASTERIX5=(Token)match(input,ASTERIX,FOLLOW_ASTERIX_in_select104); 
					ASTERIX5_tree = (CommonTree)adaptor.create(ASTERIX5);
					adaptor.addChild(root_0, ASTERIX5_tree);

					}
					break;

			}

			pushFollow(FOLLOW_from_in_select113);
			from6=from();
			state._fsp--;

			adaptor.addChild(root_0, from6.getTree());

			// MuleDsql.g:67:7: ( where )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0==WHERE) ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// MuleDsql.g:67:7: where
					{
					pushFollow(FOLLOW_where_in_select122);
					where7=where();
					state._fsp--;

					adaptor.addChild(root_0, where7.getTree());

					}
					break;

			}

			// MuleDsql.g:68:7: ( orderBy )?
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0==ORDER) ) {
				alt4=1;
			}
			switch (alt4) {
				case 1 :
					// MuleDsql.g:68:7: orderBy
					{
					pushFollow(FOLLOW_orderBy_in_select131);
					orderBy8=orderBy();
					state._fsp--;

					adaptor.addChild(root_0, orderBy8.getTree());

					}
					break;

			}

			// MuleDsql.g:69:7: ( limit )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==LIMIT) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// MuleDsql.g:69:7: limit
					{
					pushFollow(FOLLOW_limit_in_select140);
					limit9=limit();
					state._fsp--;

					adaptor.addChild(root_0, limit9.getTree());

					}
					break;

			}

			// MuleDsql.g:70:7: ( offset )?
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==OFFSET) ) {
				alt6=1;
			}
			switch (alt6) {
				case 1 :
					// MuleDsql.g:70:7: offset
					{
					pushFollow(FOLLOW_offset_in_select149);
					offset10=offset();
					state._fsp--;

					adaptor.addChild(root_0, offset10.getTree());

					}
					break;

			}

			EOF11=(Token)match(input,EOF,FOLLOW_EOF_in_select158); 
			EOF11_tree = (CommonTree)adaptor.create(EOF11);
			adaptor.addChild(root_0, EOF11_tree);

			EOF12=(Token)match(input,EOF,FOLLOW_EOF_in_select166); 
			EOF12_tree = (CommonTree)adaptor.create(EOF12);
			adaptor.addChild(root_0, EOF12_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "select"


	public static class from_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "from"
	// MuleDsql.g:75:1: from : FROM ^ ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* ;
	public final MuleDsqlParser.from_return from() throws RecognitionException {
		MuleDsqlParser.from_return retval = new MuleDsqlParser.from_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token FROM13=null;
		Token set14=null;
		Token char_literal15=null;
		Token set16=null;

		CommonTree FROM13_tree=null;
		CommonTree set14_tree=null;
		CommonTree char_literal15_tree=null;
		CommonTree set16_tree=null;

		try {
			// MuleDsql.g:75:5: ( FROM ^ ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* )
			// MuleDsql.g:76:7: FROM ^ ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )*
			{
			root_0 = (CommonTree)adaptor.nil();


			FROM13=(Token)match(input,FROM,FOLLOW_FROM_in_from186); 
			FROM13_tree = (CommonTree)adaptor.create(FROM13);
			root_0 = (CommonTree)adaptor.becomeRoot(FROM13_tree, root_0);

			set14=input.LT(1);
			if ( input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL ) {
				input.consume();
				adaptor.addChild(root_0, (CommonTree)adaptor.create(set14));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			// MuleDsql.g:77:31: ( ',' ! ( IDENT | STRING_LITERAL ) )*
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0==64) ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// MuleDsql.g:77:32: ',' ! ( IDENT | STRING_LITERAL )
					{
					char_literal15=(Token)match(input,64,FOLLOW_64_in_from203); 
					set16=input.LT(1);
					if ( input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL ) {
						input.consume();
						adaptor.addChild(root_0, (CommonTree)adaptor.create(set16));
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					}
					break;

				default :
					break loop7;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "from"


	public static class where_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "where"
	// MuleDsql.g:79:1: where : WHERE ^ expression ;
	public final MuleDsqlParser.where_return where() throws RecognitionException {
		MuleDsqlParser.where_return retval = new MuleDsqlParser.where_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token WHERE17=null;
		ParserRuleReturnScope expression18 =null;

		CommonTree WHERE17_tree=null;

		try {
			// MuleDsql.g:79:6: ( WHERE ^ expression )
			// MuleDsql.g:80:7: WHERE ^ expression
			{
			root_0 = (CommonTree)adaptor.nil();


			WHERE17=(Token)match(input,WHERE,FOLLOW_WHERE_in_where227); 
			WHERE17_tree = (CommonTree)adaptor.create(WHERE17);
			root_0 = (CommonTree)adaptor.becomeRoot(WHERE17_tree, root_0);

			pushFollow(FOLLOW_expression_in_where236);
			expression18=expression();
			state._fsp--;

			adaptor.addChild(root_0, expression18.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "where"


	public static class orderBy_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "orderBy"
	// MuleDsql.g:83:1: orderBy : ORDER ^ BY ! ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* ) ( direction )? ;
	public final MuleDsqlParser.orderBy_return orderBy() throws RecognitionException {
		MuleDsqlParser.orderBy_return retval = new MuleDsqlParser.orderBy_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token ORDER19=null;
		Token BY20=null;
		Token set21=null;
		Token char_literal22=null;
		Token set23=null;
		ParserRuleReturnScope direction24 =null;

		CommonTree ORDER19_tree=null;
		CommonTree BY20_tree=null;
		CommonTree set21_tree=null;
		CommonTree char_literal22_tree=null;
		CommonTree set23_tree=null;

		try {
			// MuleDsql.g:83:8: ( ORDER ^ BY ! ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* ) ( direction )? )
			// MuleDsql.g:84:5: ORDER ^ BY ! ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* ) ( direction )?
			{
			root_0 = (CommonTree)adaptor.nil();


			ORDER19=(Token)match(input,ORDER,FOLLOW_ORDER_in_orderBy251); 
			ORDER19_tree = (CommonTree)adaptor.create(ORDER19);
			root_0 = (CommonTree)adaptor.becomeRoot(ORDER19_tree, root_0);

			BY20=(Token)match(input,BY,FOLLOW_BY_in_orderBy254); 
			// MuleDsql.g:85:5: ( ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )* )
			// MuleDsql.g:85:6: ( IDENT | STRING_LITERAL ) ( ',' ! ( IDENT | STRING_LITERAL ) )*
			{
			set21=input.LT(1);
			if ( input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL ) {
				input.consume();
				adaptor.addChild(root_0, (CommonTree)adaptor.create(set21));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			// MuleDsql.g:85:30: ( ',' ! ( IDENT | STRING_LITERAL ) )*
			loop8:
			while (true) {
				int alt8=2;
				int LA8_0 = input.LA(1);
				if ( (LA8_0==64) ) {
					alt8=1;
				}

				switch (alt8) {
				case 1 :
					// MuleDsql.g:85:31: ',' ! ( IDENT | STRING_LITERAL )
					{
					char_literal22=(Token)match(input,64,FOLLOW_64_in_orderBy271); 
					set23=input.LT(1);
					if ( input.LA(1)==IDENT||input.LA(1)==STRING_LITERAL ) {
						input.consume();
						adaptor.addChild(root_0, (CommonTree)adaptor.create(set23));
						state.errorRecovery=false;
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						throw mse;
					}
					}
					break;

				default :
					break loop8;
				}
			}

			}

			// MuleDsql.g:86:5: ( direction )?
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0==ASC||LA9_0==DESC) ) {
				alt9=1;
			}
			switch (alt9) {
				case 1 :
					// MuleDsql.g:86:5: direction
					{
					pushFollow(FOLLOW_direction_in_orderBy289);
					direction24=direction();
					state._fsp--;

					adaptor.addChild(root_0, direction24.getTree());

					}
					break;

			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "orderBy"


	public static class direction_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "direction"
	// MuleDsql.g:88:1: direction : ( ASC | DESC ) ;
	public final MuleDsqlParser.direction_return direction() throws RecognitionException {
		MuleDsqlParser.direction_return retval = new MuleDsqlParser.direction_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token set25=null;

		CommonTree set25_tree=null;

		try {
			// MuleDsql.g:88:10: ( ( ASC | DESC ) )
			// MuleDsql.g:
			{
			root_0 = (CommonTree)adaptor.nil();


			set25=input.LT(1);
			if ( input.LA(1)==ASC||input.LA(1)==DESC ) {
				input.consume();
				adaptor.addChild(root_0, (CommonTree)adaptor.create(set25));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "direction"


	public static class limit_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "limit"
	// MuleDsql.g:91:1: limit : LIMIT ^ number ;
	public final MuleDsqlParser.limit_return limit() throws RecognitionException {
		MuleDsqlParser.limit_return retval = new MuleDsqlParser.limit_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token LIMIT26=null;
		ParserRuleReturnScope number27 =null;

		CommonTree LIMIT26_tree=null;

		try {
			// MuleDsql.g:91:6: ( LIMIT ^ number )
			// MuleDsql.g:92:5: LIMIT ^ number
			{
			root_0 = (CommonTree)adaptor.nil();


			LIMIT26=(Token)match(input,LIMIT,FOLLOW_LIMIT_in_limit313); 
			LIMIT26_tree = (CommonTree)adaptor.create(LIMIT26);
			root_0 = (CommonTree)adaptor.becomeRoot(LIMIT26_tree, root_0);

			pushFollow(FOLLOW_number_in_limit320);
			number27=number();
			state._fsp--;

			adaptor.addChild(root_0, number27.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "limit"


	public static class offset_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "offset"
	// MuleDsql.g:95:1: offset : OFFSET ^ number ;
	public final MuleDsqlParser.offset_return offset() throws RecognitionException {
		MuleDsqlParser.offset_return retval = new MuleDsqlParser.offset_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OFFSET28=null;
		ParserRuleReturnScope number29 =null;

		CommonTree OFFSET28_tree=null;

		try {
			// MuleDsql.g:95:7: ( OFFSET ^ number )
			// MuleDsql.g:96:5: OFFSET ^ number
			{
			root_0 = (CommonTree)adaptor.nil();


			OFFSET28=(Token)match(input,OFFSET,FOLLOW_OFFSET_in_offset335); 
			OFFSET28_tree = (CommonTree)adaptor.create(OFFSET28);
			root_0 = (CommonTree)adaptor.becomeRoot(OFFSET28_tree, root_0);

			pushFollow(FOLLOW_number_in_offset342);
			number29=number();
			state._fsp--;

			adaptor.addChild(root_0, number29.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "offset"


	public static class string_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "string"
	// MuleDsql.g:99:1: string : STRING_LITERAL ;
	public final MuleDsqlParser.string_return string() throws RecognitionException {
		MuleDsqlParser.string_return retval = new MuleDsqlParser.string_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token STRING_LITERAL30=null;

		CommonTree STRING_LITERAL30_tree=null;

		try {
			// MuleDsql.g:99:7: ( STRING_LITERAL )
			// MuleDsql.g:100:5: STRING_LITERAL
			{
			root_0 = (CommonTree)adaptor.nil();


			STRING_LITERAL30=(Token)match(input,STRING_LITERAL,FOLLOW_STRING_LITERAL_in_string357); 
			STRING_LITERAL30_tree = (CommonTree)adaptor.create(STRING_LITERAL30);
			adaptor.addChild(root_0, STRING_LITERAL30_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "string"


	public static class bool_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "bool"
	// MuleDsql.g:102:1: bool : BOOLEAN_LITERAL ;
	public final MuleDsqlParser.bool_return bool() throws RecognitionException {
		MuleDsqlParser.bool_return retval = new MuleDsqlParser.bool_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token BOOLEAN_LITERAL31=null;

		CommonTree BOOLEAN_LITERAL31_tree=null;

		try {
			// MuleDsql.g:102:5: ( BOOLEAN_LITERAL )
			// MuleDsql.g:103:5: BOOLEAN_LITERAL
			{
			root_0 = (CommonTree)adaptor.nil();


			BOOLEAN_LITERAL31=(Token)match(input,BOOLEAN_LITERAL,FOLLOW_BOOLEAN_LITERAL_in_bool368); 
			BOOLEAN_LITERAL31_tree = (CommonTree)adaptor.create(BOOLEAN_LITERAL31);
			adaptor.addChild(root_0, BOOLEAN_LITERAL31_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "bool"


	public static class date_time_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "date_time"
	// MuleDsql.g:105:1: date_time : DATE_TIME_LITERAL ;
	public final MuleDsqlParser.date_time_return date_time() throws RecognitionException {
		MuleDsqlParser.date_time_return retval = new MuleDsqlParser.date_time_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token DATE_TIME_LITERAL32=null;

		CommonTree DATE_TIME_LITERAL32_tree=null;

		try {
			// MuleDsql.g:105:10: ( DATE_TIME_LITERAL )
			// MuleDsql.g:106:5: DATE_TIME_LITERAL
			{
			root_0 = (CommonTree)adaptor.nil();


			DATE_TIME_LITERAL32=(Token)match(input,DATE_TIME_LITERAL,FOLLOW_DATE_TIME_LITERAL_in_date_time379); 
			DATE_TIME_LITERAL32_tree = (CommonTree)adaptor.create(DATE_TIME_LITERAL32);
			adaptor.addChild(root_0, DATE_TIME_LITERAL32_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date_time"


	public static class date_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "date"
	// MuleDsql.g:108:1: date : DATE_LITERAL ;
	public final MuleDsqlParser.date_return date() throws RecognitionException {
		MuleDsqlParser.date_return retval = new MuleDsqlParser.date_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token DATE_LITERAL33=null;

		CommonTree DATE_LITERAL33_tree=null;

		try {
			// MuleDsql.g:108:5: ( DATE_LITERAL )
			// MuleDsql.g:109:5: DATE_LITERAL
			{
			root_0 = (CommonTree)adaptor.nil();


			DATE_LITERAL33=(Token)match(input,DATE_LITERAL,FOLLOW_DATE_LITERAL_in_date390); 
			DATE_LITERAL33_tree = (CommonTree)adaptor.create(DATE_LITERAL33);
			adaptor.addChild(root_0, DATE_LITERAL33_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "date"


	public static class number_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "number"
	// MuleDsql.g:111:1: number : ( DOUBLE_LITERAL | INTEGER_LITERAL | MULE_EXPRESSION );
	public final MuleDsqlParser.number_return number() throws RecognitionException {
		MuleDsqlParser.number_return retval = new MuleDsqlParser.number_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token set34=null;

		CommonTree set34_tree=null;

		try {
			// MuleDsql.g:111:7: ( DOUBLE_LITERAL | INTEGER_LITERAL | MULE_EXPRESSION )
			// MuleDsql.g:
			{
			root_0 = (CommonTree)adaptor.nil();


			set34=input.LT(1);
			if ( input.LA(1)==DOUBLE_LITERAL||input.LA(1)==INTEGER_LITERAL||input.LA(1)==MULE_EXPRESSION ) {
				input.consume();
				adaptor.addChild(root_0, (CommonTree)adaptor.create(set34));
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}
			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "number"


	public static class null_type_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "null_type"
	// MuleDsql.g:114:1: null_type : NULL_LITERAL ;
	public final MuleDsqlParser.null_type_return null_type() throws RecognitionException {
		MuleDsqlParser.null_type_return retval = new MuleDsqlParser.null_type_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token NULL_LITERAL35=null;

		CommonTree NULL_LITERAL35_tree=null;

		try {
			// MuleDsql.g:114:10: ( NULL_LITERAL )
			// MuleDsql.g:115:3: NULL_LITERAL
			{
			root_0 = (CommonTree)adaptor.nil();


			NULL_LITERAL35=(Token)match(input,NULL_LITERAL,FOLLOW_NULL_LITERAL_in_null_type416); 
			NULL_LITERAL35_tree = (CommonTree)adaptor.create(NULL_LITERAL35);
			adaptor.addChild(root_0, NULL_LITERAL35_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "null_type"


	public static class identifier_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "identifier"
	// MuleDsql.g:117:1: identifier : IDENT ;
	public final MuleDsqlParser.identifier_return identifier() throws RecognitionException {
		MuleDsqlParser.identifier_return retval = new MuleDsqlParser.identifier_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token IDENT36=null;

		CommonTree IDENT36_tree=null;

		try {
			// MuleDsql.g:117:11: ( IDENT )
			// MuleDsql.g:118:3: IDENT
			{
			root_0 = (CommonTree)adaptor.nil();


			IDENT36=(Token)match(input,IDENT,FOLLOW_IDENT_in_identifier425); 
			IDENT36_tree = (CommonTree)adaptor.create(IDENT36);
			adaptor.addChild(root_0, IDENT36_tree);

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "identifier"


	public static class term_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "term"
	// MuleDsql.g:120:1: term : ( identifier | OPENING_PARENTHESIS ^ expression CLOSING_PARENTHESIS !| string | number | bool | date_time | date | null_type );
	public final MuleDsqlParser.term_return term() throws RecognitionException {
		MuleDsqlParser.term_return retval = new MuleDsqlParser.term_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPENING_PARENTHESIS38=null;
		Token CLOSING_PARENTHESIS40=null;
		ParserRuleReturnScope identifier37 =null;
		ParserRuleReturnScope expression39 =null;
		ParserRuleReturnScope string41 =null;
		ParserRuleReturnScope number42 =null;
		ParserRuleReturnScope bool43 =null;
		ParserRuleReturnScope date_time44 =null;
		ParserRuleReturnScope date45 =null;
		ParserRuleReturnScope null_type46 =null;

		CommonTree OPENING_PARENTHESIS38_tree=null;
		CommonTree CLOSING_PARENTHESIS40_tree=null;

		try {
			// MuleDsql.g:120:5: ( identifier | OPENING_PARENTHESIS ^ expression CLOSING_PARENTHESIS !| string | number | bool | date_time | date | null_type )
			int alt10=8;
			switch ( input.LA(1) ) {
			case IDENT:
				{
				alt10=1;
				}
				break;
			case OPENING_PARENTHESIS:
				{
				alt10=2;
				}
				break;
			case STRING_LITERAL:
				{
				alt10=3;
				}
				break;
			case DOUBLE_LITERAL:
			case INTEGER_LITERAL:
			case MULE_EXPRESSION:
				{
				alt10=4;
				}
				break;
			case BOOLEAN_LITERAL:
				{
				alt10=5;
				}
				break;
			case DATE_TIME_LITERAL:
				{
				alt10=6;
				}
				break;
			case DATE_LITERAL:
				{
				alt10=7;
				}
				break;
			case NULL_LITERAL:
				{
				alt10=8;
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}
			switch (alt10) {
				case 1 :
					// MuleDsql.g:121:5: identifier
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_identifier_in_term440);
					identifier37=identifier();
					state._fsp--;

					adaptor.addChild(root_0, identifier37.getTree());

					}
					break;
				case 2 :
					// MuleDsql.g:122:7: OPENING_PARENTHESIS ^ expression CLOSING_PARENTHESIS !
					{
					root_0 = (CommonTree)adaptor.nil();


					OPENING_PARENTHESIS38=(Token)match(input,OPENING_PARENTHESIS,FOLLOW_OPENING_PARENTHESIS_in_term448); 
					OPENING_PARENTHESIS38_tree = (CommonTree)adaptor.create(OPENING_PARENTHESIS38);
					root_0 = (CommonTree)adaptor.becomeRoot(OPENING_PARENTHESIS38_tree, root_0);

					pushFollow(FOLLOW_expression_in_term450);
					expression39=expression();
					state._fsp--;

					adaptor.addChild(root_0, expression39.getTree());

					CLOSING_PARENTHESIS40=(Token)match(input,CLOSING_PARENTHESIS,FOLLOW_CLOSING_PARENTHESIS_in_term452); 
					}
					break;
				case 3 :
					// MuleDsql.g:123:7: string
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_string_in_term461);
					string41=string();
					state._fsp--;

					adaptor.addChild(root_0, string41.getTree());

					}
					break;
				case 4 :
					// MuleDsql.g:124:7: number
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_number_in_term469);
					number42=number();
					state._fsp--;

					adaptor.addChild(root_0, number42.getTree());

					}
					break;
				case 5 :
					// MuleDsql.g:125:7: bool
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_bool_in_term477);
					bool43=bool();
					state._fsp--;

					adaptor.addChild(root_0, bool43.getTree());

					}
					break;
				case 6 :
					// MuleDsql.g:126:7: date_time
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_date_time_in_term485);
					date_time44=date_time();
					state._fsp--;

					adaptor.addChild(root_0, date_time44.getTree());

					}
					break;
				case 7 :
					// MuleDsql.g:127:7: date
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_date_in_term493);
					date45=date();
					state._fsp--;

					adaptor.addChild(root_0, date45.getTree());

					}
					break;
				case 8 :
					// MuleDsql.g:128:7: null_type
					{
					root_0 = (CommonTree)adaptor.nil();


					pushFollow(FOLLOW_null_type_in_term501);
					null_type46=null_type();
					state._fsp--;

					adaptor.addChild(root_0, null_type46.getTree());

					}
					break;

			}
			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "term"


	public static class negation_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "negation"
	// MuleDsql.g:130:1: negation : ( NOT ^)* term ;
	public final MuleDsqlParser.negation_return negation() throws RecognitionException {
		MuleDsqlParser.negation_return retval = new MuleDsqlParser.negation_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token NOT47=null;
		ParserRuleReturnScope term48 =null;

		CommonTree NOT47_tree=null;

		try {
			// MuleDsql.g:130:9: ( ( NOT ^)* term )
			// MuleDsql.g:131:7: ( NOT ^)* term
			{
			root_0 = (CommonTree)adaptor.nil();


			// MuleDsql.g:131:10: ( NOT ^)*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( (LA11_0==NOT) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// MuleDsql.g:131:10: NOT ^
					{
					NOT47=(Token)match(input,NOT,FOLLOW_NOT_in_negation518); 
					NOT47_tree = (CommonTree)adaptor.create(NOT47);
					root_0 = (CommonTree)adaptor.becomeRoot(NOT47_tree, root_0);

					}
					break;

				default :
					break loop11;
				}
			}

			pushFollow(FOLLOW_term_in_negation522);
			term48=term();
			state._fsp--;

			adaptor.addChild(root_0, term48.getTree());

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "negation"


	public static class relation_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "relation"
	// MuleDsql.g:133:1: relation : negation ( ( OPERATOR ^| COMPARATOR ^) negation )* ;
	public final MuleDsqlParser.relation_return relation() throws RecognitionException {
		MuleDsqlParser.relation_return retval = new MuleDsqlParser.relation_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token OPERATOR50=null;
		Token COMPARATOR51=null;
		ParserRuleReturnScope negation49 =null;
		ParserRuleReturnScope negation52 =null;

		CommonTree OPERATOR50_tree=null;
		CommonTree COMPARATOR51_tree=null;

		try {
			// MuleDsql.g:133:9: ( negation ( ( OPERATOR ^| COMPARATOR ^) negation )* )
			// MuleDsql.g:134:5: negation ( ( OPERATOR ^| COMPARATOR ^) negation )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_negation_in_relation533);
			negation49=negation();
			state._fsp--;

			adaptor.addChild(root_0, negation49.getTree());

			// MuleDsql.g:134:14: ( ( OPERATOR ^| COMPARATOR ^) negation )*
			loop13:
			while (true) {
				int alt13=2;
				int LA13_0 = input.LA(1);
				if ( (LA13_0==COMPARATOR||LA13_0==OPERATOR) ) {
					alt13=1;
				}

				switch (alt13) {
				case 1 :
					// MuleDsql.g:134:15: ( OPERATOR ^| COMPARATOR ^) negation
					{
					// MuleDsql.g:134:15: ( OPERATOR ^| COMPARATOR ^)
					int alt12=2;
					int LA12_0 = input.LA(1);
					if ( (LA12_0==OPERATOR) ) {
						alt12=1;
					}
					else if ( (LA12_0==COMPARATOR) ) {
						alt12=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 12, 0, input);
						throw nvae;
					}

					switch (alt12) {
						case 1 :
							// MuleDsql.g:134:16: OPERATOR ^
							{
							OPERATOR50=(Token)match(input,OPERATOR,FOLLOW_OPERATOR_in_relation537); 
							OPERATOR50_tree = (CommonTree)adaptor.create(OPERATOR50);
							root_0 = (CommonTree)adaptor.becomeRoot(OPERATOR50_tree, root_0);

							}
							break;
						case 2 :
							// MuleDsql.g:134:26: COMPARATOR ^
							{
							COMPARATOR51=(Token)match(input,COMPARATOR,FOLLOW_COMPARATOR_in_relation540); 
							COMPARATOR51_tree = (CommonTree)adaptor.create(COMPARATOR51);
							root_0 = (CommonTree)adaptor.becomeRoot(COMPARATOR51_tree, root_0);

							}
							break;

					}

					pushFollow(FOLLOW_negation_in_relation544);
					negation52=negation();
					state._fsp--;

					adaptor.addChild(root_0, negation52.getTree());

					}
					break;

				default :
					break loop13;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "relation"


	public static class expression_return extends ParserRuleReturnScope {
		CommonTree tree;
		@Override
		public CommonTree getTree() { return tree; }
	};


	// $ANTLR start "expression"
	// MuleDsql.g:136:1: expression : relation ( ( AND ^| OR ^) relation )* ;
	public final MuleDsqlParser.expression_return expression() throws RecognitionException {
		MuleDsqlParser.expression_return retval = new MuleDsqlParser.expression_return();
		retval.start = input.LT(1);

		CommonTree root_0 = null;

		Token AND54=null;
		Token OR55=null;
		ParserRuleReturnScope relation53 =null;
		ParserRuleReturnScope relation56 =null;

		CommonTree AND54_tree=null;
		CommonTree OR55_tree=null;

		try {
			// MuleDsql.g:136:11: ( relation ( ( AND ^| OR ^) relation )* )
			// MuleDsql.g:137:7: relation ( ( AND ^| OR ^) relation )*
			{
			root_0 = (CommonTree)adaptor.nil();


			pushFollow(FOLLOW_relation_in_expression559);
			relation53=relation();
			state._fsp--;

			adaptor.addChild(root_0, relation53.getTree());

			// MuleDsql.g:137:16: ( ( AND ^| OR ^) relation )*
			loop15:
			while (true) {
				int alt15=2;
				int LA15_0 = input.LA(1);
				if ( (LA15_0==AND||LA15_0==OR) ) {
					alt15=1;
				}

				switch (alt15) {
				case 1 :
					// MuleDsql.g:137:17: ( AND ^| OR ^) relation
					{
					// MuleDsql.g:137:17: ( AND ^| OR ^)
					int alt14=2;
					int LA14_0 = input.LA(1);
					if ( (LA14_0==AND) ) {
						alt14=1;
					}
					else if ( (LA14_0==OR) ) {
						alt14=2;
					}

					else {
						NoViableAltException nvae =
							new NoViableAltException("", 14, 0, input);
						throw nvae;
					}

					switch (alt14) {
						case 1 :
							// MuleDsql.g:137:18: AND ^
							{
							AND54=(Token)match(input,AND,FOLLOW_AND_in_expression563); 
							AND54_tree = (CommonTree)adaptor.create(AND54);
							root_0 = (CommonTree)adaptor.becomeRoot(AND54_tree, root_0);

							}
							break;
						case 2 :
							// MuleDsql.g:137:23: OR ^
							{
							OR55=(Token)match(input,OR,FOLLOW_OR_in_expression566); 
							OR55_tree = (CommonTree)adaptor.create(OR55);
							root_0 = (CommonTree)adaptor.becomeRoot(OR55_tree, root_0);

							}
							break;

					}

					pushFollow(FOLLOW_relation_in_expression570);
					relation56=relation();
					state._fsp--;

					adaptor.addChild(root_0, relation56.getTree());

					}
					break;

				default :
					break loop15;
				}
			}

			}

			retval.stop = input.LT(-1);

			retval.tree = (CommonTree)adaptor.rulePostProcessing(root_0);
			adaptor.setTokenBoundaries(retval.tree, retval.start, retval.stop);

		}

		    catch (RecognitionException e) {
		        throw e;
		    }

		finally {
			// do for sure before leaving
		}
		return retval;
	}
	// $ANTLR end "expression"

	// Delegated rules



	public static final BitSet FOLLOW_SELECT_in_select73 = new BitSet(new long[]{0x0004000008000040L});
	public static final BitSet FOLLOW_set_in_select83 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000001L});
	public static final BitSet FOLLOW_64_in_select91 = new BitSet(new long[]{0x0004000008000000L});
	public static final BitSet FOLLOW_set_in_select94 = new BitSet(new long[]{0x0000000000400000L,0x0000000000000001L});
	public static final BitSet FOLLOW_ASTERIX_in_select104 = new BitSet(new long[]{0x0000000000400000L});
	public static final BitSet FOLLOW_from_in_select113 = new BitSet(new long[]{0x0400110100000000L});
	public static final BitSet FOLLOW_where_in_select122 = new BitSet(new long[]{0x0000110100000000L});
	public static final BitSet FOLLOW_orderBy_in_select131 = new BitSet(new long[]{0x0000010100000000L});
	public static final BitSet FOLLOW_limit_in_select140 = new BitSet(new long[]{0x0000010000000000L});
	public static final BitSet FOLLOW_offset_in_select149 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_select158 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_select166 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_FROM_in_from186 = new BitSet(new long[]{0x0004000008000000L});
	public static final BitSet FOLLOW_set_in_from195 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_64_in_from203 = new BitSet(new long[]{0x0004000008000000L});
	public static final BitSet FOLLOW_set_in_from206 = new BitSet(new long[]{0x0000000000000002L,0x0000000000000001L});
	public static final BitSet FOLLOW_WHERE_in_where227 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_expression_in_where236 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ORDER_in_orderBy251 = new BitSet(new long[]{0x0000000000000200L});
	public static final BitSet FOLLOW_BY_in_orderBy254 = new BitSet(new long[]{0x0004000008000000L});
	public static final BitSet FOLLOW_set_in_orderBy263 = new BitSet(new long[]{0x0000000000020022L,0x0000000000000001L});
	public static final BitSet FOLLOW_64_in_orderBy271 = new BitSet(new long[]{0x0004000008000000L});
	public static final BitSet FOLLOW_set_in_orderBy274 = new BitSet(new long[]{0x0000000000020022L,0x0000000000000001L});
	public static final BitSet FOLLOW_direction_in_orderBy289 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LIMIT_in_limit313 = new BitSet(new long[]{0x0000000410040000L});
	public static final BitSet FOLLOW_number_in_limit320 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OFFSET_in_offset335 = new BitSet(new long[]{0x0000000410040000L});
	public static final BitSet FOLLOW_number_in_offset342 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_LITERAL_in_string357 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_BOOLEAN_LITERAL_in_bool368 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DATE_TIME_LITERAL_in_date_time379 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_DATE_LITERAL_in_date390 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NULL_LITERAL_in_null_type416 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_IDENT_in_identifier425 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_identifier_in_term440 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_OPENING_PARENTHESIS_in_term448 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_expression_in_term450 = new BitSet(new long[]{0x0000000000000800L});
	public static final BitSet FOLLOW_CLOSING_PARENTHESIS_in_term452 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_string_in_term461 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_number_in_term469 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_bool_in_term477 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_time_in_term485 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_date_in_term493 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_null_type_in_term501 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_NOT_in_negation518 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_term_in_negation522 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_negation_in_relation533 = new BitSet(new long[]{0x0000040000002002L});
	public static final BitSet FOLLOW_OPERATOR_in_relation537 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_COMPARATOR_in_relation540 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_negation_in_relation544 = new BitSet(new long[]{0x0000040000002002L});
	public static final BitSet FOLLOW_relation_in_expression559 = new BitSet(new long[]{0x0000080000000012L});
	public static final BitSet FOLLOW_AND_in_expression563 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_OR_in_expression566 = new BitSet(new long[]{0x0004026418058100L});
	public static final BitSet FOLLOW_relation_in_expression570 = new BitSet(new long[]{0x0000080000000012L});
}
