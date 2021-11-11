package io.github.muehmar.pojoextension.generator.data;

import static io.github.muehmar.pojoextension.generator.data.Necessity.OPTIONAL;
import static io.github.muehmar.pojoextension.generator.data.Necessity.REQUIRED;
import static io.github.muehmar.pojoextension.generator.data.Type.optional;
import static io.github.muehmar.pojoextension.generator.data.Type.string;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class ArgumentTest {

  @ParameterizedTest
  @EnumSource(Necessity.class)
  void getRelation_when_typesAreTheSame_then_sameType(Necessity necessity) {
    final Argument argument = new Argument(Name.fromString("id"), string());
    final PojoField field = new PojoField(Name.fromString("extId"), string(), necessity);

    assertEquals(
        Optional.of(OptionalFieldRelation.SAME_TYPE), argument.getRelationFromField(field));
  }

  @Test
  void
      getRelation_when_argumentTypeWrappedIntoOptionalAndFieldNotRequired_then_relationIsWrappedIntoOptional() {
    final Argument argument = new Argument(Name.fromString("id"), optional(string()));
    final PojoField field = new PojoField(Name.fromString("extId"), string(), OPTIONAL);

    assertEquals(
        Optional.of(OptionalFieldRelation.WRAP_INTO_OPTIONAL),
        argument.getRelationFromField(field));
  }

  @Test
  void getRelation_when_argumentTypeWrappedIntoOptionalAndFieldRequired_then_noRelation() {
    final Argument argument = new Argument(Name.fromString("id"), optional(string()));
    final PojoField field = new PojoField(Name.fromString("extId"), string(), REQUIRED);

    assertEquals(Optional.empty(), argument.getRelationFromField(field));
  }

  @Test
  void
      getRelation_when_fieldTypeWrappedIntoOptionalAndFieldNoRequired_then_relationIsUnwrapOptional() {
    final Argument argument = new Argument(Name.fromString("id"), string());
    final PojoField field = new PojoField(Name.fromString("extId"), optional(string()), OPTIONAL);

    assertEquals(
        Optional.of(OptionalFieldRelation.UNWRAP_OPTIONAL), argument.getRelationFromField(field));
  }

  @Test
  void getRelation_when_fieldTypeWrappedIntoOptionalAndFieldRequired_then_noRelation() {
    final Argument argument = new Argument(Name.fromString("id"), string());
    final PojoField field = new PojoField(Name.fromString("extId"), optional(string()), REQUIRED);

    assertEquals(Optional.empty(), argument.getRelationFromField(field));
  }
}
