package com.davelabs.wakemehome.persistence.db;

import android.provider.BaseColumns;

public final class DBSearchedLocationStoreContract {

    public DBSearchedLocationStoreContract() {}

    public static abstract class SearchedLocationContract implements BaseColumns {
        public static final String TABLE_NAME = "searchedlocations";
        public static final String COLUMN_NAME_SEARCH_QUERY = "search_query";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_IS_HOME = "is_home";
        public static final String COLUMN_NAME_IS_PINNED = "is_pinned";
        public static final String COLUMN_NAME_LAST_UPDATED = "last_updated";
    }
}