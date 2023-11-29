package org.stilab.parser.mapper;

import org.json.simple.JSONObject;

public class BlockPosition<T, U, W, Z, M, I, S> {
  private T start;
  private U end;
  private W content;
  private Z object;
  private M metrics;
  private I identifier;

  private S size;

  public BlockPosition(T start, U end, W content, Z object, I identifier, S size) {
      this.start   = start;
      this.end     = end;
      this.content = content;
      this.object  = object;
      this.identifier = identifier;
      this.size = size;
  }

  public void setMetrics(M metrics) { this.metrics = metrics; }

  public T getStart() { return start; }
  public U getEnd() { return end; }
  public W getContent() { return content; }
  public Z getObject() { return object; }
  public M metrics() { return metrics; }
  public I getIdentifier() { return this.identifier; }
  public S getSize() { return this.size; }

  public JSONObject toJson() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("block_identifier", getIdentifier());
    jsonObject.put("start", getStart());
    jsonObject.put("end", getEnd());
    jsonObject.put("size", getSize());
    return jsonObject;
  }
}
