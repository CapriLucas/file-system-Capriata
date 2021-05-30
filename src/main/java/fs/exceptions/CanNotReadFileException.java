package fs.exceptions;

public class CanNotReadFileException extends RuntimeException {
  public CanNotReadFileException(String message) {
    super(message);
  }
}
