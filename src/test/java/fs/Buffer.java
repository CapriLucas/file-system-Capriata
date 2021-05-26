package fs;

public class Buffer {
   byte[] bytes;
   int start;
   int end;

   public Buffer(int size) {
      start = 0;
      end = size - 1;
      bytes = new byte[size];
   }

   public byte[] getBytes() {
      return bytes;
   }

   public int getStart() {
      return start;
   }

   public int getEnd() {
      return end;
   }

   public void limit(int offset) {
        end = start + offset - 1;
   }

}
