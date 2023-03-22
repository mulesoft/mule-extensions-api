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

@SuppressWarnings("all")
public class MuleDsqlLexer extends Lexer {
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
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public MuleDsqlLexer() {} 
	public MuleDsqlLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public MuleDsqlLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "MuleDsql.g"; }

	// $ANTLR start "T__64"
	public final void mT__64() throws RecognitionException {
		try {
			int _type = T__64;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:17:7: ( ',' )
			// MuleDsql.g:17:9: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__64"

	// $ANTLR start "ASC"
	public final void mASC() throws RecognitionException {
		try {
			int _type = ASC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:139:5: ( ( A_ S_ C_ | A_ S_ C_ E_ N_ D_ I_ N_ G_ ) )
			// MuleDsql.g:139:7: ( A_ S_ C_ | A_ S_ C_ E_ N_ D_ I_ N_ G_ )
			{
			// MuleDsql.g:139:7: ( A_ S_ C_ | A_ S_ C_ E_ N_ D_ I_ N_ G_ )
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0=='A'||LA1_0=='a') ) {
				int LA1_1 = input.LA(2);
				if ( (LA1_1=='S'||LA1_1=='s') ) {
					int LA1_2 = input.LA(3);
					if ( (LA1_2=='C'||LA1_2=='c') ) {
						int LA1_3 = input.LA(4);
						if ( (LA1_3=='E'||LA1_3=='e') ) {
							alt1=2;
						}

						else {
							alt1=1;
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 1, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 1, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}

			switch (alt1) {
				case 1 :
					// MuleDsql.g:139:8: A_ S_ C_
					{
					mA_(); 

					mS_(); 

					mC_(); 

					}
					break;
				case 2 :
					// MuleDsql.g:139:19: A_ S_ C_ E_ N_ D_ I_ N_ G_
					{
					mA_(); 

					mS_(); 

					mC_(); 

					mE_(); 

					mN_(); 

					mD_(); 

					mI_(); 

					mN_(); 

					mG_(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASC"

	// $ANTLR start "DESC"
	public final void mDESC() throws RecognitionException {
		try {
			int _type = DESC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:140:6: ( ( D_ E_ S_ C_ | D_ E_ S_ C_ E_ N_ D_ I_ N_ G_ ) )
			// MuleDsql.g:140:8: ( D_ E_ S_ C_ | D_ E_ S_ C_ E_ N_ D_ I_ N_ G_ )
			{
			// MuleDsql.g:140:8: ( D_ E_ S_ C_ | D_ E_ S_ C_ E_ N_ D_ I_ N_ G_ )
			int alt2=2;
			int LA2_0 = input.LA(1);
			if ( (LA2_0=='D'||LA2_0=='d') ) {
				int LA2_1 = input.LA(2);
				if ( (LA2_1=='E'||LA2_1=='e') ) {
					int LA2_2 = input.LA(3);
					if ( (LA2_2=='S'||LA2_2=='s') ) {
						int LA2_3 = input.LA(4);
						if ( (LA2_3=='C'||LA2_3=='c') ) {
							int LA2_4 = input.LA(5);
							if ( (LA2_4=='E'||LA2_4=='e') ) {
								alt2=2;
							}

							else {
								alt2=1;
							}

						}

						else {
							int nvaeMark = input.mark();
							try {
								for (int nvaeConsume = 0; nvaeConsume < 4 - 1; nvaeConsume++) {
									input.consume();
								}
								NoViableAltException nvae =
									new NoViableAltException("", 2, 3, input);
								throw nvae;
							} finally {
								input.rewind(nvaeMark);
							}
						}

					}

					else {
						int nvaeMark = input.mark();
						try {
							for (int nvaeConsume = 0; nvaeConsume < 3 - 1; nvaeConsume++) {
								input.consume();
							}
							NoViableAltException nvae =
								new NoViableAltException("", 2, 2, input);
							throw nvae;
						} finally {
							input.rewind(nvaeMark);
						}
					}

				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 2, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 2, 0, input);
				throw nvae;
			}

			switch (alt2) {
				case 1 :
					// MuleDsql.g:140:9: D_ E_ S_ C_
					{
					mD_(); 

					mE_(); 

					mS_(); 

					mC_(); 

					}
					break;
				case 2 :
					// MuleDsql.g:140:23: D_ E_ S_ C_ E_ N_ D_ I_ N_ G_
					{
					mD_(); 

					mE_(); 

					mS_(); 

					mC_(); 

					mE_(); 

					mN_(); 

					mD_(); 

					mI_(); 

					mN_(); 

					mG_(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DESC"

	// $ANTLR start "SELECT"
	public final void mSELECT() throws RecognitionException {
		try {
			int _type = SELECT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:141:9: ( S_ E_ L_ E_ C_ T_ )
			// MuleDsql.g:141:11: S_ E_ L_ E_ C_ T_
			{
			mS_(); 

			mE_(); 

			mL_(); 

			mE_(); 

			mC_(); 

			mT_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "SELECT"

	// $ANTLR start "FROM"
	public final void mFROM() throws RecognitionException {
		try {
			int _type = FROM;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:142:7: ( F_ R_ O_ M_ )
			// MuleDsql.g:142:9: F_ R_ O_ M_
			{
			mF_(); 

			mR_(); 

			mO_(); 

			mM_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "FROM"

	// $ANTLR start "WHERE"
	public final void mWHERE() throws RecognitionException {
		try {
			int _type = WHERE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:143:8: ( W_ H_ E_ R_ E_ )
			// MuleDsql.g:143:10: W_ H_ E_ R_ E_
			{
			mW_(); 

			mH_(); 

			mE_(); 

			mR_(); 

			mE_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WHERE"

	// $ANTLR start "ORDER"
	public final void mORDER() throws RecognitionException {
		try {
			int _type = ORDER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:144:6: ( O_ R_ D_ E_ R_ )
			// MuleDsql.g:144:8: O_ R_ D_ E_ R_
			{
			mO_(); 

			mR_(); 

			mD_(); 

			mE_(); 

			mR_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ORDER"

	// $ANTLR start "BY"
	public final void mBY() throws RecognitionException {
		try {
			int _type = BY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:145:3: ( B_ Y_ )
			// MuleDsql.g:145:5: B_ Y_
			{
			mB_(); 

			mY_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BY"

	// $ANTLR start "LIMIT"
	public final void mLIMIT() throws RecognitionException {
		try {
			int _type = LIMIT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:146:6: ( L_ I_ M_ I_ T_ )
			// MuleDsql.g:146:8: L_ I_ M_ I_ T_
			{
			mL_(); 

			mI_(); 

			mM_(); 

			mI_(); 

			mT_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LIMIT"

	// $ANTLR start "OFFSET"
	public final void mOFFSET() throws RecognitionException {
		try {
			int _type = OFFSET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:147:7: ( O_ F_ F_ S_ E_ T_ )
			// MuleDsql.g:147:9: O_ F_ F_ S_ E_ T_
			{
			mO_(); 

			mF_(); 

			mF_(); 

			mS_(); 

			mE_(); 

			mT_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OFFSET"

	// $ANTLR start "AND"
	public final void mAND() throws RecognitionException {
		try {
			int _type = AND;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:148:4: ( A_ N_ D_ )
			// MuleDsql.g:148:6: A_ N_ D_
			{
			mA_(); 

			mN_(); 

			mD_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AND"

	// $ANTLR start "OR"
	public final void mOR() throws RecognitionException {
		try {
			int _type = OR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:149:3: ( O_ R_ )
			// MuleDsql.g:149:5: O_ R_
			{
			mO_(); 

			mR_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OR"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:150:4: ( N_ O_ T_ )
			// MuleDsql.g:150:6: N_ O_ T_
			{
			mN_(); 

			mO_(); 

			mT_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "OPENING_PARENTHESIS"
	public final void mOPENING_PARENTHESIS() throws RecognitionException {
		try {
			int _type = OPENING_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:151:20: ( '(' )
			// MuleDsql.g:151:22: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPENING_PARENTHESIS"

	// $ANTLR start "CLOSING_PARENTHESIS"
	public final void mCLOSING_PARENTHESIS() throws RecognitionException {
		try {
			int _type = CLOSING_PARENTHESIS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:152:20: ( ')' )
			// MuleDsql.g:152:22: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "CLOSING_PARENTHESIS"

	// $ANTLR start "COMPARATOR"
	public final void mCOMPARATOR() throws RecognitionException {
		try {
			int _type = COMPARATOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:154:11: ( L_ I_ K_ E_ )
			// MuleDsql.g:154:13: L_ I_ K_ E_
			{
			mL_(); 

			mI_(); 

			mK_(); 

			mE_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMPARATOR"

	// $ANTLR start "DATE_TIME_LITERAL"
	public final void mDATE_TIME_LITERAL() throws RecognitionException {
		try {
			int _type = DATE_TIME_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:156:18: ( TWO_DIGIT TWO_DIGIT '-' TWO_DIGIT '-' TWO_DIGIT 'T' TWO_DIGIT ':' TWO_DIGIT ':' TWO_DIGIT ( '.' TWO_DIGIT ( '0' .. '9' ) )? TIME_ZONE )
			// MuleDsql.g:156:20: TWO_DIGIT TWO_DIGIT '-' TWO_DIGIT '-' TWO_DIGIT 'T' TWO_DIGIT ':' TWO_DIGIT ':' TWO_DIGIT ( '.' TWO_DIGIT ( '0' .. '9' ) )? TIME_ZONE
			{
			mTWO_DIGIT(); 

			mTWO_DIGIT(); 

			match('-'); 
			mTWO_DIGIT(); 

			match('-'); 
			mTWO_DIGIT(); 

			match('T'); 
			mTWO_DIGIT(); 

			match(':'); 
			mTWO_DIGIT(); 

			match(':'); 
			mTWO_DIGIT(); 

			// MuleDsql.g:156:99: ( '.' TWO_DIGIT ( '0' .. '9' ) )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0=='.') ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// MuleDsql.g:156:100: '.' TWO_DIGIT ( '0' .. '9' )
					{
					match('.'); 
					mTWO_DIGIT(); 

					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

			}

			mTIME_ZONE(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DATE_TIME_LITERAL"

	// $ANTLR start "DATE_LITERAL"
	public final void mDATE_LITERAL() throws RecognitionException {
		try {
			int _type = DATE_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:158:13: ( TWO_DIGIT TWO_DIGIT '-' TWO_DIGIT '-' TWO_DIGIT )
			// MuleDsql.g:158:15: TWO_DIGIT TWO_DIGIT '-' TWO_DIGIT '-' TWO_DIGIT
			{
			mTWO_DIGIT(); 

			mTWO_DIGIT(); 

			match('-'); 
			mTWO_DIGIT(); 

			match('-'); 
			mTWO_DIGIT(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DATE_LITERAL"

	// $ANTLR start "TIME_ZONE"
	public final void mTIME_ZONE() throws RecognitionException {
		try {
			// MuleDsql.g:162:10: ( ( ( '+' | '-' ) TWO_DIGIT ':' TWO_DIGIT | 'Z' ) )
			// MuleDsql.g:162:12: ( ( '+' | '-' ) TWO_DIGIT ':' TWO_DIGIT | 'Z' )
			{
			// MuleDsql.g:162:12: ( ( '+' | '-' ) TWO_DIGIT ':' TWO_DIGIT | 'Z' )
			int alt4=2;
			int LA4_0 = input.LA(1);
			if ( (LA4_0=='+'||LA4_0=='-') ) {
				alt4=1;
			}
			else if ( (LA4_0=='Z') ) {
				alt4=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 4, 0, input);
				throw nvae;
			}

			switch (alt4) {
				case 1 :
					// MuleDsql.g:162:13: ( '+' | '-' ) TWO_DIGIT ':' TWO_DIGIT
					{
					if ( input.LA(1)=='+'||input.LA(1)=='-' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					mTWO_DIGIT(); 

					match(':'); 
					mTWO_DIGIT(); 

					}
					break;
				case 2 :
					// MuleDsql.g:162:47: 'Z'
					{
					match('Z'); 
					}
					break;

			}

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TIME_ZONE"

	// $ANTLR start "NULL_LITERAL"
	public final void mNULL_LITERAL() throws RecognitionException {
		try {
			int _type = NULL_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:162:13: ( N_ U_ L_ L_ )
			// MuleDsql.g:162:15: N_ U_ L_ L_
			{
			mN_(); 

			mU_(); 

			mL_(); 

			mL_(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NULL_LITERAL"

	// $ANTLR start "TWO_DIGIT"
	public final void mTWO_DIGIT() throws RecognitionException {
		try {
			// MuleDsql.g:166:10: ( ( '0' .. '9' ) ( '0' .. '9' ) )
			// MuleDsql.g:166:12: ( '0' .. '9' ) ( '0' .. '9' )
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "TWO_DIGIT"

	// $ANTLR start "MULE_EXPRESSION"
	public final void mMULE_EXPRESSION() throws RecognitionException {
		try {
			int _type = MULE_EXPRESSION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:168:2: ( '#' NESTED_MULE_EXPRESSION )
			// MuleDsql.g:168:4: '#' NESTED_MULE_EXPRESSION
			{
			match('#'); 
			mNESTED_MULE_EXPRESSION(); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MULE_EXPRESSION"

	// $ANTLR start "NESTED_MULE_EXPRESSION"
	public final void mNESTED_MULE_EXPRESSION() throws RecognitionException {
		try {
			// MuleDsql.g:172:24: ( '[' ( options {greedy=false; k=2; } : NESTED_MULE_EXPRESSION | STRING_LITERAL | . )* ']' )
			// MuleDsql.g:173:2: '[' ( options {greedy=false; k=2; } : NESTED_MULE_EXPRESSION | STRING_LITERAL | . )* ']'
			{
			match('['); 
			// MuleDsql.g:174:2: ( options {greedy=false; k=2; } : NESTED_MULE_EXPRESSION | STRING_LITERAL | . )*
			loop5:
			while (true) {
				int alt5=4;
				int LA5_0 = input.LA(1);
				if ( (LA5_0==']') ) {
					alt5=4;
				}
				else if ( (LA5_0=='[') ) {
					alt5=1;
				}
				else if ( (LA5_0=='\'') ) {
					int LA5_3 = input.LA(2);
					if ( (LA5_3=='\\') ) {
						alt5=2;
					}
					else if ( (LA5_3==']') ) {
						alt5=2;
					}
					else if ( (LA5_3=='\'') ) {
						alt5=2;
					}
					else if ( (LA5_3=='[') ) {
						alt5=2;
					}
					else if ( (LA5_3=='\"') ) {
						alt5=2;
					}
					else if ( ((LA5_3 >= '\u0000' && LA5_3 <= '!')||(LA5_3 >= '#' && LA5_3 <= '&')||(LA5_3 >= '(' && LA5_3 <= 'Z')||(LA5_3 >= '^' && LA5_3 <= '\uFFFF')) ) {
						alt5=2;
					}

				}
				else if ( (LA5_0=='\"') ) {
					int LA5_4 = input.LA(2);
					if ( (LA5_4=='\\') ) {
						alt5=2;
					}
					else if ( (LA5_4==']') ) {
						alt5=2;
					}
					else if ( (LA5_4=='\"') ) {
						alt5=2;
					}
					else if ( (LA5_4=='[') ) {
						alt5=2;
					}
					else if ( (LA5_4=='\'') ) {
						alt5=2;
					}
					else if ( ((LA5_4 >= '\u0000' && LA5_4 <= '!')||(LA5_4 >= '#' && LA5_4 <= '&')||(LA5_4 >= '(' && LA5_4 <= 'Z')||(LA5_4 >= '^' && LA5_4 <= '\uFFFF')) ) {
						alt5=2;
					}

				}
				else if ( ((LA5_0 >= '\u0000' && LA5_0 <= '!')||(LA5_0 >= '#' && LA5_0 <= '&')||(LA5_0 >= '(' && LA5_0 <= 'Z')||LA5_0=='\\'||(LA5_0 >= '^' && LA5_0 <= '\uFFFF')) ) {
					alt5=3;
				}

				switch (alt5) {
				case 1 :
					// MuleDsql.g:175:4: NESTED_MULE_EXPRESSION
					{
					mNESTED_MULE_EXPRESSION(); 

					}
					break;
				case 2 :
					// MuleDsql.g:176:4: STRING_LITERAL
					{
					mSTRING_LITERAL(); 

					}
					break;
				case 3 :
					// MuleDsql.g:177:4: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop5;
				}
			}

			match(']'); 
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NESTED_MULE_EXPRESSION"

	// $ANTLR start "STRING_LITERAL"
	public final void mSTRING_LITERAL() throws RecognitionException {
		try {
			int _type = STRING_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:180:15: ( '\\'' ( ESCAPE_SEQUENCE |~ ( '\\\\' | '\\'' ) )* '\\'' | '\"' ( ESCAPE_SEQUENCE |~ ( '\\\\' | '\"' ) )* '\"' )
			int alt8=2;
			int LA8_0 = input.LA(1);
			if ( (LA8_0=='\'') ) {
				alt8=1;
			}
			else if ( (LA8_0=='\"') ) {
				alt8=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 8, 0, input);
				throw nvae;
			}

			switch (alt8) {
				case 1 :
					// MuleDsql.g:181:2: '\\'' ( ESCAPE_SEQUENCE |~ ( '\\\\' | '\\'' ) )* '\\''
					{
					match('\''); 
					// MuleDsql.g:181:7: ( ESCAPE_SEQUENCE |~ ( '\\\\' | '\\'' ) )*
					loop6:
					while (true) {
						int alt6=3;
						int LA6_0 = input.LA(1);
						if ( (LA6_0=='\\') ) {
							alt6=1;
						}
						else if ( ((LA6_0 >= '\u0000' && LA6_0 <= '&')||(LA6_0 >= '(' && LA6_0 <= '[')||(LA6_0 >= ']' && LA6_0 <= '\uFFFF')) ) {
							alt6=2;
						}

						switch (alt6) {
						case 1 :
							// MuleDsql.g:181:9: ESCAPE_SEQUENCE
							{
							mESCAPE_SEQUENCE(); 

							}
							break;
						case 2 :
							// MuleDsql.g:181:27: ~ ( '\\\\' | '\\'' )
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop6;
						}
					}

					match('\''); 
					}
					break;
				case 2 :
					// MuleDsql.g:182:4: '\"' ( ESCAPE_SEQUENCE |~ ( '\\\\' | '\"' ) )* '\"'
					{
					match('\"'); 
					// MuleDsql.g:182:8: ( ESCAPE_SEQUENCE |~ ( '\\\\' | '\"' ) )*
					loop7:
					while (true) {
						int alt7=3;
						int LA7_0 = input.LA(1);
						if ( (LA7_0=='\\') ) {
							alt7=1;
						}
						else if ( ((LA7_0 >= '\u0000' && LA7_0 <= '!')||(LA7_0 >= '#' && LA7_0 <= '[')||(LA7_0 >= ']' && LA7_0 <= '\uFFFF')) ) {
							alt7=2;
						}

						switch (alt7) {
						case 1 :
							// MuleDsql.g:182:10: ESCAPE_SEQUENCE
							{
							mESCAPE_SEQUENCE(); 

							}
							break;
						case 2 :
							// MuleDsql.g:182:28: ~ ( '\\\\' | '\"' )
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '[')||(input.LA(1) >= ']' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop7;
						}
					}

					match('\"'); 
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING_LITERAL"

	// $ANTLR start "BOOLEAN_LITERAL"
	public final void mBOOLEAN_LITERAL() throws RecognitionException {
		try {
			int _type = BOOLEAN_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:185:16: ( ( T_ R_ U_ E_ | F_ A_ L_ S_ E_ ) )
			// MuleDsql.g:185:18: ( T_ R_ U_ E_ | F_ A_ L_ S_ E_ )
			{
			// MuleDsql.g:185:18: ( T_ R_ U_ E_ | F_ A_ L_ S_ E_ )
			int alt9=2;
			int LA9_0 = input.LA(1);
			if ( (LA9_0=='T'||LA9_0=='t') ) {
				alt9=1;
			}
			else if ( (LA9_0=='F'||LA9_0=='f') ) {
				alt9=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 9, 0, input);
				throw nvae;
			}

			switch (alt9) {
				case 1 :
					// MuleDsql.g:185:19: T_ R_ U_ E_
					{
					mT_(); 

					mR_(); 

					mU_(); 

					mE_(); 

					}
					break;
				case 2 :
					// MuleDsql.g:185:33: F_ A_ L_ S_ E_
					{
					mF_(); 

					mA_(); 

					mL_(); 

					mS_(); 

					mE_(); 

					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "BOOLEAN_LITERAL"

	// $ANTLR start "ESCAPE_SEQUENCE"
	public final void mESCAPE_SEQUENCE() throws RecognitionException {
		try {
			// MuleDsql.g:189:16: ( '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' ) | UNICODE_ESCAPE )
			int alt10=2;
			int LA10_0 = input.LA(1);
			if ( (LA10_0=='\\') ) {
				int LA10_1 = input.LA(2);
				if ( (LA10_1=='\"'||LA10_1=='\''||LA10_1=='\\'||LA10_1=='b'||LA10_1=='f'||LA10_1=='n'||LA10_1=='r'||LA10_1=='t') ) {
					alt10=1;
				}
				else if ( (LA10_1=='u') ) {
					alt10=2;
				}

				else {
					int nvaeMark = input.mark();
					try {
						input.consume();
						NoViableAltException nvae =
							new NoViableAltException("", 10, 1, input);
						throw nvae;
					} finally {
						input.rewind(nvaeMark);
					}
				}

			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 10, 0, input);
				throw nvae;
			}

			switch (alt10) {
				case 1 :
					// MuleDsql.g:190:2: '\\\\' ( 'b' | 't' | 'n' | 'f' | 'r' | '\\\"' | '\\'' | '\\\\' )
					{
					match('\\'); 
					if ( input.LA(1)=='\"'||input.LA(1)=='\''||input.LA(1)=='\\'||input.LA(1)=='b'||input.LA(1)=='f'||input.LA(1)=='n'||input.LA(1)=='r'||input.LA(1)=='t' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;
				case 2 :
					// MuleDsql.g:191:9: UNICODE_ESCAPE
					{
					mUNICODE_ESCAPE(); 

					}
					break;

			}
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ESCAPE_SEQUENCE"

	// $ANTLR start "UNICODE_ESCAPE"
	public final void mUNICODE_ESCAPE() throws RecognitionException {
		try {
			// MuleDsql.g:195:15: ( '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT )
			// MuleDsql.g:196:2: '\\\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
			{
			match('\\'); 
			match('u'); 
			mHEX_DIGIT(); 

			mHEX_DIGIT(); 

			mHEX_DIGIT(); 

			mHEX_DIGIT(); 

			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "UNICODE_ESCAPE"

	// $ANTLR start "HEX_DIGIT"
	public final void mHEX_DIGIT() throws RecognitionException {
		try {
			// MuleDsql.g:199:10: ( ( '0' .. '9' | 'a' .. 'f' | 'A' .. 'F' ) )
			// MuleDsql.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'F')||(input.LA(1) >= 'a' && input.LA(1) <= 'f') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "HEX_DIGIT"

	// $ANTLR start "INTEGER_LITERAL"
	public final void mINTEGER_LITERAL() throws RecognitionException {
		try {
			int _type = INTEGER_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:201:16: ( ( '0' .. '9' )* )
			// MuleDsql.g:202:2: ( '0' .. '9' )*
			{
			// MuleDsql.g:202:2: ( '0' .. '9' )*
			loop11:
			while (true) {
				int alt11=2;
				int LA11_0 = input.LA(1);
				if ( ((LA11_0 >= '0' && LA11_0 <= '9')) ) {
					alt11=1;
				}

				switch (alt11) {
				case 1 :
					// MuleDsql.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop11;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INTEGER_LITERAL"

	// $ANTLR start "DOUBLE_LITERAL"
	public final void mDOUBLE_LITERAL() throws RecognitionException {
		try {
			int _type = DOUBLE_LITERAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:203:15: ( ( '0' .. '9' | '.' )* )
			// MuleDsql.g:204:2: ( '0' .. '9' | '.' )*
			{
			// MuleDsql.g:204:2: ( '0' .. '9' | '.' )*
			loop12:
			while (true) {
				int alt12=2;
				int LA12_0 = input.LA(1);
				if ( (LA12_0=='.'||(LA12_0 >= '0' && LA12_0 <= '9')) ) {
					alt12=1;
				}

				switch (alt12) {
				case 1 :
					// MuleDsql.g:
					{
					if ( input.LA(1)=='.'||(input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop12;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOUBLE_LITERAL"

	// $ANTLR start "IDENT"
	public final void mIDENT() throws RecognitionException {
		try {
			int _type = IDENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:206:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '-' | '_' | '.' )+ | '[' (~ ( ']' ) )+ ']' )
			int alt15=2;
			int LA15_0 = input.LA(1);
			if ( ((LA15_0 >= '-' && LA15_0 <= '.')||(LA15_0 >= '0' && LA15_0 <= '9')||(LA15_0 >= 'A' && LA15_0 <= 'Z')||LA15_0=='_'||(LA15_0 >= 'a' && LA15_0 <= 'z')) ) {
				alt15=1;
			}
			else if ( (LA15_0=='[') ) {
				alt15=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 15, 0, input);
				throw nvae;
			}

			switch (alt15) {
				case 1 :
					// MuleDsql.g:206:9: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '-' | '_' | '.' )+
					{
					// MuleDsql.g:206:9: ( 'a' .. 'z' | 'A' .. 'Z' | '0' .. '9' | '-' | '_' | '.' )+
					int cnt13=0;
					loop13:
					while (true) {
						int alt13=2;
						int LA13_0 = input.LA(1);
						if ( ((LA13_0 >= '-' && LA13_0 <= '.')||(LA13_0 >= '0' && LA13_0 <= '9')||(LA13_0 >= 'A' && LA13_0 <= 'Z')||LA13_0=='_'||(LA13_0 >= 'a' && LA13_0 <= 'z')) ) {
							alt13=1;
						}

						switch (alt13) {
						case 1 :
							// MuleDsql.g:
							{
							if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt13 >= 1 ) break loop13;
							EarlyExitException eee = new EarlyExitException(13, input);
							throw eee;
						}
						cnt13++;
					}

					}
					break;
				case 2 :
					// MuleDsql.g:207:7: '[' (~ ( ']' ) )+ ']'
					{
					match('['); 
					// MuleDsql.g:207:11: (~ ( ']' ) )+
					int cnt14=0;
					loop14:
					while (true) {
						int alt14=2;
						int LA14_0 = input.LA(1);
						if ( ((LA14_0 >= '\u0000' && LA14_0 <= '\\')||(LA14_0 >= '^' && LA14_0 <= '\uFFFF')) ) {
							alt14=1;
						}

						switch (alt14) {
						case 1 :
							// MuleDsql.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\\')||(input.LA(1) >= '^' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							if ( cnt14 >= 1 ) break loop14;
							EarlyExitException eee = new EarlyExitException(14, input);
							throw eee;
						}
						cnt14++;
					}

					match(']'); 
					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "IDENT"

	// $ANTLR start "ASTERIX"
	public final void mASTERIX() throws RecognitionException {
		try {
			int _type = ASTERIX;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:208:9: ( '*' )
			// MuleDsql.g:208:11: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ASTERIX"

	// $ANTLR start "OPERATOR"
	public final void mOPERATOR() throws RecognitionException {
		try {
			int _type = OPERATOR;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:209:10: ( '=' | '>' | '<' | '<=' | '<>' | '>=' )
			int alt16=6;
			switch ( input.LA(1) ) {
			case '=':
				{
				alt16=1;
				}
				break;
			case '>':
				{
				int LA16_2 = input.LA(2);
				if ( (LA16_2=='=') ) {
					alt16=6;
				}

				else {
					alt16=2;
				}

				}
				break;
			case '<':
				{
				switch ( input.LA(2) ) {
				case '=':
					{
					alt16=4;
					}
					break;
				case '>':
					{
					alt16=5;
					}
					break;
				default:
					alt16=3;
				}
				}
				break;
			default:
				NoViableAltException nvae =
					new NoViableAltException("", 16, 0, input);
				throw nvae;
			}
			switch (alt16) {
				case 1 :
					// MuleDsql.g:209:12: '='
					{
					match('='); 
					}
					break;
				case 2 :
					// MuleDsql.g:209:16: '>'
					{
					match('>'); 
					}
					break;
				case 3 :
					// MuleDsql.g:209:20: '<'
					{
					match('<'); 
					}
					break;
				case 4 :
					// MuleDsql.g:209:24: '<='
					{
					match("<="); 

					}
					break;
				case 5 :
					// MuleDsql.g:209:29: '<>'
					{
					match("<>"); 

					}
					break;
				case 6 :
					// MuleDsql.g:209:34: '>='
					{
					match(">="); 

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "OPERATOR"

	// $ANTLR start "COMMENT"
	public final void mCOMMENT() throws RecognitionException {
		try {
			int _type = COMMENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:211:10: ( ( ( '--' ) (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' ) )
			// MuleDsql.g:211:12: ( ( '--' ) (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
			{
			// MuleDsql.g:211:12: ( ( '--' ) (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n' )
			// MuleDsql.g:211:14: ( '--' ) (~ ( '\\n' | '\\r' ) )* ( '\\r' )? '\\n'
			{
			// MuleDsql.g:211:14: ( '--' )
			// MuleDsql.g:211:15: '--'
			{
			match("--"); 

			}

			// MuleDsql.g:211:21: (~ ( '\\n' | '\\r' ) )*
			loop17:
			while (true) {
				int alt17=2;
				int LA17_0 = input.LA(1);
				if ( ((LA17_0 >= '\u0000' && LA17_0 <= '\t')||(LA17_0 >= '\u000B' && LA17_0 <= '\f')||(LA17_0 >= '\u000E' && LA17_0 <= '\uFFFF')) ) {
					alt17=1;
				}

				switch (alt17) {
				case 1 :
					// MuleDsql.g:
					{
					if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '\t')||(input.LA(1) >= '\u000B' && input.LA(1) <= '\f')||(input.LA(1) >= '\u000E' && input.LA(1) <= '\uFFFF') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop17;
				}
			}

			// MuleDsql.g:211:35: ( '\\r' )?
			int alt18=2;
			int LA18_0 = input.LA(1);
			if ( (LA18_0=='\r') ) {
				alt18=1;
			}
			switch (alt18) {
				case 1 :
					// MuleDsql.g:211:35: '\\r'
					{
					match('\r'); 
					}
					break;

			}

			match('\n'); 
			}

			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMENT"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// MuleDsql.g:212:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+ )
			// MuleDsql.g:212:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
			{
			// MuleDsql.g:212:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\f' )+
			int cnt19=0;
			loop19:
			while (true) {
				int alt19=2;
				int LA19_0 = input.LA(1);
				if ( ((LA19_0 >= '\t' && LA19_0 <= '\n')||(LA19_0 >= '\f' && LA19_0 <= '\r')||LA19_0==' ') ) {
					alt19=1;
				}

				switch (alt19) {
				case 1 :
					// MuleDsql.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt19 >= 1 ) break loop19;
					EarlyExitException eee = new EarlyExitException(19, input);
					throw eee;
				}
				cnt19++;
			}

			_channel=HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	// $ANTLR start "A_"
	public final void mA_() throws RecognitionException {
		try {
			// MuleDsql.g:214:12: ( ( 'a' | 'A' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='A'||input.LA(1)=='a' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "A_"

	// $ANTLR start "B_"
	public final void mB_() throws RecognitionException {
		try {
			// MuleDsql.g:215:12: ( ( 'b' | 'B' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='B'||input.LA(1)=='b' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "B_"

	// $ANTLR start "C_"
	public final void mC_() throws RecognitionException {
		try {
			// MuleDsql.g:216:12: ( ( 'c' | 'C' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='C'||input.LA(1)=='c' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "C_"

	// $ANTLR start "D_"
	public final void mD_() throws RecognitionException {
		try {
			// MuleDsql.g:217:12: ( ( 'd' | 'D' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "D_"

	// $ANTLR start "E_"
	public final void mE_() throws RecognitionException {
		try {
			// MuleDsql.g:218:12: ( ( 'e' | 'E' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='E'||input.LA(1)=='e' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "E_"

	// $ANTLR start "F_"
	public final void mF_() throws RecognitionException {
		try {
			// MuleDsql.g:219:12: ( ( 'f' | 'F' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='F'||input.LA(1)=='f' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "F_"

	// $ANTLR start "G_"
	public final void mG_() throws RecognitionException {
		try {
			// MuleDsql.g:220:12: ( ( 'g' | 'G' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='G'||input.LA(1)=='g' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "G_"

	// $ANTLR start "H_"
	public final void mH_() throws RecognitionException {
		try {
			// MuleDsql.g:221:12: ( ( 'h' | 'H' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='H'||input.LA(1)=='h' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "H_"

	// $ANTLR start "I_"
	public final void mI_() throws RecognitionException {
		try {
			// MuleDsql.g:222:12: ( ( 'i' | 'I' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='I'||input.LA(1)=='i' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "I_"

	// $ANTLR start "J_"
	public final void mJ_() throws RecognitionException {
		try {
			// MuleDsql.g:223:12: ( ( 'j' | 'J' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='J'||input.LA(1)=='j' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "J_"

	// $ANTLR start "K_"
	public final void mK_() throws RecognitionException {
		try {
			// MuleDsql.g:224:12: ( ( 'k' | 'K' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='K'||input.LA(1)=='k' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_"

	// $ANTLR start "L_"
	public final void mL_() throws RecognitionException {
		try {
			// MuleDsql.g:225:12: ( ( 'l' | 'L' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='L'||input.LA(1)=='l' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "L_"

	// $ANTLR start "M_"
	public final void mM_() throws RecognitionException {
		try {
			// MuleDsql.g:226:12: ( ( 'm' | 'M' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='M'||input.LA(1)=='m' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "M_"

	// $ANTLR start "N_"
	public final void mN_() throws RecognitionException {
		try {
			// MuleDsql.g:227:12: ( ( 'n' | 'N' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='N'||input.LA(1)=='n' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "N_"

	// $ANTLR start "O_"
	public final void mO_() throws RecognitionException {
		try {
			// MuleDsql.g:228:12: ( ( 'o' | 'O' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='O'||input.LA(1)=='o' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "O_"

	// $ANTLR start "P_"
	public final void mP_() throws RecognitionException {
		try {
			// MuleDsql.g:229:12: ( ( 'p' | 'P' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='P'||input.LA(1)=='p' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "P_"

	// $ANTLR start "Q_"
	public final void mQ_() throws RecognitionException {
		try {
			// MuleDsql.g:230:12: ( ( 'q' | 'Q' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='Q'||input.LA(1)=='q' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Q_"

	// $ANTLR start "R_"
	public final void mR_() throws RecognitionException {
		try {
			// MuleDsql.g:231:12: ( ( 'r' | 'R' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='R'||input.LA(1)=='r' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "R_"

	// $ANTLR start "S_"
	public final void mS_() throws RecognitionException {
		try {
			// MuleDsql.g:232:12: ( ( 's' | 'S' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='S'||input.LA(1)=='s' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "S_"

	// $ANTLR start "T_"
	public final void mT_() throws RecognitionException {
		try {
			// MuleDsql.g:233:12: ( ( 't' | 'T' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='T'||input.LA(1)=='t' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T_"

	// $ANTLR start "U_"
	public final void mU_() throws RecognitionException {
		try {
			// MuleDsql.g:234:12: ( ( 'u' | 'U' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='U'||input.LA(1)=='u' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "U_"

	// $ANTLR start "V_"
	public final void mV_() throws RecognitionException {
		try {
			// MuleDsql.g:235:12: ( ( 'v' | 'V' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='V'||input.LA(1)=='v' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "V_"

	// $ANTLR start "W_"
	public final void mW_() throws RecognitionException {
		try {
			// MuleDsql.g:236:12: ( ( 'w' | 'W' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='W'||input.LA(1)=='w' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "W_"

	// $ANTLR start "X_"
	public final void mX_() throws RecognitionException {
		try {
			// MuleDsql.g:237:12: ( ( 'x' | 'X' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='X'||input.LA(1)=='x' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "X_"

	// $ANTLR start "Y_"
	public final void mY_() throws RecognitionException {
		try {
			// MuleDsql.g:238:12: ( ( 'y' | 'Y' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='Y'||input.LA(1)=='y' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Y_"

	// $ANTLR start "Z_"
	public final void mZ_() throws RecognitionException {
		try {
			// MuleDsql.g:239:12: ( ( 'z' | 'Z' ) )
			// MuleDsql.g:
			{
			if ( input.LA(1)=='Z'||input.LA(1)=='z' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "Z_"

	@Override
	public void mTokens() throws RecognitionException {
		// MuleDsql.g:1:8: ( T__64 | ASC | DESC | SELECT | FROM | WHERE | ORDER | BY | LIMIT | OFFSET | AND | OR | NOT | OPENING_PARENTHESIS | CLOSING_PARENTHESIS | COMPARATOR | DATE_TIME_LITERAL | DATE_LITERAL | NULL_LITERAL | MULE_EXPRESSION | STRING_LITERAL | BOOLEAN_LITERAL | INTEGER_LITERAL | DOUBLE_LITERAL | IDENT | ASTERIX | OPERATOR | COMMENT | WS )
		int alt20=29;
		alt20 = dfa20.predict(input);
		switch (alt20) {
			case 1 :
				// MuleDsql.g:1:10: T__64
				{
				mT__64(); 

				}
				break;
			case 2 :
				// MuleDsql.g:1:16: ASC
				{
				mASC(); 

				}
				break;
			case 3 :
				// MuleDsql.g:1:20: DESC
				{
				mDESC(); 

				}
				break;
			case 4 :
				// MuleDsql.g:1:25: SELECT
				{
				mSELECT(); 

				}
				break;
			case 5 :
				// MuleDsql.g:1:32: FROM
				{
				mFROM(); 

				}
				break;
			case 6 :
				// MuleDsql.g:1:37: WHERE
				{
				mWHERE(); 

				}
				break;
			case 7 :
				// MuleDsql.g:1:43: ORDER
				{
				mORDER(); 

				}
				break;
			case 8 :
				// MuleDsql.g:1:49: BY
				{
				mBY(); 

				}
				break;
			case 9 :
				// MuleDsql.g:1:52: LIMIT
				{
				mLIMIT(); 

				}
				break;
			case 10 :
				// MuleDsql.g:1:58: OFFSET
				{
				mOFFSET(); 

				}
				break;
			case 11 :
				// MuleDsql.g:1:65: AND
				{
				mAND(); 

				}
				break;
			case 12 :
				// MuleDsql.g:1:69: OR
				{
				mOR(); 

				}
				break;
			case 13 :
				// MuleDsql.g:1:72: NOT
				{
				mNOT(); 

				}
				break;
			case 14 :
				// MuleDsql.g:1:76: OPENING_PARENTHESIS
				{
				mOPENING_PARENTHESIS(); 

				}
				break;
			case 15 :
				// MuleDsql.g:1:96: CLOSING_PARENTHESIS
				{
				mCLOSING_PARENTHESIS(); 

				}
				break;
			case 16 :
				// MuleDsql.g:1:116: COMPARATOR
				{
				mCOMPARATOR(); 

				}
				break;
			case 17 :
				// MuleDsql.g:1:127: DATE_TIME_LITERAL
				{
				mDATE_TIME_LITERAL(); 

				}
				break;
			case 18 :
				// MuleDsql.g:1:145: DATE_LITERAL
				{
				mDATE_LITERAL(); 

				}
				break;
			case 19 :
				// MuleDsql.g:1:158: NULL_LITERAL
				{
				mNULL_LITERAL(); 

				}
				break;
			case 20 :
				// MuleDsql.g:1:171: MULE_EXPRESSION
				{
				mMULE_EXPRESSION(); 

				}
				break;
			case 21 :
				// MuleDsql.g:1:187: STRING_LITERAL
				{
				mSTRING_LITERAL(); 

				}
				break;
			case 22 :
				// MuleDsql.g:1:202: BOOLEAN_LITERAL
				{
				mBOOLEAN_LITERAL(); 

				}
				break;
			case 23 :
				// MuleDsql.g:1:218: INTEGER_LITERAL
				{
				mINTEGER_LITERAL(); 

				}
				break;
			case 24 :
				// MuleDsql.g:1:234: DOUBLE_LITERAL
				{
				mDOUBLE_LITERAL(); 

				}
				break;
			case 25 :
				// MuleDsql.g:1:249: IDENT
				{
				mIDENT(); 

				}
				break;
			case 26 :
				// MuleDsql.g:1:255: ASTERIX
				{
				mASTERIX(); 

				}
				break;
			case 27 :
				// MuleDsql.g:1:263: OPERATOR
				{
				mOPERATOR(); 

				}
				break;
			case 28 :
				// MuleDsql.g:1:272: COMMENT
				{
				mCOMMENT(); 

				}
				break;
			case 29 :
				// MuleDsql.g:1:280: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA20 dfa20 = new DFA20(this);
	static final String DFA20_eotS =
		"\1\21\1\uffff\11\24\2\uffff\1\21\2\uffff\1\24\1\uffff\1\47\1\24\4\uffff"+
		"\7\24\1\60\1\24\1\63\3\24\1\21\1\24\1\uffff\1\24\1\74\1\76\5\24\1\uffff"+
		"\2\24\1\uffff\2\24\1\110\1\24\1\21\2\24\2\uffff\1\24\1\uffff\1\115\1\24"+
		"\1\120\5\24\1\126\1\uffff\1\127\1\21\1\132\1\24\1\uffff\2\24\1\uffff\1"+
		"\132\1\136\1\137\1\24\1\141\2\uffff\1\24\1\21\1\uffff\2\24\1\145\2\uffff"+
		"\1\146\1\uffff\3\24\2\uffff\4\24\1\74\2\24\1\115\1\160\1\uffff\3\24\1"+
		"\uffff";
	static final String DFA20_eofS =
		"\165\uffff";
	static final String DFA20_minS =
		"\1\11\1\uffff\1\116\2\105\1\101\1\110\1\106\1\131\1\111\1\117\2\uffff"+
		"\1\55\2\uffff\1\122\1\uffff\2\55\4\uffff\1\103\1\104\1\123\1\114\1\117"+
		"\1\114\1\105\1\55\1\106\1\55\1\113\1\124\1\114\1\55\1\125\1\uffff\1\0"+
		"\2\55\1\103\1\105\1\115\1\123\1\122\1\uffff\1\105\1\123\1\uffff\1\111"+
		"\1\105\1\55\1\114\1\55\1\105\1\0\2\uffff\1\116\1\uffff\1\55\1\103\1\55"+
		"\2\105\1\122\1\105\1\124\1\55\1\uffff\3\55\1\104\1\uffff\1\116\1\124\1"+
		"\uffff\3\55\1\124\1\55\2\uffff\1\60\1\55\1\uffff\1\111\1\104\1\55\2\uffff"+
		"\1\55\1\uffff\1\60\1\116\1\111\2\uffff\1\55\1\107\1\116\1\60\1\55\1\107"+
		"\1\60\2\55\1\uffff\2\60\1\72\1\uffff";
	static final String DFA20_maxS =
		"\1\172\1\uffff\1\163\2\145\1\162\1\150\1\162\1\171\1\151\1\165\2\uffff"+
		"\1\172\2\uffff\1\162\1\uffff\1\172\1\55\4\uffff\1\143\1\144\1\163\1\154"+
		"\1\157\1\154\1\145\1\172\1\146\1\172\1\155\1\164\1\154\1\172\1\165\1\uffff"+
		"\1\uffff\2\172\1\143\1\145\1\155\1\163\1\162\1\uffff\1\145\1\163\1\uffff"+
		"\1\151\1\145\1\172\1\154\1\172\1\145\1\uffff\2\uffff\1\156\1\uffff\1\172"+
		"\1\143\1\172\2\145\1\162\1\145\1\164\1\172\1\uffff\3\172\1\144\1\uffff"+
		"\1\156\1\164\1\uffff\3\172\1\164\1\172\2\uffff\1\71\1\172\1\uffff\1\151"+
		"\1\144\1\172\2\uffff\1\172\1\uffff\1\71\1\156\1\151\2\uffff\1\55\1\147"+
		"\1\156\1\71\1\172\1\147\1\71\2\172\1\uffff\2\71\1\72\1\uffff";
	static final String DFA20_acceptS =
		"\1\uffff\1\1\11\uffff\1\16\1\17\1\uffff\1\24\1\25\1\uffff\1\27\2\uffff"+
		"\1\31\1\32\1\33\1\35\17\uffff\1\30\10\uffff\1\14\2\uffff\1\10\7\uffff"+
		"\1\34\1\2\1\uffff\1\13\11\uffff\1\15\4\uffff\1\3\2\uffff\1\5\5\uffff\1"+
		"\20\1\23\2\uffff\1\26\3\uffff\1\6\1\7\1\uffff\1\11\3\uffff\1\4\1\12\11"+
		"\uffff\1\22\3\uffff\1\21";
	static final String DFA20_specialS =
		"\50\uffff\1\1\21\uffff\1\0\72\uffff}>";
	static final String[] DFA20_transitionS = {
			"\2\27\1\uffff\2\27\22\uffff\1\27\1\uffff\1\17\1\16\3\uffff\1\17\1\13"+
			"\1\14\1\25\1\uffff\1\1\1\23\1\22\1\uffff\12\15\2\uffff\3\26\2\uffff\1"+
			"\2\1\10\1\24\1\3\1\24\1\5\5\24\1\11\1\24\1\12\1\7\3\24\1\4\1\20\2\24"+
			"\1\6\4\24\3\uffff\1\24\1\uffff\1\2\1\10\1\24\1\3\1\24\1\5\5\24\1\11\1"+
			"\24\1\12\1\7\3\24\1\4\1\20\2\24\1\6\3\24",
			"",
			"\1\31\4\uffff\1\30\32\uffff\1\31\4\uffff\1\30",
			"\1\32\37\uffff\1\32",
			"\1\33\37\uffff\1\33",
			"\1\35\20\uffff\1\34\16\uffff\1\35\20\uffff\1\34",
			"\1\36\37\uffff\1\36",
			"\1\40\13\uffff\1\37\23\uffff\1\40\13\uffff\1\37",
			"\1\41\37\uffff\1\41",
			"\1\42\37\uffff\1\42",
			"\1\43\5\uffff\1\44\31\uffff\1\43\5\uffff\1\44",
			"",
			"",
			"\1\24\1\22\1\uffff\12\45\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"",
			"\1\46\37\uffff\1\46",
			"",
			"\1\24\1\22\1\uffff\12\22\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\50",
			"",
			"",
			"",
			"",
			"\1\51\37\uffff\1\51",
			"\1\52\37\uffff\1\52",
			"\1\53\37\uffff\1\53",
			"\1\54\37\uffff\1\54",
			"\1\55\37\uffff\1\55",
			"\1\56\37\uffff\1\56",
			"\1\57\37\uffff\1\57",
			"\2\24\1\uffff\12\24\7\uffff\3\24\1\61\26\24\4\uffff\1\24\1\uffff\3\24"+
			"\1\61\26\24",
			"\1\62\37\uffff\1\62",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\65\1\uffff\1\64\35\uffff\1\65\1\uffff\1\64",
			"\1\66\37\uffff\1\66",
			"\1\67\37\uffff\1\67",
			"\1\24\1\22\1\uffff\12\70\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\71\37\uffff\1\71",
			"",
			"\55\73\2\72\1\73\12\72\7\73\32\72\4\73\1\72\1\73\32\72\uff85\73",
			"\2\24\1\uffff\12\24\7\uffff\4\24\1\75\25\24\4\uffff\1\24\1\uffff\4\24"+
			"\1\75\25\24",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\77\37\uffff\1\77",
			"\1\100\37\uffff\1\100",
			"\1\101\37\uffff\1\101",
			"\1\102\37\uffff\1\102",
			"\1\103\37\uffff\1\103",
			"",
			"\1\104\37\uffff\1\104",
			"\1\105\37\uffff\1\105",
			"",
			"\1\106\37\uffff\1\106",
			"\1\107\37\uffff\1\107",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\111\37\uffff\1\111",
			"\1\24\1\22\1\uffff\12\112\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\113\37\uffff\1\113",
			"\55\73\2\72\1\73\12\72\7\73\32\72\4\73\1\72\1\73\32\72\uff85\73",
			"",
			"",
			"\1\114\37\uffff\1\114",
			"",
			"\2\24\1\uffff\12\24\7\uffff\4\24\1\116\25\24\4\uffff\1\24\1\uffff\4"+
			"\24\1\116\25\24",
			"\1\117\37\uffff\1\117",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\121\37\uffff\1\121",
			"\1\122\37\uffff\1\122",
			"\1\123\37\uffff\1\123",
			"\1\124\37\uffff\1\124",
			"\1\125\37\uffff\1\125",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\130\1\22\1\uffff\12\131\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\133\37\uffff\1\133",
			"",
			"\1\134\37\uffff\1\134",
			"\1\135\37\uffff\1\135",
			"",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\140\37\uffff\1\140",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"",
			"\12\142",
			"\1\24\1\22\1\uffff\12\131\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"\1\143\37\uffff\1\143",
			"\1\144\37\uffff\1\144",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"",
			"\12\147",
			"\1\150\37\uffff\1\150",
			"\1\151\37\uffff\1\151",
			"",
			"",
			"\1\152",
			"\1\153\37\uffff\1\153",
			"\1\154\37\uffff\1\154",
			"\12\155",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\1\156\37\uffff\1\156",
			"\12\157",
			"\2\24\1\uffff\12\24\7\uffff\32\24\4\uffff\1\24\1\uffff\32\24",
			"\2\24\1\uffff\12\24\7\uffff\23\24\1\161\6\24\4\uffff\1\24\1\uffff\32"+
			"\24",
			"",
			"\12\162",
			"\12\163",
			"\1\164",
			""
	};

	static final short[] DFA20_eot = DFA.unpackEncodedString(DFA20_eotS);
	static final short[] DFA20_eof = DFA.unpackEncodedString(DFA20_eofS);
	static final char[] DFA20_min = DFA.unpackEncodedStringToUnsignedChars(DFA20_minS);
	static final char[] DFA20_max = DFA.unpackEncodedStringToUnsignedChars(DFA20_maxS);
	static final short[] DFA20_accept = DFA.unpackEncodedString(DFA20_acceptS);
	static final short[] DFA20_special = DFA.unpackEncodedString(DFA20_specialS);
	static final short[][] DFA20_transition;

	static {
		int numStates = DFA20_transitionS.length;
		DFA20_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA20_transition[i] = DFA.unpackEncodedString(DFA20_transitionS[i]);
		}
	}

	protected class DFA20 extends DFA {

		public DFA20(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 20;
			this.eot = DFA20_eot;
			this.eof = DFA20_eof;
			this.min = DFA20_min;
			this.max = DFA20_max;
			this.accept = DFA20_accept;
			this.special = DFA20_special;
			this.transition = DFA20_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__64 | ASC | DESC | SELECT | FROM | WHERE | ORDER | BY | LIMIT | OFFSET | AND | OR | NOT | OPENING_PARENTHESIS | CLOSING_PARENTHESIS | COMPARATOR | DATE_TIME_LITERAL | DATE_LITERAL | NULL_LITERAL | MULE_EXPRESSION | STRING_LITERAL | BOOLEAN_LITERAL | INTEGER_LITERAL | DOUBLE_LITERAL | IDENT | ASTERIX | OPERATOR | COMMENT | WS );";
		}
		@Override
		public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
			IntStream input = _input;
			int _s = s;
			switch ( s ) {
					case 0 : 
						int LA20_58 = input.LA(1);
						s = -1;
						if ( ((LA20_58 >= '\u0000' && LA20_58 <= ',')||LA20_58=='/'||(LA20_58 >= ':' && LA20_58 <= '@')||(LA20_58 >= '[' && LA20_58 <= '^')||LA20_58=='`'||(LA20_58 >= '{' && LA20_58 <= '\uFFFF')) ) {s = 59;}
						else if ( ((LA20_58 >= '-' && LA20_58 <= '.')||(LA20_58 >= '0' && LA20_58 <= '9')||(LA20_58 >= 'A' && LA20_58 <= 'Z')||LA20_58=='_'||(LA20_58 >= 'a' && LA20_58 <= 'z')) ) {s = 58;}
						else s = 20;
						if ( s>=0 ) return s;
						break;

					case 1 : 
						int LA20_40 = input.LA(1);
						s = -1;
						if ( ((LA20_40 >= '-' && LA20_40 <= '.')||(LA20_40 >= '0' && LA20_40 <= '9')||(LA20_40 >= 'A' && LA20_40 <= 'Z')||LA20_40=='_'||(LA20_40 >= 'a' && LA20_40 <= 'z')) ) {s = 58;}
						else if ( ((LA20_40 >= '\u0000' && LA20_40 <= ',')||LA20_40=='/'||(LA20_40 >= ':' && LA20_40 <= '@')||(LA20_40 >= '[' && LA20_40 <= '^')||LA20_40=='`'||(LA20_40 >= '{' && LA20_40 <= '\uFFFF')) ) {s = 59;}
						else s = 20;
						if ( s>=0 ) return s;
						break;
			}
			NoViableAltException nvae =
				new NoViableAltException(getDescription(), 20, _s, input);
			error(nvae);
			throw nvae;
		}
	}

}
