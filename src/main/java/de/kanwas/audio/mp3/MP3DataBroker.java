/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3;

import java.util.List;

import de.kanwas.audio.commons.Category;
import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.io.CategoryReader2;
import de.kanwas.audio.io.MP3FileDataSetWriter;
import de.kanwas.audio.io.MP3FileReader;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3DataBroker {
  /** version number */
  public static final String VER = "$Revision$";

  private String categoryPath;

  private String mp3Path;

  private String dbPath;

  private MP3FileDataSetWriter writer = null;

  private CategoryReader2 categoryReader = null;

  private MP3FileReader mp3FileReader = null;

  public MP3DataBroker(String categoryPath, String mp3Path, String dbPath) {
    this.setMp3Path(mp3Path);
    this.setCategoryPath(categoryPath);
    this.setDbPath(dbPath);
  }

  public String getCategoryPath() {
    return this.categoryPath;
  }

  public void setCategoryPath(String categoryPath) {
    this.categoryPath = categoryPath;
    this.categoryReader = new CategoryReader2(categoryPath);
    this.categoryReader.readCategories();

  }

  public String getMp3Path() {
    return this.mp3Path;
  }

  public void setMp3Path(String mp3Path) {
    this.mp3Path = mp3Path;
    this.mp3FileReader = new MP3FileReader(mp3Path);
    mp3FileReader.readFiles();
  }

  public List<MP3File> getMP3Files() {
    return this.mp3FileReader.getMP3Files();
  }

  public String getDbPath() {
    return this.dbPath;
  }

  public void setDbPath(String dbPath) {
    this.dbPath = dbPath;
    this.writer = new MP3FileDataSetWriter(mp3Path);
  }

  public void writeFiles(MP3File[] files) {
    this.writer.write2DB(files);
  }

  public List<Category> getCategories() {
    return this.categoryReader.getCategories();
  }
}
