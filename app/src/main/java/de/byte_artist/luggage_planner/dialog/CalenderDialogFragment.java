package de.byte_artist.luggage_planner.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class CalenderDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog.OnDateSetListener listener = null;

    private String date = "";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calender = Calendar.getInstance();

        if (!date.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            try {
                calender.setTime(dateFormat.parse(this.date));
            } catch (ParseException exception) {
                Toast.makeText(getContext(), this.date+" konnte nicht geparsed werden!", Toast.LENGTH_SHORT).show();
            }
        }
        int year = calender.get(Calendar.YEAR);
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(Objects.requireNonNull(getActivity()),
            AlertDialog.THEME_HOLO_LIGHT,
//            R.style.AlertDialogTheme,
//            AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,
//            AlertDialog.THEME_TRADITIONAL,
            this,
            year,
            month,
            day
        );
    }

    public void setListeningActivity(DatePickerDialog.OnDateSetListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (listener != null) {
            listener.onDateSet(view, year, month, day);
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
