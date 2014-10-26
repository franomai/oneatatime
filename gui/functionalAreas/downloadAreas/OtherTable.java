package gui.functionalAreas.downloadAreas;

import java.util.List;

import javax.swing.table.AbstractTableModel;
// This table model code is an edited version of the code @ http://www.codejava.net/java-se/swing/editable-jtable-example
public class OtherTable extends AbstractTableModel
{
    private final List<Creation> subsList;
     
    private final String[] columnNames = new String[] {
            "Description", "Link"
    };
    private final Class[] columnClass = new Class[] {
        String.class, String.class
    };
 
    public OtherTable(List<Creation> subsList)
    {
        this.subsList = subsList;
    }
     
    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }
 
    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        return columnClass[columnIndex];
    }
 
    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount()
    {
        return subsList.size();
    }
 
    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Creation row = subsList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getDesc();
        }
        else if(1 == columnIndex) {
            return row.getLink();
        }
        return null;
    }
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
    	if (columnIndex == 1){
        return true;
    	}
    	return false;
    }
}