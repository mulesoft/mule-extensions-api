/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.grammar;



import org.mule.runtime.extension.internal.MuleDsqlParser;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;

/**
 * A DSQL specific implementation of {@link TreeAdaptor} used to create a tree.
 *
 * @since 1.0
 */
public class DsqlTreeAdaptor extends CommonTreeAdaptor {

  @Override
  public Object create(Token payload) {
    // TODO: Check if there is a better way to do this.
    Object retVal;
    if (payload != null) {
      switch (payload.getType()) {
        case MuleDsqlParser.SELECT: {
          retVal = new SelectDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.FROM: {
          retVal = new FromDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.WHERE: {
          retVal = new ExpressionDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.AND: {
          retVal = new AndDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.OR: {
          retVal = new OrDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.NOT: {
          retVal = new NotDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.OPENING_PARENTHESIS: {
          retVal = new OpeningParenthesesDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.COMPARATOR:
        case MuleDsqlParser.OPERATOR: {
          retVal = new OperatorDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.ORDER: {
          retVal = new OrderByDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.ASC: {
          retVal = new DirectionDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.DESC: {
          retVal = new DirectionDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.LIMIT: {
          retVal = new LimitDsqlNode(payload);
          break;
        }
        case MuleDsqlParser.OFFSET: {
          retVal = new OffsetDsqlNode(payload);
          break;
        }
        default: {
          retVal = new BaseDsqlNode(payload);
        }
      }
    } else {
      retVal = new BaseDsqlNode(payload);
    }
    return retVal;
  }

  @Override
  public Object errorNode(TokenStream input, Token start, Token stop,
                          RecognitionException e) {
    return new DsqlErrorNode(input, start, stop, e);
  }

}
