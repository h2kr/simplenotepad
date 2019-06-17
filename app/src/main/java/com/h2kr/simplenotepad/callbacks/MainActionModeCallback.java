package com.h2kr.simplenotepad.callbacks;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.h2kr.simplenotepad.R;

public abstract class MainActionModeCallback implements ActionMode.Callback {

    private ActionMode action;
    private MenuItem countItem;
    private MenuItem shareItem;

    public ActionMode getAction() {
        return action;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.main_action_mode, menu);
        this.action = mode;
        this.countItem = menu.findItem(R.id.checked_count);
        this.shareItem = menu.findItem(R.id.action_share_note);


        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

    public void setCount(String countChecked) {
        if(countItem != null)
        this.countItem.setTitle(countChecked);
    }

    public void changeShareItemVisible(boolean b) {
        shareItem.setVisible(b);
    }
}
