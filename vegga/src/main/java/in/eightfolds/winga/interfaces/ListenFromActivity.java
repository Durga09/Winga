package in.eightfolds.winga.interfaces;

import android.content.Intent;

public interface ListenFromActivity {
    void doSomethingInFragment();
    void onAction( int requestCode,  int resultCode,  Intent data);

}
