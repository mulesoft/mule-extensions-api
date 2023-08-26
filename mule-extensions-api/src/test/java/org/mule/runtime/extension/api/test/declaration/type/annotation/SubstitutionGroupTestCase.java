/*
 * Copyright 2023 Salesforce, Inc. All rights reserved.
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.runtime.extension.api.test.declaration.type.annotation;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.mule.runtime.extension.api.declaration.type.annotation.SubstitutionGroup;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SubstitutionGroupTestCase {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();



  @Test
  public void testEmptyConstructor() {
    SubstitutionGroup sg = new SubstitutionGroup();
    assertTrue(StringUtils.isEmpty(sg.getPrefix()));
    assertTrue(StringUtils.isEmpty(sg.getElement()));
  }

  @Test
  public void testConstructorWithEmptyPrefixFails() {
    expectedException.expect(IllegalArgumentException.class);
    SubstitutionGroup sg = new SubstitutionGroup("", "some-element");
  }

  @Test
  public void testConstructorWithEmptyElementFails() {
    expectedException.expect(IllegalArgumentException.class);
    SubstitutionGroup sg = new SubstitutionGroup("someprefix", "");
  }

  @Test
  public void testConstructorWithEmptyArgumentsFails() {
    expectedException.expect(IllegalArgumentException.class);
    SubstitutionGroup sg = new SubstitutionGroup("", "");
  }

  @Test
  public void testConstructorWithArguments() {
    String prefix = "someprefix";
    String element = "some-element";
    SubstitutionGroup sg = new SubstitutionGroup(prefix, element);
    assertEquals(prefix, sg.getPrefix());
    assertEquals(element, sg.getElement());
  }

  @Test
  public void testConstructorFromStringFailsWithWrongFormat() {
    expectedException.expect(IllegalArgumentException.class);
    SubstitutionGroup sg = new SubstitutionGroup("prefix+some-element");
  }

  @Test
  public void testConstructorFromStringFailsWithNoPrefix() {
    expectedException.expect(IllegalArgumentException.class);
    SubstitutionGroup sg = new SubstitutionGroup(":some-element");
  }

  @Test
  public void testConstructorFromStringFailsWithNoElement() {
    expectedException.expect(IllegalArgumentException.class);
    SubstitutionGroup sg = new SubstitutionGroup("prefix:");
  }

  @Test
  public void testConstructorFromString() {
    SubstitutionGroup sg = new SubstitutionGroup("prefix:some-element");
    assertEquals("prefix", sg.getPrefix());
    assertEquals("some-element", sg.getElement());
  }

  @Test
  public void testConstructorWithStringWithSpacesWorks() {
    SubstitutionGroup sg = new SubstitutionGroup("   prefix:some-element   ");
    assertEquals("prefix", sg.getPrefix());
    assertEquals("some-element", sg.getElement());
  }

  @Test
  public void testEquals() {
    String prefix = "prefix";
    String element = "element";
    SubstitutionGroup sg1 = new SubstitutionGroup(prefix, element);
    SubstitutionGroup sg2 = new SubstitutionGroup(prefix, element);
    assertEquals(sg1, sg2);
    SubstitutionGroup sg3 = new SubstitutionGroup(String.format("%s:%s", prefix, element));
    assertEquals(sg1, sg3);
    assertEquals(sg2, sg3);
  }

  @Test
  public void testHashCode() {
    String prefix = "prefix";
    String element = "element";
    SubstitutionGroup sg1 = new SubstitutionGroup(prefix, element);
    SubstitutionGroup sg2 = new SubstitutionGroup(prefix, element);
    assertEquals(sg1.hashCode(), sg2.hashCode());
    SubstitutionGroup sg3 = new SubstitutionGroup(String.format("%s:%s", prefix, element));
    assertEquals(sg1.hashCode(), sg3.hashCode());
    assertEquals(sg2.hashCode(), sg3.hashCode());
  }
}
