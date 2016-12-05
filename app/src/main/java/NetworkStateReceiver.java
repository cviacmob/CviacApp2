import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.cviac.activity.cviacapp.CVIACApplication;

/**
 * Created by User on 12/5/2016.
 */

  public class NetworkStateReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            CVIACApplication app=(CVIACApplication) context.getApplicationContext();
            Log.d("app","Network connectivity change");
            if(intent.getExtras()!=null) {
                NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
                if(ni!=null && ni.getState()== NetworkInfo.State.CONNECTED) {
                    Log.i("app","Network "+ni.getTypeName()+" connected");

                    app.setNetworkStatus(true);
                }
            }
            if(intent.getExtras().getBoolean(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
                Log.d("app","There's no network connectivity");
                app.setNetworkStatus(false);
            }
        }
    }


