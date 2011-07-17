/*
 * icon Systemhaus GmbH
 * www.icongmbh.de
 */
package de.kanwas.audio.mp3.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

import de.kanwas.audio.commons.Category;

/**
 * @author $Author$
 * @version $Revision$ ($Date$)
 */
public class CategoryListModel extends DefaultListModel {
  /** version number */
  public static final String VER = "$Revision$";

  private final Map<Category, JCheckBox> categories;

  public CategoryListModel(List<Category> categoryList) {
    this.categories = new HashMap<Category, JCheckBox>();
    for (Category c : categoryList) {
      this.categories.put(c, new JCheckBox());
    }
    setData();
  }

  /**
   * 
   */
  private void setData() {
    Object[] model = null;
    for (Entry<Category, JCheckBox> c : categories.entrySet()) {
      model = new Object[2];
      model[0] = c.getValue();
      model[1] = c.getKey();
      this.addElement(model);
    }
  }

  public List<Category> getSelectedCategories() {
    List<Category> selectedCategoryList = new ArrayList<Category>(categories.size());
    for (int i = 0; i < getSize(); i++) {
      Object[] obj = (Object[])getElementAt(i);
      if (obj[0] != null && obj[0] instanceof JCheckBox && obj[1] instanceof Category) {
        JCheckBox chkBox = (JCheckBox)obj[0];
        if (chkBox != null && chkBox.isSelected()) {
          Category c = (Category)obj[1];          selectedCategoryList.add(c);
        }
      }
    }
    return selectedCategoryList;
  }

  // /**
  // * @param row
  // */
  // public void updateElement(Category category) {
  // boolean selected = false;
  // if (this.categories.containsKey(category)) {
  // selected = this.categories.get(category);
  // }
  // this.categories.put(category, !selected);
  // }

}
