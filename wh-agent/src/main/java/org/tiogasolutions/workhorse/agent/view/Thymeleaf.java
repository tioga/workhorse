package org.tiogasolutions.workhorse.agent.view;

import java.util.HashMap;
import java.util.Map;

public class Thymeleaf {

  private final String view;
  private final Map<String, Object>  variables = new HashMap<>();

  public Thymeleaf(String view) {
    this(view, null);
  }

  public Thymeleaf(String view, Object model) {
    this.view = view;
    if (model != null){
      this.variables.put("it", model);
    }
  }

  public String getView() {
    return view;
  }

  public Map<String, ?> getVariables() {
    return variables;
  }
}
