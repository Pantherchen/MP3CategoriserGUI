/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class Mp3CategoryCellEditor extends AbstractCellEditor implements TableCellEditor {
  /** version number */
  public static final String VER = "$Revision$";

  private JCheckBox checkbox = new JCheckBox();

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
    if(isSelected) {
      Color c = table.getBackground();    
      Color selectedColor = c.darker();
      table.setBackground(selectedColor);
    }
    if (value != null && value instanceof Boolean) {
      this.checkbox = new JCheckBox();
      this.checkbox.setSelected((Boolean)value);
    }
    return this.checkbox;
  }

}
