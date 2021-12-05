package io.github.muehmar.pojoextension.generator.impl.gen.equalshashcode;

import static io.github.muehmar.pojoextension.generator.data.Necessity.REQUIRED;
import static io.github.muehmar.pojoextension.generator.data.settings.Ability.DISABLED;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_ARRAYS;
import static io.github.muehmar.pojoextension.generator.impl.gen.Refs.JAVA_UTIL_OBJECTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.PojoFields;
import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.data.Name;
import io.github.muehmar.pojoextension.generator.data.Pojo;
import io.github.muehmar.pojoextension.generator.data.PojoField;
import io.github.muehmar.pojoextension.generator.data.Type;
import io.github.muehmar.pojoextension.generator.data.settings.PojoSettings;
import io.github.muehmar.pojoextension.generator.writer.Writer;
import org.junit.jupiter.api.Test;

class EqualsGensTest {

  @Test
  void staticEqualsMethod_when_generatorUsedWithSamplePojoWithoutFields_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.staticEqualsMethod();

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(PList.empty()),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "private static boolean equals(Customer o1, Object obj) {\n"
            + "  if (o1 == obj) return true;\n"
            + "  if (obj == null || o1.getClass() != obj.getClass()) return false;\n"
            + "  final Customer o2 = (Customer) obj;\n"
            + "  return true;\n"
            + "}",
        writer.asString());
    assertFalse(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void staticEqualsMethod_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.staticEqualsMethod();

    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals(
        "private static boolean equals(Customer o1, Object obj) {\n"
            + "  if (o1 == obj) return true;\n"
            + "  if (obj == null || o1.getClass() != obj.getClass()) return false;\n"
            + "  final Customer o2 = (Customer) obj;\n"
            + "  return Objects.equals(o1.getId(), o2.getId())\n"
            + "      && Objects.equals(o1.getUsername(), o2.getUsername())\n"
            + "      && Objects.equals(o1.getNickname(), o2.getNickname());\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void staticEqualsMethod_when_generatorUsedWithPrimitiveTypes_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.staticEqualsMethod();

    final PList<PojoField> primitiveFields =
        Type.allPrimitives().map(t -> new PojoField(t.getName().prefix("p"), t, REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample()
                .withFields(primitiveFields)
                .withGetters(primitiveFields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "private static boolean equals(Customer o1, Object obj) {\n"
            + "  if (o1 == obj) return true;\n"
            + "  if (obj == null || o1.getClass() != obj.getClass()) return false;\n"
            + "  final Customer o2 = (Customer) obj;\n"
            + "  return o1.getPint() == o2.getPint()\n"
            + "      && o1.getPbyte() == o2.getPbyte()\n"
            + "      && o1.getPshort() == o2.getPshort()\n"
            + "      && o1.getPlong() == o2.getPlong()\n"
            + "      && o1.getPfloat() == o2.getPfloat()\n"
            + "      && Double.compare(o1.getPdouble(), o2.getPdouble()) == 0\n"
            + "      && o1.isPboolean() == o2.isPboolean()\n"
            + "      && o1.getPchar() == o2.getPchar();\n"
            + "}",
        writer.asString());
    assertFalse(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void staticEqualsMethod_when_generatorUsedWithSamplePojoAndArrayField_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.staticEqualsMethod();

    final PList<PojoField> fields =
        Pojos.sample()
            .getFields()
            .cons(
                new PojoField(
                    Name.fromString("byteArray"),
                    Type.primitive("byte").withIsArray(true),
                    REQUIRED));

    final Writer writer =
        generator.generate(
            Pojos.sample().withFields(fields).withGetters(fields.map(PojoFields::toGetter)),
            PojoSettings.defaultSettings(),
            Writer.createDefault());
    assertEquals(
        "private static boolean equals(Customer o1, Object obj) {\n"
            + "  if (o1 == obj) return true;\n"
            + "  if (obj == null || o1.getClass() != obj.getClass()) return false;\n"
            + "  final Customer o2 = (Customer) obj;\n"
            + "  return Arrays.equals(o1.getByteArray(), o2.getByteArray())\n"
            + "      && Objects.equals(o1.getId(), o2.getId())\n"
            + "      && Objects.equals(o1.getUsername(), o2.getUsername())\n"
            + "      && Objects.equals(o1.getNickname(), o2.getNickname());\n"
            + "}",
        writer.asString());
    assertTrue(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void staticEqualsMethod_when_disabled_then_noOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.staticEqualsMethod();

    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withEqualsHashCodeAbility(DISABLED),
            Writer.createDefault());
    assertEquals("", writer.asString());
    assertTrue(writer.getRefs().isEmpty());
  }

  @Test
  void equalsMethod_when_generatorUsedWithSamplePojo_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.equalsMethod();
    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals(
        "@Override\n"
            + "public boolean equals(Object obj) {\n"
            + "  return equals(self(), obj);\n"
            + "}",
        writer.asString());
    assertFalse(writer.getRefs().exists(JAVA_UTIL_OBJECTS::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_ARRAYS::equals));
  }

  @Test
  void equalsMethod_when_disabled_then_noOutput() {
    final Generator<Pojo, PojoSettings> generator = EqualsGens.equalsMethod();

    final Writer writer =
        generator.generate(
            Pojos.sample(),
            PojoSettings.defaultSettings().withEqualsHashCodeAbility(DISABLED),
            Writer.createDefault());
    assertEquals("", writer.asString());
    assertTrue(writer.getRefs().isEmpty());
  }
}