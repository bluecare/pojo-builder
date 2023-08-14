package io.github.muehmar.pojobuilder.processor;

import ch.bluecare.commons.data.PList;
import io.github.muehmar.pojobuilder.annotations.OptionalDetection;
import lombok.Value;

@Value
public class DetectionSettings {
  PList<OptionalDetection> optionalDetections;
}
