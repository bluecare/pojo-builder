package io.github.muehmar.pojoextension.generator.data;

import io.github.muehmar.pojoextension.annotations.PojoExtension;

@PojoExtension
@SuppressWarnings("java:S2160")
public class PojoSettings extends PojoSettingsExtension {
  private final boolean disableSafeBuilder;

  public PojoSettings(boolean disableSafeBuilder) {
    this.disableSafeBuilder = disableSafeBuilder;
  }

  public static PojoSettings defaultSettings() {
    return new PojoSettings(false);
  }

  public boolean isDisableSafeBuilder() {
    return disableSafeBuilder;
  }

  public boolean isEnableSafeBuilder() {
    return !isDisableSafeBuilder();
  }
}
