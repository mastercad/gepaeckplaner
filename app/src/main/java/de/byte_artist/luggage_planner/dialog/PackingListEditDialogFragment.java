package de.byte_artist.luggage_planner.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.entity.PackingListEntity;

public class PackingListEditDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private PackingListEntity packingListEntity;

    TextView editText;

    public static PackingListEditDialogFragment newInstance(PackingListEntity packingListEntity) {
        PackingListEditDialogFragment fragment = new PackingListEditDialogFragment();
        fragment.setComplexVariable(packingListEntity);

        return fragment;
    }

    public void setComplexVariable(PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
    }

    private void setDate(final Calendar calendar) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        editText.setText(sdf.format(calendar.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calender = Calendar.getInstance();
        calender.set(year, month, day);
        setDate(calender);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        final View packingListEntryEditView = View.inflate(getContext(), R.layout.activity_packing_list_edit_dialog, null);

        final EditText inputPackingListName = packingListEntryEditView.findViewById(R.id.inputPackingListName);
        inputPackingListName.setText(packingListEntity.getName());

        final TextView inputPackingListDate = packingListEntryEditView.findViewById(R.id.inputPackingListDate);
        inputPackingListDate.setText(packingListEntity.getDate());

        builder.setTitle(R.string.title_packing_list_new);
        builder.setView(packingListEntryEditView);

        editText = packingListEntryEditView.findViewById(R.id.inputPackingListDate);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderDialogFragment dialog = new CalenderDialogFragment();
                dialog.setListeningActivity(PackingListEditDialogFragment.this);
                dialog.show(getFragmentManager(), "date");
            }
        });

        builder.setPositiveButton(R.string.text_save, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String packingListName = inputPackingListName.getText().toString();
                String packingListDate = inputPackingListDate.getText().toString();

                if (0 == packingListName.length()
                    || 0 == packingListDate.length()
                ) {
                    showAlertNotAllNeededFieldFilled();
                } else {
                    packingListEntity.setName(packingListName);
                    packingListEntity.setDate(packingListDate);

                    PackingListDbModel packingListDbModel = new PackingListDbModel(getActivity(), null, null, 1);
                    packingListDbModel.update(packingListEntity);

                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                }
            }
        });

        builder.setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        setRetainInstance(true);
        builder.setOnDismissListener(this);

        return builder.create();
    }

    private void showAlertNotAllNeededFieldFilled() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_edit_dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertNotAllNeededFieldsFilled alertDialog = new AlertNotAllNeededFieldsFilled();
        alertDialog.show(ft, "packing_list_edit_dialog");
    }
}
