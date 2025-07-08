package dev.eduzavarce.blackjack_api.contexts.shared.domain;

import java.util.HashMap;

public interface Monitoring {
  void incrementCounter(int times);

  void incrementGauge(int times);

  void decrementGauge(int times);

  void setGauge(int value);

  void observeHistogram(int value, HashMap<String, String> labels);
}
