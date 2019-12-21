package de.byte_artist.luggage_planner;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Objects;

import de.byte_artist.luggage_planner.activity.CategoryActivity;
import de.byte_artist.luggage_planner.activity.LuggageActivity;
import de.byte_artist.luggage_planner.activity.MainActivity;
import de.byte_artist.luggage_planner.activity.PackingListActivity;
import de.byte_artist.luggage_planner.activity.SyncActivity;
import de.byte_artist.luggage_planner.dialog.CustomDialog;
import de.byte_artist.luggage_planner.dialog.OptionsFragment;
import de.byte_artist.luggage_planner.dialog.UsageFragment;
import de.byte_artist.luggage_planner.interfaces.RefreshableInterface;

@SuppressLint("Registered")
public class AbstractActivity extends AppCompatActivity implements RefreshableInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getSupportActionBar()) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        }
    }

    public void refresh() {}

    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.complete, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                break;
            case R.id.mainMenuMain:
                finish();
                Intent mainIntent = new Intent(this, MainActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.mainMenuCategories:
//                finish();
                Intent categoryIntent = new Intent(this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;
            case R.id.mainMenuLuggage:
//                finish();
                Intent luggageIntent = new Intent(this, LuggageActivity.class);
                startActivity(luggageIntent);
                break;
            case R.id.mainMenuSync:
                Intent myIntent = new Intent(this, SyncActivity.class);
                this.startActivity(myIntent);
                break;
            case R.id.mainMenuOptions:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("options_dialog");

                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                OptionsFragment fragment = new OptionsFragment();
                fragment.show(ft, "options_dialog");

                break;
            case R.id.mainMenuUsage:
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment previousFragment = getSupportFragmentManager().findFragmentByTag("usage_dialog");

                if (previousFragment != null) {
                    fragmentTransaction.remove(previousFragment);
                }
                fragmentTransaction.addToBackStack(null);

                UsageFragment currentFragment = new UsageFragment();
                currentFragment.show(fragmentTransaction, "usage_dialog");
                break;
            case R.id.mainMenuPackingLists:
//                finish();
                Intent packingListIntent = new Intent(this, PackingListActivity.class);
                startActivity(packingListIntent);
                break;
            case R.id.mainMenuExit:
                finishAffinity();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.class == getClass()) {
            CustomDialog dialog = new CustomDialog(this, R.style.AlertDialogTheme, CustomDialog.TYPE_WARNING);
            dialog.setTitle(R.string.label_attention);
            dialog.setMessage(R.string.text_warning_exit_app);
            dialog.setButton(CustomDialog.BUTTON_POSITIVE, getResources().getString(R.string.text_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            });
            dialog.setButton(CustomDialog.BUTTON_NEGATIVE, getResources().getString(R.string.text_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }
}
