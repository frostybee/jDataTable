/*

Copyright (c) 2013 by Sleiman Rabah

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

package org.nahoul.datagrid.ui.grid;

import org.nahoul.datagrid.core.Functor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Sleiman Rabah <sleiman.rabah@gmail.com>
 */
public class DgObjectModel extends DefaultTableModel {

    private static final Logger log = LoggerFactory.getLogger(DgObjectModel.class);
    private static final long serialVersionUID = 240L;
    private transient ArrayList<Object> objects = new ArrayList<Object>();
    private transient List<String> headers = new ArrayList<String>();
    private transient ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    private transient ArrayList<Functor> readFunctors = new ArrayList<Functor>();
    private transient ArrayList<Functor> writeFunctors = new ArrayList<Functor>();
    private transient Class<?> objectClass = null; // if provided
    private transient boolean cellEditable = false;

    /**
     * The ObjectTableModel is a TableModel whose rows are objects; columns are
     * defined as Functors on the object.
     *
     * @param headers - Column names
     * @param _objClass - Object class that will be used
     * @param readFunctors - used to get the values
     * @param writeFunctors - used to set the values
     * @param editorClasses - class for each column
     */
    public DgObjectModel(String[] headers, Class<?> _objClass, Functor[] readFunctors, Functor[] writeFunctors, Class<?>[] editorClasses) {
        this(headers, readFunctors, writeFunctors, editorClasses);
        this.objectClass = _objClass;
    }

    /**
     * The ObjectTableModel is a TableModel whose rows are objects; columns are
     * defined as Functors on the object.
     *
     * @param headers - Column names
     * @param _objClass - Object class that will be used
     * @param readFunctors - used to get the values
     * @param writeFunctors - used to set the values
     * @param editorClasses - class for each column
     * @param cellEditable - if cell must editable (false to allow double click
     * on cell)
     */
    public DgObjectModel(String[] headers, Class<?> _objClass, Functor[] readFunctors,
            Functor[] writeFunctors, Class<?>[] editorClasses, boolean cellEditable) {
        this(headers, readFunctors, writeFunctors, editorClasses);
        this.objectClass = _objClass;
        this.cellEditable = cellEditable;
    }

    /**
     * The ObjectTableModel is a TableModel whose rows are objects; columns are
     * defined as Functors on the object.
     *
     * @param headers - Column names
     * @param readFunctors - used to get the values
     * @param writeFunctors - used to set the values
     * @param editorClasses - class for each column
     */
    public DgObjectModel(String[] headers, Functor[] readFunctors, Functor[] writeFunctors, Class<?>[] editorClasses) {
        this.headers.addAll(Arrays.asList(headers));
        this.classes.addAll(Arrays.asList(editorClasses));
        this.readFunctors = new ArrayList<Functor>(Arrays.asList(readFunctors));
        this.writeFunctors = new ArrayList<Functor>(Arrays.asList(writeFunctors));

        int numHeaders = headers.length;

        int numClasses = classes.size();
        if (numClasses != numHeaders) {
//            log.warn("Header count=" + numHeaders + " but classes count=" + numClasses);
        }

        // Functor count = 0 is handled specially
        int numWrite = writeFunctors.length;
        if (numWrite > 0 && numWrite != numHeaders) {
            //log.warn("Header count=" + numHeaders + " but writeFunctor count=" + numWrite);
        }

        int numRead = readFunctors.length;
        if (numRead > 0 && numRead != numHeaders) {
            //log.warn("Header count=" + numHeaders + " but readFunctor count=" + numRead);
        }
    }

    private Object readResolve() {
        objects = new ArrayList<Object>();
        headers = new ArrayList<String>();
        classes = new ArrayList<Class<?>>();
        readFunctors = new ArrayList<Functor>();
        writeFunctors = new ArrayList<Functor>();
        return this;
    }

    public Iterator<?> iterator() {
        return objects.iterator();
    }

    public void clearData() {
        int size = getRowCount();
        objects.clear();

        super.fireTableRowsDeleted(0, size);
    }

