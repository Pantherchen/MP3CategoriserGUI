/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.table;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import de.kanwas.audio.commons.Category;
import de.kanwas.audio.commons.MP3File;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3TableCellCenderer extends DefaultTableCellRenderer implements TableCellRenderer {
  /** version number */
  public static final String VER = "$Revision$";

  @Override
  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column) {

    if (column < MP3TableModel.getIndexGeneralColumns()) {
      String name = null;
      String path = null;
      if (value instanceof MP3File) {
        MP3File file = (MP3File)value;
        name = file.getFile().getName();
        path = file.getFile().getPath();
      }
      if (value instanceof String) {
        name = (String)value;
        path = "";
      }
      JLabel textField = new JLabel(name);
      textField.setToolTipText(path);
      textField.setFocusable(false);
      return textField;
    }
    boolean selected = false;
    Object val = table.getValueAt(row, column);
    if (val instanceof Category) {
      Category category = (Category)val;
      selected = category.isMapped();
    } else if (val instanceof Boolean) {
      selected = (Boolean)val;
    }
    JCheckBox cb = new JCheckBox();
    cb.setHorizontalAlignment(SwingConstants.CENTER);
    cb.setSelected(selected);
    TableColumn col = table.getTableHeader().getColumnModel().getColumn(column);
    if (col != null && MP3TableModel.UNKNOWN.equals(col.getIdentifier())) {
      cb.setEnabled(false);
    } else {
      setEnabled(true);
    }
    return cb;
  }
}
