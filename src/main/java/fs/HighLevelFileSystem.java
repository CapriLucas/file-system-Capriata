package fs;

import fs.exceptions.CanNotOpenFileException;

public class HighLevelFileSystem {
  LowLevelFileSystem fs;

  public HighLevelFileSystem(LowLevelFileSystem lowLevelFileSystem) {
    fs = lowLevelFileSystem;
  }

  public File openFile(String path) {
    int fd = fs.openFile(path);
    if (fd < 0) {
      throw new CanNotOpenFileException("El archivo no se pudo abrir, fd: " + fd);
    }
    return new File(this.fs,fd);
  }
}
