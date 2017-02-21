/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.util;

import static java.lang.String.format;
import static java.util.Collections.sort;
import static java.util.Comparator.comparing;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.removeEndIgnoreCase;
import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import static org.mule.metadata.api.utils.MetadataTypeUtils.getTypeId;
import static org.mule.metadata.java.api.utils.JavaTypeUtils.getType;

import org.mule.metadata.api.annotation.TypeAliasAnnotation;
import org.mule.metadata.api.model.MetadataType;
import org.mule.metadata.java.api.utils.JavaTypeUtils;
import org.mule.runtime.api.meta.NamedObject;
import org.mule.runtime.api.meta.model.config.ConfigurationModel;
import org.mule.runtime.api.meta.model.connection.ConnectionProviderModel;
import org.mule.runtime.api.meta.model.declaration.fluent.BaseDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConfigurationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.ConnectionProviderDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.OperationDeclaration;
import org.mule.runtime.api.meta.model.declaration.fluent.SourceDeclaration;
import org.mule.runtime.api.meta.model.operation.OperationModel;
import org.mule.runtime.api.meta.model.source.SourceCallbackModel;
import org.mule.runtime.api.meta.model.source.SourceModel;
import org.mule.runtime.extension.api.annotation.Alias;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;


/**
 * Utilities for manipulating names of supported by the DSL. Mostly for validating the configuration content.
 *
 * @since 1.0
 */
public class NameUtils {

  private static final String CONFIGURATION = "configuration";
  private static final String OPERATION = "operation";
  private static final String CONNECTION_PROVIDER = "connection provider";
  private static final String SOURCE = "source";
  private static final String SOURCE_CALLBACK = "source callback";

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

    uncountable("equipment");
    uncountable("information");
    uncountable("rice");
    uncountable("money");
    uncountable("species");
    uncountable("series");
    uncountable("fish");
    uncountable("sheep");
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
   * Transforms a camel case value into a hyphenizedone.
   * <p>
   * For example:
   * {@code messageProcessor} would be transformed to {@code message-processor}
   *
   * @param camelCaseName a {@link String} in camel case form
   * @return the {@code camelCaseName} in hypenized form
   */
  public static String hyphenize(String camelCaseName) {
    if (isBlank(camelCaseName)) {
      return camelCaseName;
    }

    String result = "";
    String[] parts = camelCaseName.split("(?<!^)(?=[A-Z])");

    for (int i = 0; i < parts.length; i++) {
      result += parts[i].toLowerCase() + (i < parts.length - 1 ? "-" : "");
    }

    return result;
  }

  /**
   * Return the pluralized version of a word.
   *
   * @param word The word
   * @return The pluralized word
   */
  public static String pluralize(String word) {
    if (isUncountable(word)) {
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
    if (isUncountable(word)) {
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
   * Return the itemized version of a word, which is
   * an {@link this#hyphenize hyphenized} version of the word with
   * the {@code item} suffix
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
      for (String w : uncountable) {
        if (w.equalsIgnoreCase(word)) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns a hypenized name of the give top level {@code metadataType}.
   * <p>
   * This method will look for the {@link TypeAliasAnnotation} of the {@link MetadataType}
   * to get the type simple name.
   * <p>
   * As a fallback, it uses {@link JavaTypeUtils#getType(MetadataType)} to obtain the
   * {@link Class} that the {@code metadataType} represents. Then, it
   * checks if the  {@link Alias} annotation is present. If so, the
   * {@link Alias#value()} is used. Otherwise, the {@link Class#getSimpleName} will
   * be considered.
   *
   * @param metadataType the {@link MetadataType} which name you want
   * @return the hypenized name for the given {@code type}
   */
  public static String getTopLevelTypeName(MetadataType metadataType) {
    return hyphenize(getAliasName(metadataType));
  }

  public static String getAliasName(MetadataType metadataType) {
    return metadataType.getAnnotation(TypeAliasAnnotation.class).map(TypeAliasAnnotation::getValue)
        .orElseGet(() -> getTypeId(metadataType).map(typeId -> metadataType.getMetadataFormat().equals(JAVA)
            ? getAliasName(getType(metadataType))
            : typeId).orElseThrow(() -> new IllegalArgumentException("No name available for the given type")));
  }

  public static String getAliasName(Class<?> type) {
    return getAliasName(type.getSimpleName(), type.getAnnotation(Alias.class));
  }

  public static String getAliasName(Field field) {
    return getAliasName(field.getName(), field.getAnnotation(Alias.class));
  }

  public static String getAliasName(Parameter parameter) {
    return getAliasName(parameter.getName(), parameter.getAnnotation(Alias.class));
  }

  public static String getAliasName(String defaultName, Alias aliasAnnotation) {
    String alias = aliasAnnotation != null ? aliasAnnotation.value() : null;
    return isEmpty(alias) ? defaultName : alias;
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

  public static String getComponentModelTypeName(Object component) {
    if (component instanceof OperationModel) {
      return OPERATION;
    } else if (component instanceof ConfigurationModel) {
      return CONFIGURATION;
    } else if (component instanceof ConnectionProviderModel) {
      return CONNECTION_PROVIDER;
    } else if (component instanceof SourceModel) {
      return SOURCE;
    } else if (component instanceof SourceCallbackModel) {
      return SOURCE_CALLBACK;
    }

    throw new IllegalArgumentException(format("Component '%s' is not an instance of any known model type [%s, %s, %s, %s]",
                                              component.toString(), CONFIGURATION, CONNECTION_PROVIDER, OPERATION, SOURCE));
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
    }

    throw new IllegalArgumentException(format("Component '%s' is not an instance of any known model type [%s, %s, %s, %s]",
                                              declaration.toString(), CONFIGURATION, CONNECTION_PROVIDER, OPERATION, SOURCE));
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

  /**
   * Removes everything that's not a word, a dot nor a hyphen
   *
   * @param originalName name that needs the removal of invalid characters
   * @return name without invalid characters
   */
  public static String sanitizeName(String originalName) {
    return originalName.replaceAll("[^\\w|\\.\\-]", EMPTY);
  }

  private static class Inflection {

    private String pattern;
    private String replacement;
    private boolean ignoreCase;

    public Inflection(String pattern, String replacement) {
      this(pattern, replacement, true);
    }

    public Inflection(String pattern, String replacement, boolean ignoreCase) {
      this.pattern = pattern;
      this.replacement = replacement;
      this.ignoreCase = ignoreCase;
    }


    /**
     * Does the given word match?
     *
     * @param word The word
     * @return True if it matches the inflection pattern
     */
    public boolean match(String word) {
      int flags = 0;
      if (ignoreCase) {
        flags = flags | java.util.regex.Pattern.CASE_INSENSITIVE;
      }
      return java.util.regex.Pattern.compile(pattern, flags).matcher(word).find();
    }

    /**
     * Replace the word with its pattern.
     *
     * @param word The word
     * @return The result
     */
    public String replace(String word) {
      int flags = 0;
      if (ignoreCase) {
        flags = flags | java.util.regex.Pattern.CASE_INSENSITIVE;
      }
      return java.util.regex.Pattern.compile(pattern, flags).matcher(word).replaceAll(replacement);
    }
  }
}
