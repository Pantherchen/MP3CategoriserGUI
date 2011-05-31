/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.kanwas.audio.commons.Category;
import de.kanwas.audio.commons.MP3Content;
import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.commons.MP3Folder;
import de.kanwas.audio.io.CategoryReader;
import de.kanwas.audio.io.MP3FileDataSetHandler;
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

  private MP3FileDataSetHandler dbHandler = null;

  private CategoryReader categoryReader = null;

  private MP3FileReader mp3FileReader = null;

  private List<MP3File> mp3Files;

  private List<Category> categories;

  private List<MP3Content> mp3Collection;

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
    this.categoryReader = new CategoryReader(categoryPath);
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

  public List<MP3Content> getMP3Collection() {
    if (mp3Collection == null) {
      this.mp3Collection = this.mp3FileReader.getMP3Files();
      MP3Folder mp3Folder = null;
      for (MP3Content mp3Content : mp3Collection) {
        if (mp3Content instanceof MP3Folder) {
          mp3Folder = (MP3Folder)mp3Content;
          for (MP3File file : mp3Folder.getMp3Files()) {
            for (Category allCat : this.getCategories()) {
              if (file.getCategory(allCat.getName()) == null) {
                file.setCategory(new Category(allCat.getName(), allCat.getIndex()));
              }
            }
          }
        }
      }

    }
    return mp3Collection;
  }

  public String getDbPath() {
    return this.dbPath;
  }

  public void setDbPath(String dbPath) {
    this.dbPath = dbPath;
    this.dbHandler = new MP3FileDataSetHandler(dbPath, getCategories());
  }

  public boolean writeFiles(List<MP3File> files) {
    if (files == null) {
      return false;
    }
    mergeFiles(files);
    return this.dbHandler.write2DB(files);
  }

  public List<Category> getCategories() {
    if (this.categories == null) {
      this.categories = this.categoryReader.getCategories();
    }
    return this.categories;
  }

  public List<MP3File> readFiles() {
    if (this.mp3Files == null) {
      this.mp3Files = this.dbHandler.readFilesFromDB();
    }
    return this.mp3Files;
  }

  private void mergeFiles(List<MP3File> files) {
    for (MP3File file : files) {
      if (!this.mp3Files.contains(file)) {
        this.mp3Files.add(file);
      }
    }
  }

  /**
   * @param category
   */
  public void exportPlaylist(Category category, String playlistPath) {
    List<MP3File> files2Export = retrieveMP3FilesbyCategory(category);
    File playlistFolder = new File(playlistPath);
    if (playlistFolder != null && playlistFolder.canWrite()) {
      for (MP3File mp3file : files2Export) {
        // playlistFolder.
        // mp3file.getFile().ge
      }
    }
  }

  private List<MP3File> retrieveMP3FilesbyCategory(Category category) {
    List<MP3File> files2Export = new ArrayList<MP3File>();
    Category cat = null;
    for (MP3File file : this.mp3Files) {
      cat = file.getCategory(category.getName());
      if (cat != null && cat.isMapped()) {
        if (!files2Export.contains(file)) {
          files2Export.add(file);
        }
      }
    }
    return files2Export;
  }
}
