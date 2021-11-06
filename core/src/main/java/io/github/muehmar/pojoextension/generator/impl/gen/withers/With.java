package io.github.muehmar.pojoextension.generator.impl.gen.withers;

import static io.github.muehmar.pojoextension.generator.impl.JavaModifier.PUBLIC;
import static io.github.muehmar.pojoextension.generator.impl.JavaModifier.STATIC;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojoextension.generator.Generator;
import io.github.muehmar.pojoextension.generator.data.PojoSettings;
import io.github.muehmar.pojoextension.generator.impl.gen.MethodGen;
import io.github.muehmar.pojoextension.generator.impl.gen.withers.data.WithField;
import java.util.function.Function;

public class With {
  private With() {}

  public static Generator<WithField, PojoSettings> withMethod() {
    return MethodGen.<WithField, PojoSettings>modifiers(PUBLIC)
        .returnType(wf -> wf.getPojo().getName().asString())
        .methodName(wf -> "with" + wf.getField().getName().toPascalCase())
        .singleArgument(
            wf ->
                String.format("%s %s", wf.getField().getType().getName(), wf.getField().getName()))
        .content(wf -> String.format("return with(self(), %s);", wf.getField().getName()));
  }

  public static Generator<WithField, PojoSettings> staticWithMethod() {
    final Function<WithField, PList<String>> arguments =
        wf ->
            PList.of(
                String.format("%s self", wf.getPojo().getName()),
                String.format("%s %s", wf.getField().getType().getName(), wf.getField().getName()));

    return MethodGen.<WithField, PojoSettings>modifiers(PUBLIC, STATIC)
        .returnType(wf -> wf.getPojo().getName().asString())
        .methodName(wf -> "with" + wf.getField().getName().toPascalCase())
        .arguments(arguments)
        .content("return self;");
  }
}
