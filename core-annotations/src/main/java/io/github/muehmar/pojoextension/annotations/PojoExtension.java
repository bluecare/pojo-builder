package io.github.muehmar.pojoextension.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Creates an extension class for the annotated class. */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface PojoExtension {

  /** Defines how optional fields in a pojo are detected. */
  OptionalDetection[] optionalDetection() default {
    OptionalDetection.OPTIONAL_CLASS, OptionalDetection.NULLABLE_ANNOTATION
  };

  /**
   * Override the default name which is used for the extension class. `{CLASSNAME}` gets by the
   * classname of the annotated class.
   */
  String extensionName() default "{CLASSNAME}Extension";

  /** Enables or disables the generation of the SafeBuilder. */
  boolean enableSafeBuilder() default true;

  /**
   * Override the default name which is used for the discrete builder class. `{CLASSNAME}` gets by
   * the classname of the annotated class.
   */
  String builderName() default "{CLASSNAME}Builder";

  /**
   * Creates a discrete builder class. If set to false, the builder is part of the extension class.
   */
  boolean discreteBuilder() default true;

  /** Enables or disables the generation of the equals and hashCode methods. */
  boolean enableEqualsAndHashCode() default true;

  /** Enables or disables the generation of the toString method. */
  boolean enableToString() default true;

  /** Enables or disables the generation of the with methods. */
  boolean enableWithers() default true;

  /** Enables or disables the generation of the map methods. */
  boolean enableMappers() default true;
}