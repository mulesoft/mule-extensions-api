/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.internal.loader;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.NOT_SUPPORTED;
import static org.mule.runtime.api.meta.model.parameter.ParameterGroupModel.DEFAULT_GROUP_NAME;
import static org.mule.runtime.extension.api.ExtensionConstants.OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION;
import static org.mule.runtime.extension.api.ExtensionConstants.TRANSACTIONAL_ACTION_PARAMETER_NAME;
import static org.mule.runtime.extension.api.annotation.param.display.Placement.ADVANCED_TAB;
import static org.mule.sdk.api.tx.OperationTransactionalAction.JOIN_IF_POSSIBLE;
import static org.mule.sdk.api.tx.SourceTransactionalAction.NONE;

import org.mule.metadata.api.ClassTypeLoader;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.meta.model.ModelProperty;
import org.mule.runtime.api.meta.model.declaration.fluent.ComponentDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ExtensionDeclarer;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterGroupDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclarer;
import org.mule.runtime.api.tx.TransactionType;
import org.mule.runtime.extension.api.declaration.type.ExtensionsTypeLoaderFactory;
import org.mule.runtime.extension.api.exception.IllegalModelDefinitionException;
import org.mule.runtime.extension.api.loader.DeclarationEnricher;
import org.mule.runtime.extension.api.loader.ExtensionLoadingContext;
import org.mule.runtime.extension.internal.loader.enricher.TransactionalDeclarationEnricher;
import org.mule.sdk.api.tx.OperationTransactionalAction;
import org.mule.sdk.api.tx.SourceTransactionalAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class TransactionalDeclarationEnricherTestCase {

  private static final String TRANSACTIONAL_OPERATION = "transactionalOperation";
  private static final String NOT_TRANSACTIONAL_OPERATION = "notConnectedOperation";
  private static final String TRANSACTIONAL_SOURCE = "transactionalSource";
  private final NullModelProperty nullModelProperty = new NullModelProperty();

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionLoadingContext extensionLoadingContext;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclarer extensionDeclarer;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private ExtensionDeclaration extensionDeclaration;

  private OperationDeclaration transactionalOperation;

  private OperationDeclaration notTransactionalOperation;

  private SourceDeclaration transactionalSource;

  private SourceDeclaration transactionalSourceWithTxParameter;

  private DeclarationEnricher enricher = new TransactionalDeclarationEnricher();
  private MetadataType operationTransactionalActionType;

  private MetadataType sourceTransactionalActionType;
  private ClassTypeLoader typeLoader = ExtensionsTypeLoaderFactory.getDefault().createTypeLoader();

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void before() throws Exception {
    operationTransactionalActionType = typeLoader.load(OperationTransactionalAction.class);
    sourceTransactionalActionType = typeLoader.load(SourceTransactionalAction.class);

    transactionalOperation = spy(new ExtensionDeclarer()
        .withOperation(TRANSACTIONAL_OPERATION)
        .transactional(true)
        .getDeclaration());

    notTransactionalOperation = spy(new ExtensionDeclarer()
        .withOperation(NOT_TRANSACTIONAL_OPERATION)
        .transactional(false)
        .getDeclaration());

    transactionalSource = spy(new ExtensionDeclarer()
        .withMessageSource(TRANSACTIONAL_SOURCE)
        .transactional(true)
        .getDeclaration());

    SourceDeclarer transactional = new ExtensionDeclarer()
        .withMessageSource(TRANSACTIONAL_SOURCE)
        .transactional(true);

    transactional
        .onDefaultParameterGroup()
        .withRequiredParameter(TRANSACTIONAL_ACTION_PARAMETER_NAME)
        .withModelProperty(nullModelProperty)
        .ofType(sourceTransactionalActionType);

    transactionalSourceWithTxParameter = spy(transactional.getDeclaration());

    when(extensionLoadingContext.getExtensionDeclarer()).thenReturn(extensionDeclarer);
    when(extensionDeclarer.getDeclaration()).thenReturn(extensionDeclaration);
    when(extensionDeclaration.getOperations()).thenReturn(asList(transactionalOperation, notTransactionalOperation));
    when(extensionDeclaration.getMessageSources()).thenReturn(asList(transactionalSource));
  }

  @Test
  public void enrichOperation() throws Exception {
    enricher.enrich(extensionLoadingContext);
    ParameterDeclaration transactionParameter = getTransactionActionParameter(transactionalOperation).orElse(null);
    assertTxParameter(transactionParameter, operationTransactionalActionType, JOIN_IF_POSSIBLE,
                      OPERATION_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION);
  }

  @Test
  public void enrichSource() throws Exception {
    enricher.enrich(extensionLoadingContext);
    ParameterDeclaration transactionParameter = getTransactionActionParameter(transactionalSource).orElse(null);
    assertTxParameter(transactionParameter, sourceTransactionalActionType, NONE,
                      SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION);
  }

  @Test
  public void enrichExistingTransactionalActionParameterIfExist() throws Exception {
    when(extensionDeclaration.getMessageSources()).thenReturn(asList(transactionalSourceWithTxParameter));
    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration transactionParameter = getTransactionActionParameter(transactionalSourceWithTxParameter).orElse(null);
    assertTxParameter(transactionParameter, sourceTransactionalActionType, NONE,
                      SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION);
    assertThat(transactionParameter.getModelProperty(NullModelProperty.class), is(of(nullModelProperty)));
  }

  @Test
  public void enrichExistingTransactionalActionParameterFromOldApiIfExist() throws Exception {
    MetadataType sourceTransactionalActionOldApiType =
        typeLoader.load(org.mule.runtime.extension.api.tx.SourceTransactionalAction.class);
    SourceDeclarer transactional = new ExtensionDeclarer()
        .withMessageSource(TRANSACTIONAL_SOURCE)
        .transactional(true);
    transactional
        .onDefaultParameterGroup()
        .withRequiredParameter(TRANSACTIONAL_ACTION_PARAMETER_NAME)
        .withModelProperty(nullModelProperty)
        .ofType(sourceTransactionalActionOldApiType);

    transactionalSourceWithTxParameter = spy(transactional.getDeclaration());

    when(extensionDeclaration.getMessageSources()).thenReturn(asList(transactionalSourceWithTxParameter));
    enricher.enrich(extensionLoadingContext);

    ParameterDeclaration transactionParameter = getTransactionActionParameter(transactionalSourceWithTxParameter).orElse(null);
    assertTxParameter(transactionParameter, sourceTransactionalActionOldApiType,
                      org.mule.runtime.extension.api.tx.SourceTransactionalAction.NONE,
                      SOURCE_TRANSACTIONAL_ACTION_PARAMETER_DESCRIPTION);
    assertThat(transactionParameter.getModelProperty(NullModelProperty.class), is(of(nullModelProperty)));
  }

  @Test
  public void throwExceptionWhenParametersOfDifferentApisArePresent() throws Exception {
    ParameterGroupDeclaration defaultParameterGroup = transactionalSourceWithTxParameter.getDefaultParameterGroup();
    ParameterDeclaration parameterDeclaration = new ParameterDeclaration("oldApiParameter");
    parameterDeclaration.setType(typeLoader.load(org.mule.runtime.extension.api.tx.SourceTransactionalAction.class), false);
    parameterDeclaration.setRequired(true);
    defaultParameterGroup.addParameter(parameterDeclaration);

    when(extensionDeclaration.getMessageSources()).thenReturn(asList(transactionalSourceWithTxParameter));
    expectedException.expect(IllegalModelDefinitionException.class);
    expectedException
        .expectMessage("Component 'transactionalSource' has transactional parameters from different APIs. Offending parameters are 'oldApiParameter' and 'transactionalAction'.");
    enricher.enrich(extensionLoadingContext);
  }

  @Test
  public void enrichOnlyOnceWhenFlyweight() throws Exception {
    when(extensionDeclaration.getOperations())
        .thenReturn(asList(transactionalOperation, transactionalOperation, notTransactionalOperation));
    enricher.enrich(extensionLoadingContext);
    assertThat(getTransactionActionParameter(transactionalOperation).isPresent(), is(true));
  }

  private void assertTxParameter(ParameterDeclaration transactionParameter, MetadataType type, Object defaultValue,
                                 String description) {
    assertThat(transactionParameter, is(notNullValue()));

    assertThat(transactionParameter.getType(), equalTo(type));
    assertThat(transactionParameter.getExpressionSupport(), is(NOT_SUPPORTED));
    assertThat(transactionParameter.isRequired(), is(false));
    assertThat(transactionParameter.getDefaultValue(), is(defaultValue));
    assertThat(transactionParameter.getDescription(), is(description));
    assertThat(transactionParameter.getLayoutModel().getTabName().get(), is(ADVANCED_TAB));
  }

  private Optional<ParameterDeclaration> getTransactionActionParameter(ComponentDeclaration declaration) {
    List<ParameterDeclaration> txParameters = declaration.getParameterGroup(DEFAULT_GROUP_NAME).getParameters().stream()
        .filter(p -> p.getName().equals(TRANSACTIONAL_ACTION_PARAMETER_NAME))
        .collect(toList());

    assertThat(txParameters, anyOf(hasSize(1), hasSize(0)));

    return txParameters.isEmpty() ? empty() : of(txParameters.get(0));
  }

  private class NullModelProperty implements ModelProperty {

    @Override
    public String getName() {
      return null;
    }

    @Override
    public boolean isPublic() {
      return false;
    }
  }
}
