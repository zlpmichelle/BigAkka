package hadoopwc;

import java.io.Serializable;

import org.apache.hadoop.io.IntWritable;

public class MiddleResult implements Serializable {

  private String word;
  private IntWritable number;

  public MiddleResult(String word, IntWritable number) {
    this.setWord(word);
    this.setNoOfInstances(number);
  }

  public void setWord(String word) {
    this.word = word;
  }

  public String getWord() {
    return word;
  }

  public void setNoOfInstances(IntWritable number) {
    this.number = number;
  }

  public IntWritable getNoOfInstances() {
    return number;
  }

  public String toString() {
    return word + ":" + number;
  }

}
