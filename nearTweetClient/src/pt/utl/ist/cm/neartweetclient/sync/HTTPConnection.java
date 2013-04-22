package pt.utl.ist.cm.neartweetclient.sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HTTPConnection {

    private Context context;
 
    public HTTPConnection(Context context){
        this.context = context;
    }
 
    /**
     * Checking for all possible Internet providers
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = 
        		(ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
