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

  private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
    .getLog(MP3TableCellCenderer.class);

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
    Object val = table.getValueAt(row, 0);
    MP3File mp3File = null;
    Category mp3Cat = null;
    if (val instanceof MP3File) {
      mp3File = (MP3File)val;
      mp3Cat = mp3File.getCategoryByIndex(column - MP3TableModel.getIndexGeneralColumns());
      if (mp3Cat != null) {
        selected = mp3Cat.isMapped();
        // logger.debug("file " + mp3File.getFile().getName() + ", category: " + mp3Cat.getName() + " selected: "
        // + selected + ", row: " + row + ", column: " + column + ", CatIndex: " + mp3Cat.getIndex());
      } else {
        if (column != 3) {
          logger.error("Category is null: " + mp3File.getFile().getName() + ", index: " + column);
        }
      }
    } else {
      logger.error("not a mp3 file: " + val + ", in row: " + row);
    }
    JCheckBox cb = new JCheckBox();
    cb.setHorizontalAlignment(SwingConstants.CENTER);
    TableColumn col = table.getTableHeader().getColumnModel().getColumn(column);
    if (col != null && MP3TableModel.UNKNOWN.equals(col.getIdentifier())) {
      cb.setEnabled(false);
    } else {
      cb.setSelected(selected);
      setEnabled(true);
    }
    return cb;
  }
}
