package io.github.muehmar.pojoextension;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class Resources {
  private Resources() {}

  public static String readString(String resource) {
    final InputStream stream = Resources.class.getResourceAsStream(resource);
    return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  public static String readString(Path resource) {
    return readString(resource.toString());
  }
}
