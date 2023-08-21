package io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe;

import static io.github.muehmar.pojobuilder.generator.impl.gen.builder.unsafe.BuildMethod.buildMethod;
import static io.github.muehmar.pojobuilder.snapshottesting.SnapshotUtil.writerSnapshot;

import au.com.origin.snapshots.Expect;
import au.com.origin.snapshots.annotations.SnapshotName;
import au.com.origin.snapshots.junit5.SnapshotExtension;
import io.github.muehmar.codegenerator.Generator;
import io.github.muehmar.codegenerator.writer.Writer;
import io.github.muehmar.pojobuilder.generator.Pojos;
import io.github.muehmar.pojobuilder.generator.model.BuildMethod;
import io.github.muehmar.pojobuilder.generator.model.Name;
import io.github.muehmar.pojobuilder.generator.model.Pojo;
import io.github.muehmar.pojobuilder.generator.model.settings.PojoSettings;
import io.github.muehmar.pojobuilder.generator.model.type.Types;
import io.github.muehmar.pojobuilder.snapshottesting.IntellijDiffSnapshotTestExtension;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({SnapshotExtension.class, IntellijDiffSnapshotTestExtension.class})
class BuildMethodTest {
  private Expect expect;

  @Test
  @SnapshotName("sample")
  void buildMethod_when_calledWithSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final String output =
        generator
            .generate(Pojos.sample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("genericSample")
  void buildMethod_when_calledWithGenericSample_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final String output =
        generator
            .generate(Pojos.genericSample(), PojoSettings.defaultSettings(), Writer.createDefault())
            .asString();

    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("genericSampleAndBuildMethod")
  void buildMethod_when_calledWithGenericSampleAndBuildMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final io.github.muehmar.pojobuilder.generator.model.BuildMethod buildMethod =
        new BuildMethod(Name.fromString("customBuildMethod"), Types.string());
    final Pojo pojo = Pojos.genericSample().withBuildMethod(Optional.of(buildMethod));
    final String output =
        generator.generate(pojo, PojoSettings.defaultSettings(), Writer.createDefault()).asString();

    expect.toMatchSnapshot(output);
  }

  @Test
  @SnapshotName("sampleAndConstructorWithOptionalArgument")
  void
      buildMethod_when_generatorUsedWithSamplePojoAndConstructorWithOptionalArgument_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final Writer writer =
        generator.generate(
            Pojos.sampleWithConstructorWithOptionalArgument(),
            PojoSettings.defaultSettings(),
            Writer.createDefault());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }

  @Test
  @SnapshotName("sampleWithFactoryMethod")
  void buildMethod_when_sampleWithFactoryMethod_then_correctOutput() {
    final Generator<Pojo, PojoSettings> generator = buildMethod();
    final Writer writer =
        generator.generate(
            Pojos.factoryMethodSample(), PojoSettings.defaultSettings(), Writer.createDefault());

    expect.toMatchSnapshot(writerSnapshot(writer));
  }
}
