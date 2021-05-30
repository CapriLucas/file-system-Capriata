package fs;

public class Buffer {
  byte[] bufferBytes;
  int bufferStart;
  int bufferEnd;

  public Buffer(int size) {
    this.bufferBytes = new byte[size];
    this.bufferStart = 0;
    this.bufferEnd = size - 1;
  }

  public byte[] getBytes() {
    return bufferBytes;
  }

  public int getStart() {
    return bufferStart;
  }

  public int getEnd() {
    return bufferEnd;
  }

  public void limit(int offset) {
    this.bufferEnd = bufferStart + offset - 1;
  }

  public int getCurrentSize() {
    return this.bufferEnd + 1;
  }

  public int getMaxSize() {
    return this.bufferBytes.length;
  }

}
