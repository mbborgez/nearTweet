package pt.utl.ist.cm.neartweetclient;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 *
 * Defines constants for accessing the content provider defined in DataProvider. A content provider
 * contract assists in accessing the provider's available content URIs, column names, MIME types,
 * and so forth, without having to know the actual values.
 */
public final class DataProviderContract implements BaseColumns {

    private DataProviderContract() { }
        
        // The URI scheme used for content URIs
        public static final String SCHEME = "content";

        // The provider's authority
        public static final String AUTHORITY = "pt.utl.ist.cm.neartweetclient";

        /**
         * The DataProvider content URI
         */
        public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

        /**
         *  The MIME type for a content URI that would return multiple rows
         *  <P>Type: TEXT</P>
         */
        public static final String MIME_TYPE_ROWS =
                "vnd.android.cursor.dir/vnd.pt.utl.ist.cm.neartweetclient";

        /**
         * The MIME type for a content URI that would return a single row
         *  <P>Type: TEXT</P>
         *
         */
        public static final String MIME_TYPE_SINGLE_ROW =
                "vnd.android.cursor.item/vnd.pt.utl.ist.cm.neartweetclient";

        /**
         * Tweets table primary key column name
         */
        public static final String ROW_ID = BaseColumns._ID;
        
        /**
         * Tweets table name
         */
        public static final String TABLE_NAME = "TweetsData";

        /**
         * Tweets table content URI
         */
        public static final Uri TABLE_CONTENTURI =
                Uri.withAppendedPath(CONTENT_URI, TABLE_NAME);

        /**
         * Tweets table user ID column name
         */
        public static final String USER_ID_COLUMN = "user_id";
        
        /**
         * Tweets table tweet id column name
         */
        public static final String TWEET_ID_COLUMN = "tweet_id";
        
        /**
         * Tweets table message column name
         */
        public static final String MESSAGE_CONTENT_COLUMN = "message_text";
       
       
        // The content provider database name
        public static final String DATABASE_NAME = "nearTweetDataDB";

        // The starting version of the database
        public static final int DATABASE_VERSION = 1;
}

