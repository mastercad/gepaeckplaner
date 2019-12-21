package de.byte_artist.luggage_planner.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.service.TextSize;

public class CustomDialog extends AlertDialog {

    final public static String TYPE_ALERT = "ic_dialog_alert";
    final public static String TYPE_WARNING = "ic_dialog_alert";
    final public static String TYPE_INFO = "ic_dialog_info";
    final public static String TYPE_EDIT = "ic_menu_edit";

    private final FragmentActivity activity;
    private final String dialogType;
    private CharSequence title;

    public CustomDialog(@NonNull FragmentActivity activity, String dialogType) {
        super(activity);
        this.activity = activity;
        this.dialogType = dialogType;
    }

    public CustomDialog(@NonNull FragmentActivity activity) {
        super(activity);
        this.activity = activity;
        this.dialogType = null;
    }

    public CustomDialog(@NonNull FragmentActivity activity, @StyleRes int themeResId, String dialogType) {
        super(activity, themeResId);
        this.activity = activity;
        this.dialogType = dialogType;
    }
    public CustomDialog(@NonNull FragmentActivity activity, @StyleRes int themeResId) {
        super(activity, themeResId);
        this.activity = activity;
        this.dialogType = null;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        TextView titleTextView = new TextView(this.getContext());
        titleTextView.setText(title);
        TextSize.convert(this.activity, titleTextView, TextSize.TEXT_TYPE_TITLE);
        titleTextView.setGravity(Gravity.START);
//        titleTextView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        titleTextView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary));
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setPadding(25, 25, 25, 25);

        super.setCustomTitle(titleTextView);
    }

    public void setTitle(@StringRes int resourceId) {
        this.title = getContext().getString(resourceId);
        TextView titleTextView = new TextView(this.getContext());
        titleTextView.setText(this.title);
        TextSize.convert(this.activity, titleTextView, TextSize.TEXT_TYPE_TITLE);
        titleTextView.setGravity(Gravity.START);
//        titleTextView.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        titleTextView.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary));
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setPadding(25, 25, 25, 25);

        super.setCustomTitle(titleTextView);
    }

    public void setMessage(CharSequence message) {
        TextView messageTextView = new TextView(this.getContext());
        messageTextView.setText(message);
        TextSize.convert(this.activity, messageTextView, TextSize.TEXT_TYPE_MESSAGE);
        messageTextView.setPadding(25, 25, 25, 25);

        super.setView(messageTextView);
    }

    public void setMessage(@StringRes int resourceId) {
        TextView messageTextView = new TextView(this.getContext());
        messageTextView.setText(getContext().getString(resourceId));
        TextSize.convert(this.activity, messageTextView, TextSize.TEXT_TYPE_MESSAGE);
        messageTextView.setPadding(25, 25, 25, 25);

        super.setView(messageTextView);
    }

    public void setView(View view) {
        ViewGroup viewGroup = (ViewGroup)view;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof TextView) {
                TextSize.convert(this.activity, (TextView) childView, TextSize.TEXT_TYPE_MESSAGE);
            } else if (childView instanceof Spinner) {
                TextSize.convert(this.activity, (Spinner) childView, TextSize.TEXT_TYPE_MESSAGE);
            } else if (childView instanceof ViewGroup) {
                setView(childView);
            }
        }
        super.setView(viewGroup);
    }

    @Override
    public void create() {
        prepareIconForTitle();

        setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPositive = getButton(Dialog.BUTTON_POSITIVE);
                if (null != btnPositive) {
                    TextSize.convert(activity, btnPositive, TextSize.TEXT_TYPE_BUTTON);
                }

                Button btnNegative = getButton(Dialog.BUTTON_NEGATIVE);
                if (null != btnNegative) {
                    TextSize.convert(activity, btnNegative, TextSize.TEXT_TYPE_BUTTON);
                }

                Button btnNeutral = getButton(Dialog.BUTTON_NEUTRAL);
                if (null != btnNeutral) {
                    TextSize.convert(activity, btnNeutral, TextSize.TEXT_TYPE_BUTTON);
                }
            }
        });

        super.create();
    }

    private void prepareIconForTitle() {
        if (null != dialogType) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder("  "+this.title);

            stringBuilder.setSpan(
                new ImageSpan(
                    getContext(),
                        Resources.getSystem().getIdentifier(dialogType, "drawable", "android")
                ),
                0,
                1,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            );

            TextView title = new TextView(getContext());
            title.setText(stringBuilder, TextView.BufferType.SPANNABLE);
            TextSize.convert(activity, title, TextSize.TEXT_TYPE_TITLE);
            title.setGravity(Gravity.START);
//            title.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            title.setBackgroundColor(ContextCompat.getColor(this.getContext(), R.color.colorPrimary));
            title.setTextColor(Color.WHITE);
            title.setPadding(25, 25, 25, 25);
            this.setCustomTitle(title);
        }
    }
}
