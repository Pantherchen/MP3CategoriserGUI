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

  private JCheckBox checkbox = new JCheckBox();

  public MP3CategoryCellEditor() {
    this.checkbox = new JCheckBox();
    this.checkbox.setHorizontalAlignment(SwingConstants.CENTER);
  }

  /*
   * (non-Javadoc)
   * @see javax.swing.CellEditor#getCellEditorValue()
   */
  @Override
  public Object getCellEditorValue() {
    return this.checkbox.isSelected();
  }

  /*
   * (non-Javadoc)
   * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean,
   * int, int)
   */
  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

    if (column < MP3TableModel.getIndexGeneralColumns()) {
      return null;
    }
    final JTable mp3Table = table;
    final int currow = row;
    final int curcol = column;
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
        MP3CategoryCellEditor.this.checkbox.setSelected(selected);
        Object file = mp3Table.getValueAt(currow, 0);
        Object cat = mp3Table.getValueAt(currow, curcol);
        if (file instanceof MP3File) {
          MP3File mp3File = (MP3File)file;
          Category mp3category = null;
          if (cat instanceof Category) {
            mp3category = (Category)cat;
            mp3category.setMapped(selected);
            mp3File.setCategory(mp3category);
          }
        }
      }
    });
    TableColumn col = table.getTableHeader().getColumnModel().getColumn(column);
    if (col != null && MP3TableModel.UNKNOWN.equals(col.getIdentifier())) {
      this.checkbox.setEnabled(false);
    } else {
      this.checkbox.setEnabled(true);
    }
    return this.checkbox;
  }
}
