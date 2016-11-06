package com.connfa.model.database;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEntityDAO<ClassToSave extends AbstractEntity<ClassId>, ClassId> {

    protected abstract String getSearchCondition();

    /**
     * @param theId the id of object to search
     * @return list of arguments, generated by object id.
     */
    protected abstract String[] getSearchConditionArguments(ClassId theId);

    protected abstract String getTableName();

    public abstract String getDatabaseName();

    protected abstract ClassToSave newInstance();

    /**
     * This method should return list of columns, used to define unique object
     * in "contains" method
     *
     * @return
     */
    protected abstract String[] getKeyColumns();

    protected ILAPIDBFacade getFacade() {
        ILAPIDBFacade dbFacade = LAPIDBRegister.getInstance().lookup(getDatabaseName());

        return dbFacade;
    }

    public boolean containsData(ClassToSave theObj) {
        ILAPIDBFacade facade = getFacade();
        if (theObj.getId() == null) {
            return false;
        }

        return facade.containsRecord(getTableName(), getSearchCondition(),
                getSearchConditionArguments(theObj.getId()), getKeyColumns());
    }

    public int deleteData(ClassId theObj) {
        ILAPIDBFacade facade = getFacade();
        return facade.delete(getTableName(), getSearchCondition(),
                getSearchConditionArguments(theObj));
    }

    public int deleteDataSafe(ClassId theObj) {
        ILAPIDBFacade facade = getFacade();

        try {
            facade.open();

            return facade.delete(getTableName(), getSearchCondition(),
                    getSearchConditionArguments(theObj));
        } finally {
            facade.close();
        }
    }

    public int deleteAll() {
        ILAPIDBFacade facade = getFacade();
        return facade.delete(getTableName(), null, null);
    }

    public int deleteAllSafe() {
        ILAPIDBFacade facade = getFacade();

        try {
            facade.open();

            return facade.delete(getTableName(), null, null);
        } finally {
            facade.close();
        }
    }

    public List<ClassToSave> getDataSafe(final String theCondition, final String[] theArguments) {
        ILAPIDBFacade facade = getFacade();

        try {
            facade.open();
            return getData(theCondition, theArguments);

        } finally {
            facade.close();
        }

    }

    public List<ClassToSave> getDataSafe(ClassId theObj) {
        return getDataSafe(getSearchCondition(), getSearchConditionArguments(theObj));
    }

    public List<ClassToSave> getData(final String theCondition, final String[] theArguments) {
        List<ClassToSave> result = new LinkedList<ClassToSave>();

        ILAPIDBFacade facade = getFacade();
        Cursor cursor = null;
        cursor = facade.getAllRecords(getTableName(), null,
                theCondition, theArguments);

        boolean moved = cursor.moveToFirst();
        while (moved) {
            ClassToSave obj = newInstance();
            obj.initialize(cursor);

            result.add(obj);

            moved = cursor.moveToNext();
        }

//        if (cursor != null) {
        cursor.close();
//        }

        return result;
    }

    public List<ClassToSave> querySafe(final String theQuery, final String[] theArguments) {
        ILAPIDBFacade facade = getFacade();

        try {
            facade.open();
            return query(theQuery, theArguments);

        } finally {
            facade.close();
        }
    }

    public List<ClassToSave> query(final String theQuery, final String[] theArguments) {
        List<ClassToSave> result = new LinkedList<ClassToSave>();

        ILAPIDBFacade facade = getFacade();
        Cursor cursor = null;
        cursor = facade.query(theQuery, theArguments);

        boolean moved = cursor.moveToFirst();
        while (moved) {
            ClassToSave obj = newInstance();
            obj.initialize(cursor);

            result.add(obj);

            moved = cursor.moveToNext();
        }
        cursor.close();

        return result;
    }

    public List<ClassToSave> getDataBySqlQuery(final String theSqlQuery, final String[] theArguments) {
        List<ClassToSave> result = new LinkedList<ClassToSave>();

        ILAPIDBFacade facade = getFacade();
        Cursor cursor = null;
        cursor = facade.query(theSqlQuery, theArguments);

        boolean moved = cursor.moveToFirst();
        while (moved) {
            ClassToSave obj = newInstance();
            obj.initialize(cursor);

            result.add(obj);

            moved = cursor.moveToNext();
        }

//        if (cursor != null) {
        cursor.close();
//        }

        return result;
    }

    public List<ClassToSave> getDataBySqlQuerySafe(final String theSqlQuery, final String[] theArguments) {
        ILAPIDBFacade facade = getFacade();
        try {
            facade.open();

            return getDataBySqlQuery(theSqlQuery, theArguments);
        } finally {
            facade.close();
        }
    }

    public EntityCursor getCursorBySqlQuery(final String theSqlQuery, final String[] theArguments) {
        ILAPIDBFacade facade = getFacade();
        Cursor cursor = null;
        cursor = facade.query(theSqlQuery, theArguments);
        return new EntityCursor(cursor);
    }

    public EntityCursor getCursorBySqlQuerySafe(final String theSqlQuery, final String[] theArguments) {
        ILAPIDBFacade facade = getFacade();
        try {
            facade.open();

            return getCursorBySqlQuery(theSqlQuery, theArguments);
        } finally {
            facade.close();
        }
    }

    public List<ClassToSave> getData(ClassId theObj) {
        return getData(getSearchCondition(), getSearchConditionArguments(theObj));
    }

    public List<ClassToSave> getAllSafe() {
        ILAPIDBFacade facade = getFacade();
        try {
            facade.open();
            return getAll();

        } finally {
            facade.close();
        }
    }

    public List<ClassToSave> getAll() {
        return getData(null, null);
    }

    public void saveDataSafe(final List<ClassToSave> theList) {
        ILAPIDBFacade facade = getFacade();
        try {
            facade.open();
            saveData(theList);
        } finally {
            facade.close();
        }
    }

    public void saveDataSafe(final ClassToSave theItem) {
        ILAPIDBFacade facade = getFacade();
        try {
            facade.open();
            saveData(theItem);
        } finally {
            facade.close();
        }
    }

    public void saveData(final List<ClassToSave> theList) {
        for (ClassToSave obj : theList) {
//            Log.d("saving", obj.toString());
            saveData(obj);
        }
    }

    public void saveOrUpdateData(final List<ClassToSave> theList) {
        for (ClassToSave obj : theList) {
            saveOrUpdate(obj);
        }
    }

    public void saveOrUpdateDataSafe(final List<ClassToSave> theList) {
        ILAPIDBFacade facade = getFacade();
        try {
            facade.open();
            saveOrUpdateData(theList);
        } finally {
            facade.close();
        }
    }

    public void saveOrUpdateSafe(final ClassToSave theObj) {
        ILAPIDBFacade facade = getFacade();

        try {
            facade.open();
            saveOrUpdate(theObj);
        } finally {
            facade.close();
        }
    }

    public void saveOrUpdate(final ClassToSave theObj) {
        if (theObj != null) {
            if (!containsData(theObj)) {
                saveData(theObj);
            } else {
                updateData(theObj);
            }
        }
    }

    public long saveData(final ClassToSave theObj) {
        if (theObj == null) {
            throw new IllegalArgumentException("Object can't be null");
        }

        ContentValues values = theObj.getContentValues();
        ILAPIDBFacade facade = getFacade();
        return facade.save(getTableName(), values);
    }

    public int updateData(final ClassToSave theObj) {
        if (theObj == null) {
            throw new IllegalArgumentException("Object can't be null");
        }

        ContentValues values = theObj.getContentValues();

        ILAPIDBFacade facade = getFacade();
        return facade.update(getTableName(), getSearchCondition(),
                getSearchConditionArguments(theObj.getId()), values);
    }

    protected String getSqlQuery(int theResId) {
        ILAPIDBFacade facade = getFacade();
        return facade.getQuery(theResId);
    }

    public void clearData() {
        ILAPIDBFacade facade = getFacade();
        facade.clearTable(getTableName());
    }

    public static int getIntFromBool(boolean theValue) {
        return theValue ? 1 : 0;
    }

    public static boolean getBoolFromInt(int theValue) {
        return theValue >= 1;
    }

    public class EntityCursor {
        private Cursor cursor;

        public EntityCursor(Cursor theCursor) {
            this.cursor = theCursor;
            AbstractEntityDAO.this.getFacade().open();
        }

        private ClassToSave getDataFromCursor(Cursor theCursor) {
            ClassToSave obj = AbstractEntityDAO.this.newInstance();
            obj.initialize(cursor);
            return obj;
        }

        public ClassToSave getNext() {
            boolean mooved = cursor.moveToNext();
            if (mooved) {
                return getDataFromCursor(cursor);
            } else {
                return null;
            }
        }

        public ClassToSave getPrevious() {
            boolean mooved = cursor.moveToPrevious();
            if (mooved) {
                return getDataFromCursor(cursor);
            } else {
                return null;
            }
        }

        public ClassToSave getAtPosition(int thePosition) {
            boolean mooved = cursor.moveToPosition(thePosition);
            if (mooved) {
                return getDataFromCursor(cursor);
            } else {
                return null;
            }
        }

        public int getCount() {
            return cursor.getCount();
        }

        /**
         * Call this method to close database after cursor isn't needed
         */
        public void closeCursor() {
            this.cursor.close();
            AbstractEntityDAO.this.getFacade().close();
        }

    }

}