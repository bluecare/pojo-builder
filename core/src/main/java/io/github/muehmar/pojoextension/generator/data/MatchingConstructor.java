package io.github.muehmar.pojoextension.generator.data;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import java.util.Objects;

@PojoExtension
public class MatchingConstructor extends MatchingConstructorExtension {
  private final Constructor constructor;
  private final PList<PojoField> fields;

  public MatchingConstructor(Constructor constructor, PList<PojoField> fields) {
    this.constructor = constructor;
    this.fields = fields;
  }

  public Constructor getConstructor() {
    return constructor;
  }

  public PList<PojoField> getFields() {
    return fields;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MatchingConstructor that = (MatchingConstructor) o;
    return Objects.equals(constructor, that.constructor) && Objects.equals(fields, that.fields);
  }

  @Override
  public int hashCode() {
    return Objects.hash(constructor, fields);
  }

  @Override
  public String toString() {
    return "MatchingConstructor{" + "constructor=" + constructor + ", fields=" + fields + '}';
  }
}
