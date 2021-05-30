package fs.exceptions;

public class ClosedFileException extends RuntimeException {
  public ClosedFileException(String msg) {
    super(msg);
  }
}
