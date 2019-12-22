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

public class UsageFragment extends DialogFragment {

    private String currentClassName = "";

    public UsageFragment(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);

        final ViewGroup usageView = (ViewGroup)View.inflate(getContext(), R.layout.activity_usage, null);

        dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_understood), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setTitle(R.string.title_usage);

        TextView textView = usageView.findViewById(R.id.textGeneral);

        textView.setText(HtmlCompat.fromHtml(considerCurrentClassNameForUsageText(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        dialog.setView(usageView);
        dialog.create();

        return dialog;
    }

    private String considerCurrentClassNameForUsageText() {

        String usageText = "";

        switch (currentClassName) {
            case "CategoryActivity":
                usageText = getString(R.string.text_usage_category);
                break;
            case "LuggageActivity":
                usageText = getString(R.string.text_usage_luggage);
                break;
            case "PackingListActivity":
                usageText = getString(R.string.text_usage_packinglist);
                break;
            case "PackingListDetailActivity":
                usageText = getString(R.string.text_usage_packinglist_detail);
                break;
            case "SyncActivity":
                usageText = getString(R.string.text_usage_sync);
                break;
            default:
                usageText = getString(R.string.text_usage_general);
        }

        return usageText;
    }
}
