package io.github.muehmar.pojobuilder.example.standardbuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BuilderClassTest {

  @Test
  void builderWithCustomNameCreated() {
    final BuilderClass builderClass =
        SafeBuilder.create()
            .prop1("prop1")
            .prop2("prop2")
            .prop3(new OuterClass.InnerClass("prop3"))
            .build();

    assertEquals("prop1", builderClass.getProp1());
    assertEquals("prop2", builderClass.getProp2());
    assertEquals("prop3", builderClass.getProp3().getValue());
  }
}
