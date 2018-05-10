package de.byte_artist.gepaeck_planer.sybillesgepaeckplaner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

class LuggageEntityOnLongClickListener implements View.OnLongClickListener {

    private LuggageEntity luggageEntity = null;
    private LuggageActivity luggageActivity = null;

    LuggageEntityOnLongClickListener(LuggageActivity luggageActivity, LuggageEntity luggageEntity) {
        this.luggageActivity = luggageActivity;
        this.luggageEntity = luggageEntity;

    }

    @Override
    public boolean onLongClick(View view) {
        LuggageEditDialog editDialog = new LuggageEditDialog(this.luggageActivity);
        editDialog.showEditDialog(view, luggageEntity);

        return false;
    }
}
