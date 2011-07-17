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
import de.kanwas.audio.io.MP3FileHandler;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3DataBroker {
  /** version number */
  public static final String VER = "$Revision$";

  private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
    .getLog(MP3DataBroker.class);

  private String categoryPath;

  private String mp3Path;

  private String dbPath;

  private MP3FileDataSetHandler dbHandler = null;

  private CategoryReader categoryReader = null;

  private MP3FileHandler mp3FileHandler = null;

  private List<Category> categories;

  // read from databasefile
  private List<MP3File> mp3Files;

  // shown in the tree
  private List<MP3Content> mp3Collection;

  public MP3DataBroker(String categoryPath, String mp3Path, String dbPath) {
    this.setMP3Path(mp3Path);
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

  public String getMP3Path() {
    return this.mp3Path;
  }

  public void setMP3Path(String mp3Path) {
    this.mp3Path = mp3Path;
    this.mp3FileHandler = new MP3FileHandler(mp3Path);
    mp3FileHandler.readFiles();
  }

  public List<MP3Content> getMP3Collection() {
    if (mp3Collection == null) {
      this.mp3Collection = this.mp3FileHandler.getMP3Files();
      MP3Folder mp3Folder = null;
      // fill List of MP3Files
      this.mp3Files = new ArrayList<MP3File>();
      for (MP3Content mp3Content : mp3Collection) {
        if (mp3Content instanceof MP3Folder) {
          mp3Folder = (MP3Folder)mp3Content;
          for (MP3File file : mp3Folder.getMp3Files()) {
            if (!mp3Files.contains(file)) {
              this.mp3Files.add(file);
            }
          }
        }
      }
      readFiles();
      for (MP3File file : this.mp3Files) {
        for (Category allCat : this.getCategories()) {
          if (file.getCategory(allCat.getName()) == null) {
            file.setCategory(new Category(allCat.getName(), allCat.getIndex()));
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
    if (this.dbHandler.write2DB(this.mp3Files)) {
      for (MP3File f : this.mp3Files) {
        for (Category c : f.getCategories()) {
          c.setDirty(false);
        }
      }
      return true;
    }
    return false;
  }

  public List<Category> getCategories() {
    if (this.categories == null) {
      this.categories = this.categoryReader.getCategories();
    }
    return this.categories;
  }

  public List<MP3File> getMP3CollectionList() {
    if (this.mp3Files == null) {
      getMP3Collection();
    }
    return this.mp3Files;
  }

  private void readFiles() {
    List<MP3File> files = this.dbHandler.readFilesFromDB();
    mergeFiles(files);
  }

  private void mergeFiles(List<MP3File> files) {
    MP3File foundfile = null;
    for (MP3File f : files) {
      foundfile = containsObject(f);
      if (foundfile != null) {
        logger.debug("found file: " + f.getFile().getName());
        this.mp3Files.set(this.mp3Files.indexOf(foundfile), f);
      }
    }
  }

  private MP3File containsObject(MP3File file) {
    for (MP3File f : this.mp3Files) {
      if (f.isEqualTo(file)) {
        return f;
      }
    }
    return null;
  }

  /**
   * @param exportcategories
   */
  public void exportPlaylist(List<Category> exportcategories, String playlistPath) {
    List<MP3File> files2Export = retrieveMP3FilesbyCategory(exportcategories);
    File playlistFolder = new File(playlistPath);
    if (playlistFolder != null && playlistFolder.canWrite()) {
      this.mp3FileHandler.createPlaylist(files2Export, playlistFolder);
    }
  }

  private List<MP3File> retrieveMP3FilesbyCategory(List<Category> mp3Categories) {
    List<MP3File> files2Export = new ArrayList<MP3File>();
    Category cat = null;
    for (MP3File file : this.mp3Files) {
      for (Category c : mp3Categories) {
        cat = file.getCategory(c.getName());
        if (cat != null && cat.isMapped()) {
          if (!files2Export.contains(file)) {
            files2Export.add(file);
          }
        }
      }
    }
    return files2Export;
  }

  /**
   * 
   */
  public void clearCollection() {
    this.mp3Collection = null;
  }
}
