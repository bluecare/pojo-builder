package io.github.muehmar.pojobuilder.generator.impl.gen.instantiation;

import static io.github.muehmar.pojobuilder.generator.impl.gen.Refs.JAVA_UTIL_OPTIONAL;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.pojobuilder.Mapper;
import io.github.muehmar.pojobuilder.generator.model.MatchingConstructor;
import io.github.muehmar.pojobuilder.generator.model.OptionalFieldRelation;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import java.util.Objects;
import java.util.Optional;

public class ConstructorCallGens {
  private ConstructorCallGens() {}

  /**
   * This generator creates a call to the constructor (return the created instance) of the pojo
   * while wrapping optional fields into an {@link Optional} depending on the argument of the
   * constructor.
   */
  public static Generator<Pojo, PojoSettings> callWithAllLocalVariables(String prefix) {
    return Generator.<Pojo, PojoSettings>emptyGen()
        .append(
            constructorCallForFields(prefix),
            pojo -> {
              final MatchingConstructor matchingConstructor = pojo.getMatchingConstructorOrThrow();

              final PList<FinalConstructorArgument> fields =
                  matchingConstructor
                      .getFieldArguments()
                      .map(
                          fa ->
                              FinalConstructorArgument.ofFieldVariable(
                                  new FieldVariable(
                                      pojo, fa.getField(), OptionalFieldRelation.SAME_TYPE),
                                  fa));

              return new ConstructorCall(pojo, fields, matchingConstructor);
            });
  }

  private static Generator<ConstructorCall, PojoSettings> constructorCallForFields(String prefix) {
    return (constructorCall, settings, writer) -> {
      final PList<String> constructorParameters =
          constructorCall
              .getFields()
              .map(
                  finalConstructorArgument ->
                      finalConstructorArgument
                          .getRelation()
                          .apply(
                              finalConstructorArgument,
                              onUnwrapOptional(),
                              onSameType(),
                              onWrapOptional()));

      final boolean hasWrapIntoOptional =
          constructorCall
              .getFields()
              .map(FinalConstructorArgument::getRelation)
              .exists(relation -> relation.equals(OptionalFieldRelation.WRAP_INTO_OPTIONAL));

      return Mapper.initial(writer)
          .mapConditionally(hasWrapIntoOptional, w -> w.ref(JAVA_UTIL_OPTIONAL))
          .apply()
          .println(
              "%snew %s%s(%s);",
              prefix,
              constructorCall.getPojo().getPojoName(),
              constructorCall.getPojo().getDiamond(),
              constructorParameters.mkString(", "));
    };
  }

  private static OptionalFieldRelation.OnUnwrapOptional<FinalConstructorArgument, String>
      onUnwrapOptional() {
    return finalConstructorArgument ->
        String.format("%s.orElse(null)", finalConstructorArgument.getFieldString());
  }

  private static OptionalFieldRelation.OnSameType<FinalConstructorArgument, String> onSameType() {
    return FinalConstructorArgument::getFieldString;
  }

  private static OptionalFieldRelation.OnWrapOptional<FinalConstructorArgument, String>
      onWrapOptional() {
    return finalConstructorArgument ->
        String.format("Optional.ofNullable(%s)", finalConstructorArgument.getFieldString());
  }

  private static class ConstructorCall {
    private final Pojo pojo;
    private final PList<FinalConstructorArgument> fields;
    private final MatchingConstructor constructor;

    public ConstructorCall(
        Pojo pojo, PList<FinalConstructorArgument> fields, MatchingConstructor constructor) {
      this.pojo = pojo;
      this.fields = fields;
      this.constructor = constructor;
    }

    public Pojo getPojo() {
      return pojo;
    }

    public PList<FinalConstructorArgument> getFields() {
      return fields;
    }

    public MatchingConstructor getMatchingConstructor() {
      return constructor;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ConstructorCall that = (ConstructorCall) o;
      return Objects.equals(pojo, that.pojo)
          && Objects.equals(fields, that.fields)
          && Objects.equals(constructor, that.constructor);
    }

    @Override
    public int hashCode() {
      return Objects.hash(pojo, fields, constructor);
    }

    @Override
    public String toString() {
      return "ConstructorCall{"
          + "pojo="
          + pojo
          + ", fields="
          + fields
          + ", constructor="
          + constructor
          + '}';
    }
  }
}
