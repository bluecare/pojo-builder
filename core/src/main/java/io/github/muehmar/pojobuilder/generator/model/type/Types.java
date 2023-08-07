package io.github.muehmar.pojobuilder.generator.model.type;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.PackageName;
import java.util.Optional;

public class Types {
  private Types() {}

  public static Type primitiveDouble() {
    return new Type(PrimitiveType.DOUBLE);
  }

  public static Type primitiveInt() {
    return new Type(PrimitiveType.INT);
  }

  public static Type primitiveLong() {
    return new Type(PrimitiveType.LONG);
  }

  public static Type primitiveChar() {
    return new Type(PrimitiveType.CHAR);
  }

  public static Type primitiveShort() {
    return new Type(PrimitiveType.SHORT);
  }

  public static Type primitiveByte() {
    return new Type(PrimitiveType.BYTE);
  }

  public static Type primitiveBoolean() {
    return new Type(PrimitiveType.BOOLEAN);
  }

  public static Type primitiveFloat() {
    return new Type(PrimitiveType.FLOAT);
  }

  public static Type voidType() {
    return new Type(
        DeclaredType.fromNameAndPackage(Classname.fromString("Void"), PackageName.javaLang()));
  }

  public static Type string() {
    return declaredType(Classname.fromString("String"), PackageName.javaLang());
  }

  public static Type integer() {
    return declaredType(Classname.fromString("Integer"), PackageName.javaLang());
  }

  public static Type booleanClass() {
    return declaredType(Classname.fromString("Boolean"), PackageName.javaLang());
  }

  public static Type optional(Type value) {
    return new Type(DeclaredType.optional(value));
  }

  public static Type map(Type key, Type value) {
    return new Type(
        DeclaredType.of(Classname.fromString("Map"), PackageName.javaUtil(), PList.of(key, value)));
  }

  public static Type list(Type value) {
    return new Type(
        DeclaredType.of(Classname.fromString("List"), PackageName.javaUtil(), PList.single(value)));
  }

  public static Type array(Type itemType) {
    return new Type(ArrayType.fromItemType(itemType));
  }

  public static Type varargs(Type itemType) {
    return new Type(ArrayType.varargs(itemType));
  }

  public static Type comparable(Type objType) {
    return new Type(
        DeclaredType.of(Classname.fromString("Comparable"), PackageName.javaLang(), objType));
  }

  public static Type typeVariable(Name name) {
    return new Type(TypeVariableType.ofName(name));
  }

  public static Type declaredType(Classname name, PackageName pkg) {
    return new Type(DeclaredType.fromNameAndPackage(name, pkg));
  }

  public static Type declaredType(
      Classname name, Optional<PackageName> pkg, PList<Type> typeParameters) {
    return new Type(DeclaredType.of(name, pkg, typeParameters));
  }
}
