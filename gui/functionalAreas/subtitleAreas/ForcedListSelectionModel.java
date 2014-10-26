package gui.functionalAreas.subtitleAreas;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

/**
 * Method from http://stackoverflow.com/questions/18309113/jtable-how-to-force-user-to-select-exactly-one-row
 * Used in conjunction with TableModel.
 * @author fsta657
 *
 */

@SuppressWarnings("serial")
public class ForcedListSelectionModel extends DefaultListSelectionModel {

    public ForcedListSelectionModel () {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    }
}
