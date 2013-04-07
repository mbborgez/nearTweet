package pt.utl.ist.cm.neartweetclient;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

/**
 *
 * Defines a ContentProvider that stores URLs of Picasa featured pictures
 * The provider also has a table that tracks the last time a picture URL was updated.
 */
public class DataProvider extends ContentProvider {
    // Indicates that the incoming query is for a picture URL
    public static final int TWEET_URL_QUERY = 1;

    // Indicates an invalid content URI
    public static final int INVALID_URI = -1;

    // Constants for building SQLite tables during initialization
    private static final String TEXT_TYPE = "TEXT";
    private static final String PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY";
    private static final String INTEGER_TYPE = "INTEGER";

    // Defines an SQLite statement that builds the Twitter tweets table
    private static final String CREATE_TWEETS_TABLE_SQL = "CREATE TABLE" + " " +
            DataProviderContract.TABLE_NAME + " " +
            "(" + " " +
            DataProviderContract.ROW_ID + " " + PRIMARY_KEY_TYPE + " ," +
            DataProviderContract.USER_ID_COLUMN + " " + TEXT_TYPE + " ," +
            DataProviderContract.TWEET_ID_COLUMN + " " + TEXT_TYPE + " ," +
            DataProviderContract.MESSAGE_CONTENT_COLUMN + " " + TEXT_TYPE + " ," +
            ")";

    // Identifies log statements issued by this component
    public static final String LOG_TAG = "DataProvider";

    // Defines an helper object for the backing database
    private SQLiteOpenHelper mHelper;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    // Stores the MIME types served by this provider
    private static final SparseArray<String> sMimeTypes;

    /*
     * Initializes meta-data used by the content provider:
     * - UriMatcher that maps content URIs to codes
     * - MimeType array that returns the custom MIME type of a table
     */
    static {
        
        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        /*
         * Sets up an array that maps content URIs to MIME types, via a mapping between the
         * URIs and an integer code. These are custom MIME types that apply to tables and rows
         * in this particular provider.
         */
        sMimeTypes = new SparseArray<String>();

        // Adds a URI "match" entry that maps tweets content URIs to a numeric code
        sUriMatcher.addURI(
                DataProviderContract.AUTHORITY,
                DataProviderContract.TABLE_NAME,
                TWEET_URL_QUERY);
        
        // Specifies a custom MIME type for the picture URL table
        sMimeTypes.put(
        		TWEET_URL_QUERY,
                "vnd.android.cursor.dir/vnd." +
                DataProviderContract.AUTHORITY + "." +
                DataProviderContract.TABLE_NAME);
    }

    // Closes the SQLite database helper class, to avoid memory leaks
    public void close() {
        mHelper.close();
    }
    
    /**
     * Defines a helper class that opens the SQLite database for this provider when a request is
     * received. If the database doesn't yet exist, the helper creates it.
     */
    private class DataProviderHelper extends SQLiteOpenHelper {
        /**
         * Instantiates a new SQLite database using the supplied database name and version
         *
         * @param context The current context
         */
        DataProviderHelper(Context context) {
            super(context,
                    DataProviderContract.DATABASE_NAME,
                    null,
                    DataProviderContract.DATABASE_VERSION);
        }


        /**
         * Executes the queries to drop all of the tables from the database.
         *
         * @param db A handle to the provider's backing database.
         */
        private void dropTables(SQLiteDatabase db) {

            // If the table doesn't exist, don't throw an error
            db.execSQL("DROP TABLE IF EXISTS " + DataProviderContract.TABLE_NAME);
        }

        /**
         * Does setup of the database. The system automatically invokes this method when
         * SQLiteDatabase.getWriteableDatabase() or SQLiteDatabase.getReadableDatabase() are
         * invoked and no db instance is available.
         *
         * @param db the database instance in which to create the tables.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // Creates the tables in the backing database for this provider
            db.execSQL(CREATE_TWEETS_TABLE_SQL);

        }

        /**
         * Handles upgrading the database from a previous version. Drops the old tables and creates
         * new ones.
         *
         * @param db The database to upgrade
         * @param version1 The old database version
         * @param version2 The new database version
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int version1, int version2) {
            Log.w(DataProviderHelper.class.getName(),
                    "Upgrading database from version " + version1 + " to "
                            + version2 + ", which will destroy all the existing data");

            // Drops all the existing tables in the database
            dropTables(db);

            // Invokes the onCreate callback to build new tables
            onCreate(db);
        }
        /**
         * Handles downgrading the database from a new to a previous version. Drops the old tables
         * and creates new ones.
         * @param db The database object to downgrade
         * @param version1 The old database version
         * @param version2 The new database version
         */
        @Override
        public void onDowngrade(SQLiteDatabase db, int version1, int version2) {
            Log.w(DataProviderHelper.class.getName(),
                "Downgrading database from version " + version1 + " to "
                        + version2 + ", which will destroy all the existing data");
    
            // Drops all the existing tables in the database
            dropTables(db);
    
            // Invokes the onCreate callback to build new tables
            onCreate(db);
            
        }
    }
    /**
     * Initializes the content provider. Notice that this method simply creates a
     * the SQLiteOpenHelper instance and returns. You should do most of the initialization of a
     * content provider in its static initialization block or in SQLiteDatabase.onCreate().
     */
    @Override
    public boolean onCreate() {

        // Creates a new database helper object
        mHelper = new DataProviderHelper(getContext());

        return true;
    }
    /**
     * Returns the result of querying the chosen table.
     * @see android.content.ContentProvider#query(Uri, String[], String, String[], String)
     * @param uri The content URI of the table
     * @param projection The names of the columns to return in the cursor
     * @param selection The selection clause for the query
     * @param selectionArgs An array of Strings containing search criteria
     * @param sortOrder A clause defining the order in which the retrieved rows should be sorted
     * @return The query results, as a {@link android.database.Cursor} of rows and columns
     */
    @Override
    public Cursor query(
        Uri uri,
        String[] projection,
        String selection,
        String[] selectionArgs,
        String sortOrder) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        // Decodes the content URI and maps it to a code
        switch (sUriMatcher.match(uri)) {

            // If the query is for a picture URL
            case TWEET_URL_QUERY:
                // Does the query against a read-only version of the database
                Cursor returnCursor = db.query(
                    DataProviderContract.TABLE_NAME,
                    projection,
                    null, null, null, null, null);

                // Sets the ContentResolver to watch this content URI for data changes
                returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
                return returnCursor;
            case INVALID_URI:

                throw new IllegalArgumentException("Query -- Invalid URI:" + uri);
        }

        return null;
    }

    /**
     * Returns the mimeType associated with the Uri (query).
     * @see android.content.ContentProvider#getType(Uri)
     * @param uri the content URI to be checked
     * @return the corresponding MIMEtype
     */
    @Override
    public String getType(Uri uri) {

        return sMimeTypes.get(sUriMatcher.match(uri));
    }
    /**
     *
     * Insert a single row into a table
     * @see android.content.ContentProvider#insert(Uri, ContentValues)
     * @param uri the content URI of the table
     * @param values a {@link android.content.ContentValues} object containing the row to insert
     * @return the content URI of the new row
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new IllegalArgumentException("Insert: Invalid URI" + uri);
    }
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}

