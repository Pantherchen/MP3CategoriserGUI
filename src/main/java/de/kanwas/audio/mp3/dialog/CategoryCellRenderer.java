/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.dialog;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import de.kanwas.audio.commons.Category;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class CategoryCellRenderer extends JCheckBox implements ListCellRenderer {
  /** version number */
  public static final String VER = "$Revision$";

  /*
   * (non-Javadoc)
   * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean,
   * boolean)
   */
  @Override
  public Component getListCellRendererComponent(JList list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus) {
    if (value instanceof Object[]) {
      Object[] object = (Object[])value;
      Category category = (Category)object[1];
      JCheckBox checkBox = (JCheckBox)object[0];
      if (checkBox != null) {
        this.setSelected(checkBox.isSelected());
      }
      this.setText(category.getName());
    }
    return this;
  }

}
