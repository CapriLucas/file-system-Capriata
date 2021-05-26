package fs;

import java.util.function.Consumer;

public class OpenedFile {

   private int fd;
   private LowLevelFileSystem lowLevelFileSystem;

   public OpenedFile(int fd, LowLevelFileSystem lowLevelFileSystem) {
      this.fd = fd;
      this.lowLevelFileSystem = lowLevelFileSystem;
   }


   public void close() {
      lowLevelFileSystem.closeFile(fd);
   }


   public void read(Buffer buffer) {
        int readBytes = lowLevelFileSystem.syncReadFile(fd,
              buffer.getBytes(), 
              buffer.getStart(), 
              buffer.getEnd());
        buffer.limit(readBytes);
   }


   public void asyncRead(Consumer<Buffer> callback) {
      Buffer buffer = new Buffer(100);
      lowLevelFileSystem.asyncReadFile(fd, 
            buffer.getBytes(), 
            buffer.getStart(),
            buffer.getEnd(), 
            readBytes -> {
               buffer.limit(readBytes);
               callback.accept(buffer);
            });
   }


}
