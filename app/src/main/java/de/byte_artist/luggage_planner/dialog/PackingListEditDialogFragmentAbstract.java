package de.byte_artist.luggage_planner.dialog;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import de.byte_artist.luggage_planner.R;
import de.byte_artist.luggage_planner.db.PackingListDbModel;
import de.byte_artist.luggage_planner.entity.PackingListEntity;
import de.byte_artist.luggage_planner.helper.LocaleHelper;

abstract class PackingListEditDialogFragmentAbstract extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private PackingListEntity packingListEntity;
    private TextView inputPackingListDate;

    public PackingListEditDialogFragmentAbstract() {}

    public void setComplexVariable(PackingListEntity packingListEntity) {
        this.packingListEntity = packingListEntity;
    }

    private void setDate(final Calendar calendar) {
        final Locale currentLocale = LocaleHelper.investigateLocale(this.getContext());

        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, currentLocale);

        inputPackingListDate.setText(sdf.format(calendar.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calender = Calendar.getInstance();
        calender.set(year, month, day);
        setDate(calender);
    }

    @SuppressLint("CutPasteId")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final CustomDialog dialog = new CustomDialog(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme, CustomDialog.TYPE_EDIT);
        final View packingListEditView = View.inflate(getContext(), R.layout.activity_packing_list_edit_dialog, null);
        final Button btnSave = packingListEditView.findViewById(R.id.btnSave);
        final Button btnCancel = packingListEditView.findViewById(R.id.btnCancel);
        final EditText inputPackingListName = packingListEditView.findViewById(R.id.inputPackingListName);
        inputPackingListDate = packingListEditView.findViewById(R.id.inputPackingListDate);

        try {
            Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } catch (Exception exception) {
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        if (null != packingListEntity) {
            inputPackingListName.setText(packingListEntity.getName());
        }


        if (null != packingListEntity) {
            inputPackingListDate.setText(packingListEntity.getDate());
        }

        dialog.setTitle(R.string.title_packing_list_new);
        dialog.setView(packingListEditView);

        inputPackingListDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalenderDialogFragment fragment = new CalenderDialogFragment();

                if (null != packingListEntity) {
                    fragment.setDate(packingListEntity.getDate());
                }
                fragment.setListeningActivity(PackingListEditDialogFragmentAbstract.this);
                fragment.show(Objects.requireNonNull(getFragmentManager()), "date");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String packingListName = inputPackingListName.getText().toString();
                String packingListDate = inputPackingListDate.getText().toString();

                if (0 == packingListName.length()
                    || 0 == packingListDate.length()
                ) {
                    showAlertNotAllNeededFieldFilled();
                } else {
                    PackingListDbModel packingListDbModel = new PackingListDbModel(getActivity());
                    if (null != packingListEntity) {
                        packingListEntity.setName(packingListName);
                        packingListEntity.setDate(packingListDate);

                        packingListDbModel.update(packingListEntity);
                    } else {
                        packingListEntity = new PackingListEntity();
                        packingListEntity.setName(packingListName);
                        packingListEntity.setDate(packingListDate);

                        packingListDbModel.insert(packingListEntity);
                    }

                    getActivity().finish();
                    getActivity().startActivity(getActivity().getIntent());
                    Toast.makeText(getContext(), getResources().getString(R.string.text_data_successfully_saved), Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.create();
        return  dialog;
    }

    private void showAlertNotAllNeededFieldFilled() {
        FragmentTransaction ft = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("packing_list_edit_dialog_alert");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final AlertNotAllNeededFieldsFilled fragment = new AlertNotAllNeededFieldsFilled();
        fragment.show(ft, "packing_list_edit_dialog_alert");
    }
}
