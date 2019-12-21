package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PreferencesDbModel;

public class UsageFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        final ViewGroup usageView = (ViewGroup)View.inflate(getContext(), R.layout.activity_usage, null);
        final PreferencesDbModel preferencesDbModel = new PreferencesDbModel(getContext());

        dialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setTitle(R.string.title_usage);
        String formattedText = getString(R.string.text_usage_general);

        TextView textView = usageView.findViewById(R.id.textGeneral);

        textView.setText(HtmlCompat.fromHtml(formattedText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        dialog.setView(usageView);
        dialog.create();

        return dialog;
    }
}
