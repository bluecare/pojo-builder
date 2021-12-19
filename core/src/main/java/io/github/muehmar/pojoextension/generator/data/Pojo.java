package io.github.muehmar.pojoextension.generator.data;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.Strings;
import io.github.muehmar.pojoextension.annotations.PojoExtension;
import java.util.Optional;

@PojoExtension(extensionName = "{CLASSNAME}Ext")
@SuppressWarnings("java:S2160")
public class Pojo extends PojoExt {
  private final Name name;
  private final PackageName pkg;
  private final PList<PojoField> fields;
  private final PList<Constructor> constructors;
  private final PList<Getter> getters;
  private final PList<Generic> generics;

  public Pojo(
      Name name,
      PackageName pkg,
      PList<PojoField> fields,
      PList<Constructor> constructors,
      PList<Getter> getters,
      PList<Generic> generics) {
    this.name = name;
    this.pkg = pkg;
    this.fields = fields;
    this.constructors = constructors;
    this.getters = getters;
    this.generics = generics;
  }

  public Name getName() {
    return name;
  }

  @io.github.muehmar.pojoextension.annotations.Getter("pkg")
  public PackageName getPackage() {
    return pkg;
  }

  public PList<PojoField> getFields() {
    return fields;
  }

  public PList<PojoAndField> getPojoAndFields() {
    return fields.map(f -> new PojoAndField(this, f));
  }

  public PList<Constructor> getConstructors() {
    return constructors;
  }

  public PList<Getter> getGetters() {
    return getters;
  }

  public PList<Generic> getGenerics() {
    return generics;
  }

  public PList<Name> getGenericImports() {
    return generics.flatMap(Generic::getUpperBounds).flatMap(Type::getImports);
  }

  public String getDiamond() {
    return generics.nonEmpty() ? "<>" : "";
  }

  public PList<String> getGenericTypeDeclarations() {
    return generics.map(Generic::getTypeDeclaration).map(Name::asString);
  }

  public String getGenericTypeDeclarationSection() {
    return Strings.surroundIfNotEmpty("<", getGenericTypeDeclarations().mkString(", "), ">");
  }

  public String getTypeVariablesSection() {
    return Strings.surroundIfNotEmpty(
        "<", generics.map(Generic::getTypeVariable).mkString(", "), ">");
  }

  public Optional<MatchingConstructor> findMatchingConstructor() {
    return constructors
        .flatMapOptional(c -> c.matchFields(fields).map(f -> new MatchingConstructor(c, f)))
        .headOption();
  }

  public MatchingConstructor getMatchingConstructorOrThrow() {
    return findMatchingConstructor()
        .orElseThrow(() -> new IllegalArgumentException(noMatchingConstructorMessage()));
  }

  private String noMatchingConstructorMessage() {
    return String.format(
        "No matching constructor found for class %s."
            + " A constructor should have all the fields as arguments in the order of declaration and matching type,"
            + " where the actual type of a non-required field can be wrapped into an java.util.Optional",
        getName());
  }

  public Optional<FieldGetter> findMatchingGetter(PojoField field) {
    return getters.flatMapOptional(g -> g.getFieldGetter(field)).headOption();
  }

  public FieldGetter getMatchingGetterOrThrow(PojoField field) {
    return findMatchingGetter(field)
        .orElseThrow(() -> new IllegalArgumentException(noGetterFoundMessage(field)));
  }

  public PList<FieldGetter> getAllGettersOrThrow() {
    final Pojo self = this;
    return fields.map(self::getMatchingGetterOrThrow);
  }

  private String noGetterFoundMessage(PojoField field) {
    final String optionalMessage =
        field.isOptional()
            ? "The the actual type of this non-required field can be wrapped into an java.util.Optional."
            : "";
    return String.format(
        "Unable to find the getter for field '%s'.\n"
            + "The method name should be '%s' and the returnType should match the field type %s.\n"
            + "In case the method cannot be renamed you can use the @@Getter(\"%s\") annotation to mark\n"
            + "the method as getter for the field '%s'.\n"
            + "%s",
        field.getName(),
        Getter.getterName(field),
        field.getType().getTypeDeclaration(),
        field.getName(),
        field.getName(),
        optionalMessage);
  }
}
