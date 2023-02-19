package io.github.muehmar.pojobuilder.example;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class MethodHelper {
  public static final String MAP = "map";
  public static final String MAP_IF = "map";
  public static final String MAP_IF_PRESENT = "map";

  private MethodHelper() {}

  public static boolean hasMethod(Class<?> clazz, String methodName) {
    final Method[] declaredMethods = clazz.getDeclaredMethods();
    return Stream.of(declaredMethods).anyMatch(m -> m.getName().equals(methodName));
  }
}
