/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import de.kanwas.audio.commons.Category;
import de.kanwas.audio.commons.MP3File;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class MP3CategoryCellEditor extends AbstractCellEditor implements TableCellEditor {
  /** version number */
  public static final String VER = "$Revision$";

  private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
    .getLog(MP3CategoryCellEditor.class);

  private final JCheckBox checkbox;

  private int currentRow;

  private int currentColumn;

  private String defaultValue = null;

  private JTable mp3Table;

  public MP3CategoryCellEditor() {
    this.checkbox = new JCheckBox();
    this.checkbox.setHorizontalAlignment(SwingConstants.CENTER);
  }

  @Override
  public Object getCellEditorValue() {
    return this.checkbox.isSelected();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

    if (column < MP3TableModel.getIndexGeneralColumns()) {
      return null;
    }
    TableColumn col = table.getTableHeader().getColumnModel().getColumn(column);
    if (col != null && MP3TableModel.UNKNOWN.equals(col.getIdentifier())) {
      return null;
    }
    this.mp3Table = table;
    this.currentRow = row;
    this.currentColumn = column;
    if (isSelected) {
      Color c = table.getBackground();
      Color selectedColor = c.darker();
      table.setBackground(selectedColor);
    }
    this.checkbox.addItemListener(new ItemListener(){

      @Override
      public void itemStateChanged(ItemEvent e) {
        int state = e.getStateChange();
        boolean selected = false;
        if (state == ItemEvent.SELECTED) {
          selected = true;
        }
        if (defaultValue == null) {
          defaultValue = String.valueOf(!selected);
        }
        setCategoryValue(selected);
      }
    });

    this.checkbox.setSelected((Boolean)value);
    return this.checkbox;
  }

  private void setCategoryValue(boolean selected) {
    Object file = mp3Table.getValueAt(currentRow, 0);
    if (file instanceof MP3File) {
      MP3File mp3File = (MP3File)file;
      Category mp3category = mp3File.getCategoryByIndex(currentColumn - MP3TableModel.getIndexGeneralColumns());
      if (mp3category != null) {
        logger.debug("set file: " + mp3File.getFile().getName() + " category " + mp3category.getName() + " to "
                     + selected);
        mp3category.setMapped(selected);
        if (defaultValue != null && mp3category.isDirty() && Boolean.getBoolean(defaultValue) == selected) {
          mp3category.setDirty(false);
        }
        if (!mp3category.isDirty()) {
          mp3category.setDirty(true);
        }
      } else {
        logger.error("Category not found: " + mp3File.getFile().getName());
      }
    } else {
      logger.error("not a mp3file: " + file + ", index: " + currentRow);
    }

  }
}
