package fs;

import fs.exceptions.CanNotReadFileException;
import fs.exceptions.ClosedFileException;
import java.util.function.Consumer;

public class File {
  private LowLevelFileSystem fs;
  private int fd;
  private boolean closed;

  public File(LowLevelFileSystem fs, int fd) {
    this.fs = fs;
    this.fd = fd;
    this.closed = false;
  }

  public void close() {
    fs.closeFile(fd);
    this.closed = true;
  }

  public boolean isClosed() {
    return this.closed;
  }

  public int getDescriptor() {
    return fd;
  }

  public int syncRead(Buffer buffer) {
    this.checkClosed();
    int bytesLeidos = fs.syncReadFile(fd, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
    if (bytesLeidos == -1) {
      throw new CanNotReadFileException("No puede leerse");
    }
    buffer.limit(bytesLeidos);
    return bytesLeidos;
  }

  public void syncWrite(Buffer buffer) {
    this.checkClosed();
    fs.syncWriteFile(this.fd, buffer.getBytes(), buffer.getStart(), buffer.getEnd());
  }

  public void asyncRead(Consumer<Buffer> callback, int maxLength) {
    this.checkClosed();
    Buffer buffer = new Buffer(maxLength);
    fs.asyncReadFile(fd,
        buffer.getBytes(),
        buffer.getStart(),
        buffer.getEnd(),
        readBytes -> {
          buffer.limit(readBytes);
          callback.accept(buffer);
        });
  }

  public void asyncWrite(Consumer<Buffer> callback, Buffer buffer) {
    this.checkClosed();
    fs.asyncWriteFile(fd,
        buffer.getBytes(),
        buffer.getStart(),
        buffer.getEnd(),
        () ->  callback.accept(buffer)
    );
  }

  private void checkClosed() {
    if (isClosed()) {
      throw new ClosedFileException("El archivo esta cerrado");
    }
  }
}
