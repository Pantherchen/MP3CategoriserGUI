/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.table;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import de.kanwas.audio.commons.Category;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3TableModel extends DefaultTableModel {
  /** version number */
  public static final String VER = "$Revision$";

  public MP3TableModel(List<Category> categories) {
    String[] cats = new String[categories.size()];
    for (int i = 0; i < categories.size(); i++) {
      cats[i] = categories.get(i).getName();
    }
    setColumnIdentifiers(cats);
  }
}
