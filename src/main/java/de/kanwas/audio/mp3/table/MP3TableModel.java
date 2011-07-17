/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.table.DefaultTableModel;

import de.kanwas.audio.commons.Category;
import de.kanwas.audio.commons.MP3Content;
import de.kanwas.audio.commons.MP3File;
import de.kanwas.audio.commons.MP3Folder;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3TableModel extends DefaultTableModel {

  /**   */
  private static final int _MP3TABLE_HEADER_GENERAL_COLUMNS = 1;

  /** version number */
  public static final String VER = "$Revision$";

  private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
    .getLog(MP3TableModel.class);

  /**
   * 
   */
  public static final String UNKNOWN = "<unknown>";

  private List<MP3File> originalFiles;

  private List<MP3File> files;

  public MP3TableModel(List<Category> categories) {
    int maxIndex = 0;
    for (Category category : categories) {
      if (maxIndex < category.getIndex()) {
        maxIndex = category.getIndex();
      }
    }
    maxIndex += _MP3TABLE_HEADER_GENERAL_COLUMNS;
    String[] cats = new String[maxIndex + 1];
    cats[0] = "Datei";
    for (Category c : categories) {
      cats[c.getIndex() + _MP3TABLE_HEADER_GENERAL_COLUMNS] = c.getName();
    }
    for (int i = 0; i < cats.length; i++) {
      if (cats[i] == null) {
        cats[i] = UNKNOWN;
      }
    }
    setColumnIdentifiers(cats);
    this.files = null;
    this.originalFiles = null;
  }

  /**
   * @param c
   * @param mp3Collectionlist
   */
  public void setMP3Data(MP3Content c, List<MP3File> mp3Collectionlist) {
    if (c instanceof MP3Folder) {
      // handle a folder
      files = ((MP3Folder)c).getMp3Files();
    } else if (c instanceof MP3File) {
      // handle a single file
      files = new ArrayList<MP3File>();
      files.add((MP3File)c);
    }
    if (files != null) {
      this.originalFiles = files;
      checkDBFiles(mp3Collectionlist);
      setData(false);
    }
  }

  /**
   * @param mp3CollectionList
   */
  private void checkDBFiles(List<MP3File> mp3CollectionList) {
    for (MP3File file : mp3CollectionList) {
      for (MP3File tableFile : files) {
        if (tableFile.isEqualTo(file)) {
          this.files.set(this.files.indexOf(tableFile), file);
          break;
        }
      }
    }
  }

  private void setData(boolean originalFiles) {
    clearTable();
    List<MP3File> tmpFiles = null;
    if (originalFiles) {
      tmpFiles = this.originalFiles;
    } else {
      tmpFiles = this.files;
    }

    Object[] obj = new Object[getColumnCount()];
    for (MP3File mp3File : tmpFiles) {
      obj[0] = mp3File;
      for (Category category : mp3File.getCategories()) {
        obj[getCategoryIndex(category)] = category.isMapped();
      }
      addRow(obj);
    }
  }

  private int getCategoryIndex(Category c) {
    return c.getIndex() + _MP3TABLE_HEADER_GENERAL_COLUMNS;
  }

  /**
   * 
   */
  private void clearTable() {
    for (int i = getRowCount() - 1; i >= 0; i--) {
      this.removeRow(i);
    }
  }

  /**
   * @return true if something has been changed, else false
   */
  public boolean isDirty() {
    if (getDirtyMP3File() == null) {
      return false;
    }
    return true;
  }

  private Map<MP3File, List<Category>> getDirtyMP3File() {
    Object o = null;
    MP3File mp3File = null;
    Map<MP3File, List<Category>> dirtyObjects = new HashMap<MP3File, List<Category>>();
    List<Category> dirtyCategories = null;
    for (int i = 0; i < getRowCount(); i++) {
      o = getValueAt(i, 0);
      if (o instanceof MP3File) {
        mp3File = (MP3File)o;
        for (Category c : mp3File.getCategories()) {
          if (c.isDirty()) {
            logger.debug("Add category " + c + " as dirty");
            if (dirtyCategories == null) {
              dirtyCategories = new ArrayList<Category>();
            }
            dirtyCategories.add(c);
          }
        }
        if (dirtyCategories != null) {
          dirtyObjects.put(mp3File, dirtyCategories);
        }
      }
    }
    if (dirtyObjects.size() == 0) {
      return null;
    }
    return dirtyObjects;
  }

  /**
   * @return number of general columns
   */
  public static int getIndexGeneralColumns() {
    return _MP3TABLE_HEADER_GENERAL_COLUMNS;
  }

  /**
   * @return the current content of {@link MP3File}
   */
  public List<MP3File> savingMP3Files() {
    Map<MP3File, List<Category>> dirtyObjects = getDirtyMP3File();
    if (dirtyObjects == null) {
      return this.files;
    }
    boolean selected = false;
    Category cat = null;
    for (Entry<MP3File, List<Category>> file : dirtyObjects.entrySet()) {
      for (Category c : file.getValue()) {
        cat = file.getKey().getCategoryByIndex(c.getIndex());
        selected = !cat.isMapped();
        cat.setMapped(selected);
        file.getKey().setCategory(cat);
      }
    }
    return this.files;
  }

  /**
   * resets the currents data with the original data
   */
  public void resetMP3Data() {
    this.setData(true);
  }
}
