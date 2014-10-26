package gui.functionalAreas.subtitleAreas;

import java.util.List;

import javax.swing.table.AbstractTableModel;
// This table model code is an edited version of the code @ http://www.codejava.net/java-se/swing/editable-jtable-example
public class TableModel extends AbstractTableModel
{
    private final List<Subtitle> subsList;
     
    private final String[] columnNames = new String[] {
            "Start time", "End time", "Text to display"
    };
    private final Class[] columnClass = new Class[] {
        String.class, String.class, String.class
    };
 
    public TableModel(List<Subtitle> subsList)
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
        Subtitle row = subsList.get(rowIndex);
        if(0 == columnIndex) {
            return row.getStart();
        }
        else if(1 == columnIndex) {
            return row.getEnd();
        }
        else if(2 == columnIndex) {
            return row.getText();
        }
        return null;
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return true;
    }
 
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        Subtitle row = subsList.get(rowIndex);
        if(0 == columnIndex) {
        	 row.setStart((String) aValue, row, subsList, rowIndex);
        }
        else if(1 == columnIndex) {
            row.setEnd((String) aValue, row, subsList,rowIndex);
        }
        else if(2 == columnIndex) {
            row.setText((String) aValue);
        }
    }
}