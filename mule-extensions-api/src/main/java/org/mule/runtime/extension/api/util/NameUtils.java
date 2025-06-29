/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static java.lang.Math.max;
import static java.lang.String.format;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.removeEndIgnoreCase;
import static org.mule.runtime.extension.api.util.ExtensionMetadataTypeUtils.getId;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.annotation.TypeIdAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.annotation.ClassInformationAnnotation;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.construct.ConstructModel;
import org.mule.runtime.api.meta.model.declaration.fluent.BaseDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConstructDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.FunctionDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ParameterizedDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceCallbackDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.function.FunctionModel;
import org.mule.runtime.api.meta.model.nested.NestableElementModel;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.parameter.ParameterizedModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.declaration.type.TypeUtils;
import org.mule.runtime.extension.internal.loader.util.JavaParserUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Extends {@link org.mule.runtime.api.util.NameUtils} with extensions oriented behavior
 *
 * @since 1.0
 */
public class NameUtils extends org.mule.runtime.api.util.NameUtils {

  public static final String CONFIGURATION = "configuration";
  public static final String OPERATION = "operation";
  public static final String CONNECTION_PROVIDER = "connection provider";
  public static final String SOURCE = "source";
  public static final String SOURCE_CALLBACK = "source callback";
  public static final String FUNCTION = "function";
  public static final String CONSTRUCT = "construct";
  public static final String NESTABLE_COMPONENT = "nestable component";

  private static final List<Inflection> plural = new ArrayList<>();
  private static final List<Inflection> singular = new ArrayList<>();
  private static final List<String> uncountable = new ArrayList<>();

  static {
    // plural is "singular to plural form"
    // singular is "plural to singular form"
    plural("$", "s");
    plural("s$", "s");
    plural("(ax|test)is$", "$1es");
    plural("(octop|vir)us$", "$1i");
    plural("(alias|status)$", "$1es");
    plural("(bu)s$", "$1ses");
    plural("(buffal|tomat)o$", "$1oes");
    plural("([ti])um$", "$1a");
    plural("sis$", "ses");
    plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
    plural("(hive)$", "$1s");
    plural("([^aeiouy]|qu)y$", "$1ies");
    plural("(x|ch|ss|sh)$", "$1es");
    plural("(matr|vert|ind)ix|ex$", "$1ices");
    plural("([m|l])ouse$", "$1ice");
    plural("^(ox)$", "$1en");
    plural("(quiz)$", "$1zes");

    singular("s$", "");
    singular("(n)ews$", "$1ews");
    singular("([ti])a$", "$1um");
    singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
    singular("(^analy)ses$", "$1sis");
    singular("([^f])ves$", "$1fe");
    singular("(hive)s$", "$1");
    singular("(tive)s$", "$1");
    singular("([lr])ves$", "$1f");
    singular("([^aeiouy]|qu)ies$", "$1y");
    singular("(s)eries$", "$1eries");
    singular("(m)ovies$", "$1ovie");
    singular("(x|ch|ss|sh)es$", "$1");
    singular("([m|l])ice$", "$1ouse");
    singular("(bus)es$", "$1");
    singular("(o)es$", "$1");
    singular("(shoe)s$", "$1");
    singular("(cris|ax|test)es$", "$1is");
    singular("(octop|vir)i$", "$1us");
    singular("(alias|status)es$", "$1");
    singular("^(ox)en", "$1");
    singular("(vert|ind)ices$", "$1ex");
    singular("(matr)ices$", "$1ix");
    singular("(quiz)zes$", "$1");

    // irregular
    irregular("person", "people");
    irregular("man", "men");
    irregular("child", "children");
    irregular("sex", "sexes");
    irregular("move", "moves");

    // W-17864898: core elements that are not wrapped, so they are not consistent with SDK implemented components
    uncountable("metadata");
    uncountable("property");
  }

  private NameUtils() {}

  /**
   * Registers a plural {@code replacement} for the given {@code pattern}
   *
   * @param pattern     the pattern for which you want to register a plural form
   * @param replacement the replacement pattern
   */
  private static void plural(String pattern, String replacement) {
    plural.add(0, new Inflection(pattern, replacement));
  }

  /**
   * Registers a singular {@code replacement} for the given {@code pattern}
   *
   * @param pattern     the pattern for which you want to register a plural form
   * @param replacement the replacement pattern
   */
  private static void singular(String pattern, String replacement) {
    singular.add(0, new Inflection(pattern, replacement));
  }

  private static void irregular(String s, String p) {
    plural("(" + s.substring(0, 1) + ")" + s.substring(1) + "$", "$1" + p.substring(1));
    singular("(" + p.substring(0, 1) + ")" + p.substring(1) + "$", "$1" + s.substring(1));
  }

