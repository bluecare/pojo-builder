package io.github.muehmar.pojobuilder.generator.impl.gen;

import static io.github.muehmar.codegenerator.writer.Writer.javaWriter;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_INTEGER;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_LANG_STRING;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_LIST;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_MAP;
import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.FieldBuilderMethods;
import io.github.muehmar.pojobuilder.generator.PojoFields;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.Argument;
import io.github.muehmar.pojobuilder.generator.model.FieldBuilderMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.PojoField;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import org.junit.jupiter.api.Test;

class RefsGenTest {

  @Test
  void fieldRefs_when_genericFieldTypeAsInput_then_writerContainsAllRefs() {
    final Generator<PojoField, PojoSettings> gen = RefsGen.fieldRefs();
    final Writer writer =
        gen.generate(PojoFields.requiredMap(), PojoSettings.defaultSettings(), javaWriter());

    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_MAP::equals));
  }

  @Test
  void genericRefs_when_genericPojo_then_writerContainsAllRefs() {
    final Generator<Pojo, PojoSettings> gen = RefsGen.genericRefs();
    final Writer writer =
        gen.generate(Pojos.genericSample(), PojoSettings.defaultSettings(), javaWriter());

    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_LIST::equals));
  }

  @Test
  void genericRefs_when_nonGenericPojo_then_noRefs() {
    final Generator<Pojo, PojoSettings> gen = RefsGen.genericRefs();
    final Writer writer =
        gen.generate(Pojos.sample(), PojoSettings.defaultSettings(), javaWriter());

    assertTrue(writer.getRefs().isEmpty());
  }

  @Test
  void fieldBuilderMethodRefs_when_called_then_refsForArgumentsAddButNotForFieldReturnType() {
    final Generator<FieldBuilderMethod, PojoSettings> gen = RefsGen.fieldBuilderMethodRefs();
    final FieldBuilderMethod fieldBuilderMethod =
        FieldBuilderMethods.forField(
            PojoFields.optionalName(),
            Name.fromString("customMethod"),
            new Argument(Name.fromString("val"), Types.map(Types.string(), Types.integer())));
    final Writer writer =
        gen.generate(fieldBuilderMethod, PojoSettings.defaultSettings(), javaWriter());

    assertTrue(writer.getRefs().exists(JAVA_LANG_STRING::equals));
    assertTrue(writer.getRefs().exists(JAVA_LANG_INTEGER::equals));
    assertTrue(writer.getRefs().exists(JAVA_UTIL_MAP::equals));
    assertFalse(writer.getRefs().exists(JAVA_UTIL_OPTIONAL::equals));
  }
}
