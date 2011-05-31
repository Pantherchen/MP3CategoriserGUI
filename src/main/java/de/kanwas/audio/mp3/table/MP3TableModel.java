/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.table;

import java.util.ArrayList;
import java.util.List;

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
   * @param list
   */
  public void setMP3Data(MP3Content c, List<MP3File> list) {
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
      checkDBFiles(list);
      setData(false);
    }
  }

  /**
   * @param list
   */
  private void checkDBFiles(List<MP3File> list) {
    for (MP3File file : list) {
      if (this.files.contains(file)) {
        this.files.set(this.files.indexOf(file), file);
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
        obj[getCategoryIndex(category)] = category;
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
    Object o = null;
    Object cat = null;
    MP3File mp3File = null;
    Category mp3Cat = null;
    Category category = null;
    for (int i = 0; i < getRowCount(); i++) {
      o = getValueAt(i, 0);
      if (o instanceof MP3File) {
        mp3File = (MP3File)o;
        for (int j = _MP3TABLE_HEADER_GENERAL_COLUMNS; j < getColumnCount(); j++) {
          cat = getValueAt(i, j);
          if (cat instanceof Category) {
            category = (Category)cat;
            mp3Cat = mp3File.getCategory(category.getName());
            if (category != null && mp3Cat == null || (mp3Cat != null && category.isMapped() != mp3Cat.isMapped())) {
              return true;
            }
          } else if (cat instanceof Boolean) {
            boolean selected = (Boolean)cat;
            category = mp3File.getCategoryByIndex(j);
            if (category != null && category.isMapped() != selected) {
              return true;
            }
          }
        }
      }
    }
    return false;
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
  public List<MP3File> getMP3Files() {
    return this.files;
  }

  /**
   * resets the currents data with the original data
   */
  public void resetMP3Data() {
    this.setData(true);
  }
}
