package io.github.muehmar.pojoextension.generator;

import static io.github.muehmar.pojoextension.generator.model.Necessity.OPTIONAL;
import static io.github.muehmar.pojoextension.generator.model.Necessity.REQUIRED;
import static io.github.muehmar.pojoextension.generator.model.type.Types.string;

import io.github.muehmar.pojoextension.generator.model.Argument;
import io.github.muehmar.pojoextension.generator.model.Name;
import io.github.muehmar.pojoextension.generator.model.PojoField;
import io.github.muehmar.pojoextension.generator.model.type.Types;

public class PojoFields {
  private PojoFields() {}

  public static PojoField requiredId() {
    return new PojoField(Names.id(), Types.integer(), REQUIRED);
  }

  public static PojoField optionalName() {
    return new PojoField(Name.fromString("name"), Types.string(), OPTIONAL);
  }

  public static PojoField requiredMap() {
    return new PojoField(
        Name.fromString("someMap"), Types.map(string(), Types.list(string())), REQUIRED);
  }

  public static Argument toArgument(PojoField f) {
    return new Argument(f.getName(), f.getType());
  }
}
