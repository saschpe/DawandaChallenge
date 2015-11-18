package saschpe.dawandachallenge.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.GregorianCalendar;

import saschpe.dawandachallenge.BuildConfig;
import saschpe.dawandachallenge.R;

/**
 * Dialog fragment to display app name and version
 */
public class VersionInfoDialogFragment extends DialogFragment {

    static public VersionInfoDialogFragment newInstance() {
        return new VersionInfoDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_version_info, container, false);

        TextView version = (TextView) view.findViewById(R.id.version);
        version.setText(String.format(getString(R.string.version_info_template), BuildConfig.VERSION_NAME));
        TextView copyright = (TextView) view.findViewById(R.id.copyright);
        GregorianCalendar cal = new GregorianCalendar();
        copyright.setText(String.format(getString(R.string.copyright_template), cal));

        return view;
    }

    /**
     * The system calls this only when creating the layout in a dialog.
     * */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }
}

