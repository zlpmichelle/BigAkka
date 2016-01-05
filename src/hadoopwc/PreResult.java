package hadoopwc;

import java.io.Serializable;

public class PreResult implements Serializable {

  private Integer key;

  public Integer getKey() {
    return key;
  }

  public void setKey(Integer key) {
    this.key = key;
  }

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public PreResult(Integer key, String value) {
    this.setKey(key);
    this.setValue(value);
  }

  public String toString() {
    return key + ":" + value;
  }

}
