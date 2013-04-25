package pt.utl.ist.cm.neartweetclient.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HTTPConnection {
 
    /**
     * Checking for all possible Internet providers and return true if
     * some of those has the state connected, false otherwise
     */
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = 
        		(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) {
                  for (int i = 0; i < info.length; i++) {
                      if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                          return true;
                      }
                  }
              }
          }
          return false;
    }
}