    public void addRow(Object value) {
//        log.debug("Adding row value: " + value);
        if (objectClass != null) {
            final Class<?> valueClass = value.getClass();
            if (!objectClass.isAssignableFrom(valueClass)) {
                throw new IllegalArgumentException("Trying to add class: " + valueClass.getName()
                        + "; expecting class: " + objectClass.getName());
            }
        }
        objects.add(value);
        super.fireTableRowsInserted(objects.size() - 1, objects.size());
    }

    public void insertRow(Object value, int index) {
        objects.add(index, value);
        super.fireTableRowsInserted(index, index + 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getColumnCount() {
        return headers.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getColumnName(int col) {
        return headers.get(col);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRowCount() {
        if (objects == null) {
            return 0;
        }
        return objects.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getValueAt(int row, int col) {
//        log.debug("Getting row value");
        Object value = objects.get(row);
        if (headers.size() == 1 && col >= readFunctors.size()) {
            return value;
        }
        Functor getMethod = readFunctors.get(col);
        if (getMethod != null && value != null) {
            return getMethod.invoke(value);
        }
        return null;
    }

    public Object getValueAt(int row) {
//        log.debug("Getting row value");
        Object value = objects.get(row);

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return cellEditable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void moveRow(int start, int end, int to) {
        List<Object> subList = new ArrayList<Object>(objects.subList(start, end));
        for (int x = end - 1; x >= start; x--) {
            objects.remove(x);
        }
        objects.addAll(to, subList);
        super.fireTableChanged(new TableModelEvent(this));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeRow(int row) {
        objects.remove(row);
        super.fireTableRowsDeleted(row, row);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAt(Object cellValue, int row, int col) {
        if (row < objects.size()) {
            Object value = objects.get(row);
            if (col < writeFunctors.size()) {
                Functor setMethod = writeFunctors.get(col);
                if (setMethod != null) {
                    setMethod.invoke(value, new Object[]{cellValue});
                    super.fireTableDataChanged();
                }
            } else if (headers.size() == 1) {
                objects.set(row, cellValue);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getColumnClass(int arg0) {
        return classes.get(arg0);
    }

    /**
     * Check all registered functors.
     * <p>
     * <b>** only for use in unit test code **</b>
     * </p>
     *
     * @param _value - an instance of the table model row data item (if null,
     * use the class passed to the constructor).
     *
     * @param caller - class of caller.
     *
     * @return false if at least one Functor cannot be found.
     */
    @SuppressWarnings("deprecation")
    public boolean checkFunctors(Object _value, Class<?> caller) {
        Object value;
        if (_value == null && objectClass != null) {
            try {
                value = objectClass.newInstance();
            } catch (InstantiationException e) {
                log.error("Cannot create instance of class " + objectClass.getName(), e);
                return false;
            } catch (IllegalAccessException e) {
                log.error("Cannot create instance of class " + objectClass.getName(), e);
                return false;
            }
        } else {
            value = _value;
        }
        boolean status = true;
        for (int i = 0; i < getColumnCount(); i++) {
            Functor setMethod = writeFunctors.get(i);
            if (setMethod != null) {
                if (!setMethod.checkMethod(value, getColumnClass(i))) {
                    status = false;
                    log.warn(caller.getName() + " is attempting to use nonexistent " + setMethod.toString());
                }
            }
            Functor getMethod = readFunctors.get(i);
            if (getMethod != null) {
                if (!getMethod.checkMethod(value)) {
                    status = false;
                    log.warn(caller.getName() + " is attempting to use nonexistent " + getMethod.toString());
                }
            }

        }
        return status;
    }

    public void resourceStatusChanged() {
        super.fireTableChanged(new TableModelEvent(this));
    }

    public Object getObjectList() { // used by TableEditor
        return objects;
    }

    public void setRows(Iterable<?> rows) { // used by TableEditor
        clearData();
        for (Object val : rows) {
            addRow(val);
        }
    }
}
