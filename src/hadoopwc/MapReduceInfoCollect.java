package hadoopwc;

import java.io.Serializable;

public class MapReduceInfoCollect implements Serializable {

  private String filePath;

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  private int recordNum;

  public int getRecordNum() {
    return recordNum;
  }


  public void setRecordNum(int recordNum) {
    this.recordNum = recordNum;
  }


  public MapReduceInfoCollect(String filePath, int recordNum) {
    this.setFilePath(filePath);
    this.setRecordNum(recordNum);
  }


  public String toString() {
    return "FilePath is : " + filePath + "; RecordNumber is : " + recordNum;
  }

}