  private static void uncountable(String word) {
    uncountable.add(word);
  }


  /**
   * Return the pluralized version of a word.
   *
   * @param word The word
   * @return The pluralized word
   */
  public static String pluralize(String word) {
    return pluralize(word, false);
  }


  /**
   * Return the pluralized version of a word.
   *
   * @param word                The word
   * @param considerUncountable if it should consider that the word may be uncountable
   * @return The pluralized word
   */
  public static String pluralize(String word, boolean considerUncountable) {
    if (isUncountable(word) && considerUncountable) {
      return word;
    } else {
      for (Inflection inflection : plural) {
        if (inflection.match(word)) {
          return inflection.replace(word);
        }
      }
      return word;
    }
  }

  /**
   * Return the singularized version of a word.
   *
   * @param word The word
   * @return The singularized word
   */
  public static String singularize(String word) {
    return singularize(word, false);
  }

  /**
   * Return the singularized version of a word.
   *
   * @param word                The word
   * @param considerUncountable if it should consider that the word may be uncountable
   * @return The singularized word
   */
  public static String singularize(String word, boolean considerUncountable) {
    if (isUncountable(word) && considerUncountable) {
      return word;
    } else {
      for (Inflection inflection : singular) {
        if (inflection.match(word)) {
          return inflection.replace(word);
        }
      }
    }
    return word;
  }

  /**
   * Return the itemized version of a word, which is an {@link this#hyphenize hyphenized} version of the word with the
   * {@code item} suffix
   *
   * @param word The word
   * @return The singularized word
   */
  public static String itemize(String word) {
    return isBlank(word) ? word : hyphenize(word).concat("-item");
  }

