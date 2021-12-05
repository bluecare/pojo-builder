package io.github.muehmar.pojoextension.generator.impl.gen;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.Pojos;
import io.github.muehmar.pojoextension.generator.data.Pojo;
import io.github.muehmar.pojoextension.generator.data.settings.PojoSettings;
import io.github.muehmar.pojoextension.generator.impl.JavaModifier;
import io.github.muehmar.pojoextension.generator.writer.Writer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ClassGenTest {

  @Test
  void generate_when_simplePojoAndSingleContent_then_correctGeneratedString() {
    final ClassGen<Pojo, PojoSettings> generator =
        ClassGen.<Pojo, PojoSettings>topLevel()
            .packageGen(new PackageGen())
            .modifiers(JavaModifier.PUBLIC)
            .className((p, s) -> s.extensionName(p).asString())
            .content(Generator.ofWriterFunction(w -> w.println("Content")));

    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals(
        "package io.github.muehmar;\n"
            + "\n"
            + "\n"
            + "public class CustomerExtension {\n"
            + "  Content\n"
            + "}",
        writer.asString());
  }

  @Test
  void generate_when_refAddedInContent_then_refPrinted() {
    final ClassGen<Pojo, PojoSettings> generator =
        ClassGen.<Pojo, PojoSettings>topLevel()
            .packageGen(new PackageGen())
            .modifiers(JavaModifier.PUBLIC)
            .className((p, s) -> s.extensionName(p).asString())
            .content(Generator.ofWriterFunction(w -> w.ref("java.util.Optional")));

    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals(
        "package io.github.muehmar;\n"
            + "\n"
            + "import java.util.Optional;\n"
            + "\n"
            + "public class CustomerExtension {\n"
            + "}",
        writer.asString());
  }

  @Test
  void generate_when_nestedClass_then_noRefsAndPackagePrinted() {
    final ClassGen<Pojo, PojoSettings> generator =
        ClassGen.<Pojo, PojoSettings>nested()
            .modifiers(JavaModifier.PUBLIC)
            .className((p, s) -> s.extensionName(p).asString())
            .content(Generator.ofWriterFunction(w -> w.ref("import java.util.Optional;")));

    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals("public class CustomerExtension {\n" + "}", writer.asString());
  }

  @ParameterizedTest
  @MethodSource("publicAndFinalModifierUnordered")
  void generate_when_privateAndFinalModifierUnordered_then_correctOutputWithOrderedModifiers(
      PList<JavaModifier> modifiers) {
    final ClassGen<Pojo, PojoSettings> generator =
        ClassGen.<Pojo, PojoSettings>nested()
            .modifiers(modifiers.toArray(JavaModifier.class))
            .className((p, s) -> s.extensionName(p).asString())
            .content(Generator.ofWriterFunction(w -> w.ref("import java.util.Optional;")));

    final Writer writer =
        generator.generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault());
    assertEquals("public final class CustomerExtension {\n" + "}", writer.asString());
  }

  private static Stream<Arguments> publicAndFinalModifierUnordered() {
    return Stream.of(
        Arguments.of(PList.of(JavaModifier.FINAL, JavaModifier.PUBLIC)),
        Arguments.of(PList.of(JavaModifier.PUBLIC, JavaModifier.FINAL)));
  }
}