  /**
   * Return true if the word is uncountable.
   *
   * @param word The word
   * @return True if it is uncountable
   */
  public static boolean isUncountable(String word) {
    if (isBlank(word)) {
      return false;
    }
    for (String w : uncountable) {
      if (w.equalsIgnoreCase(word)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns a hypenized name of the give top level {@code metadataType}.
   * <p>
   * This method will look for the {@link TypeAliasAnnotation} of the {@link MetadataType} to get the type simple name.
   * <p>
   * As a fallback, it uses {@link ClassInformationAnnotation#getClassname()} to obtain the simple name of the class. Finally if
   * there is no ClassInformation, the {@link TypeIdAnnotation} will be used as Alias, failing if it is not found.
   *
   * @param metadataType the {@link MetadataType} which name you want
   * @return the hypenized name for the given {@code type}
   */
  public static String getTopLevelTypeName(MetadataType metadataType) {
    return hyphenize(getAliasName(metadataType));
  }

  /**
   * This method will look for the {@link TypeAliasAnnotation} of the {@link MetadataType} to get the type simple name.
   * <p>
   * As a fallback, it uses {@link ClassInformationAnnotation#getClassname()} to obtain the simple name of the class. Finally if
   * there is no ClassInformation, the {@link TypeIdAnnotation} will be used as Alias, failing if it is not found.
   *
   * @param metadataType the {@link MetadataType} whose Alias is required
   * @return the alias of the given type
   */
  public static String getAliasName(MetadataType metadataType) {
    return metadataType.getAnnotation(TypeAliasAnnotation.class)
        .map(TypeAliasAnnotation::getValue)
        .orElseGet(() -> metadataType.getAnnotation(ClassInformationAnnotation.class)
            .map(ClassInformationAnnotation::getClassname)
            .map(className -> className.substring(max(className.lastIndexOf("."), className.lastIndexOf("$")) + 1))
            .orElseGet(() -> getId(metadataType)
                .orElseThrow(() -> new IllegalArgumentException("No name information is available for the given type"))));
  }

  /**
   * @deprecated since 1.5.0 use {@link JavaParserUtils#getAlias(Class)} instead
   */
  @Deprecated
  public static String getAliasName(Class<?> type) {
    return JavaParserUtils.getAlias(type);
  }

  /**
   * @deprecated since 1.5.0 use {@link TypeUtils#getAlias(Field)} instead
   */
  @Deprecated
  public static String getAliasName(Field field) {
    return TypeUtils.getAlias(field);
  }

  public static String getAliasName(Parameter parameter) {
    return JavaParserUtils.getAlias(parameter, parameter::getName);
  }

  public static String defaultNamespace(String extensionName) {
    if (isBlank(extensionName)) {
      throw new IllegalArgumentException("No Namespace can be created from an empty name");
    }

    String namespace = deleteWhitespace(extensionName);
    namespace = removeEndIgnoreCase(namespace, "extension");
    namespace = removeEndIgnoreCase(namespace, "connector");
    namespace = removeEndIgnoreCase(namespace, "module");
    return hyphenize(isBlank(namespace) ? extensionName : namespace);
  }

  public static String getComponentModelTypeName(ParameterizedModel component) {
    if (component instanceof OperationModel) {
      return OPERATION;
    } else if (component instanceof ConfigurationModel) {
      return CONFIGURATION;
    } else if (component instanceof ConnectionProviderModel) {
      return CONNECTION_PROVIDER;
    } else if (component instanceof SourceModel) {
      return SOURCE;
    } else if (component instanceof FunctionModel) {
      return FUNCTION;
    } else if (component instanceof ConstructModel) {
      return CONSTRUCT;
    } else if (component instanceof SourceCallbackModel) {
      return SOURCE_CALLBACK;
    } else if (component instanceof NestableElementModel) {
      return NESTABLE_COMPONENT;
    }

    throw new IllegalArgumentException(format("Component '%s' is not an instance of any known model type [%s, %s, %s, %s, %s]",
                                              component.toString(), CONFIGURATION, CONNECTION_PROVIDER, OPERATION, SOURCE,
                                              SOURCE_CALLBACK));
  }

  public static String getDeclarationTypeName(ParameterizedDeclaration declaration) {
    if (declaration instanceof OperationDeclaration) {
      return OPERATION;
    } else if (declaration instanceof ConfigurationDeclaration) {
      return CONFIGURATION;
    } else if (declaration instanceof ConnectionProviderDeclaration) {
      return CONNECTION_PROVIDER;
    } else if (declaration instanceof SourceDeclaration) {
      return SOURCE;
    } else if (declaration instanceof FunctionDeclaration) {
      return FUNCTION;
    } else if (declaration instanceof ConstructDeclaration) {
      return CONSTRUCT;
    } else if (declaration instanceof SourceCallbackDeclaration) {
      return SOURCE_CALLBACK;
    }

    throw new IllegalArgumentException(format("Declaration '%s' is not an instance of any known model type [%s, %s, %s, %s, %s]",
                                              declaration.toString(), CONFIGURATION, CONNECTION_PROVIDER, OPERATION, SOURCE,
                                              SOURCE_CALLBACK));
  }

  public static String getComponentDeclarationTypeName(BaseDeclaration declaration) {
    if (declaration instanceof OperationDeclaration) {
      return OPERATION;
    } else if (declaration instanceof ConfigurationDeclaration) {
      return CONFIGURATION;
    } else if (declaration instanceof ConnectionProviderDeclaration) {
      return CONNECTION_PROVIDER;
    } else if (declaration instanceof SourceDeclaration) {
      return SOURCE;
    } else if (declaration instanceof ConstructDeclaration) {
      return CONSTRUCT;
    }

    throw new IllegalArgumentException(format("Component '%s' is not an instance of any known model type [%s, %s, %s, %s]",
                                              declaration.toString(), CONFIGURATION, CONNECTION_PROVIDER,
                                              OPERATION, SOURCE, CONSTRUCT));
  }

  public static String getModelName(Object model) {
    if (model instanceof NamedObject) {
      return ((NamedObject) model).getName();
    }

    throw new IllegalArgumentException(format("Model '%s' is not a named type"));
  }

  /**
   * Sorts the given {@code list} in ascending alphabetic order, using {@link NamedObject#getName()} as the sorting criteria
   *
   * @param list a {@link List} with instances of {@link NamedObject}
   * @param <T>  the generic type of the items in the {@code list}
   * @return the sorted {@code list}
   */
  public static <T extends NamedObject> List<T> alphaSortDescribedList(List<T> list) {
    if (list == null || list.isEmpty()) {
      return list;
    }

    sort(list, comparing(NamedObject::getName));
    return list;
  }

  private static class Inflection {

    private final Pattern compiledPattern;
    private final String replacement;

    public Inflection(String pattern, String replacement) {
      this(pattern, replacement, true);
    }

    public Inflection(String pattern, String replacement, boolean ignoreCase) {
      this.replacement = replacement;

      int flags = 0;
      if (ignoreCase) {
        flags = flags | java.util.regex.Pattern.CASE_INSENSITIVE;
      }
      compiledPattern = java.util.regex.Pattern.compile(pattern, flags);
    }


    /**
     * Does the given word match?
     *
     * @param word The word
     * @return True if it matches the inflection pattern
     */
    public boolean match(String word) {
      return compiledPattern.matcher(word).find();
    }

    /**
     * Replace the word with its pattern.
     *
     * @param word The word
     * @return The result
     */
    public String replace(String word) {
      return compiledPattern.matcher(word).replaceAll(replacement);
    }
  }
}
