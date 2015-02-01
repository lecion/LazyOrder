package com.cisoft.lazyorder.ui.main.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.kymjs.kjframe.ui.FrameFragment;

/**
 * Created by comet on 2014/11/25.
 */
public abstract class MenuItemContent extends FrameFragment {

    private boolean isMenuOpen = false;

    @Override
    protected abstract View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    public void setMenuOpenState(boolean isMenuOpen) {
        this.isMenuOpen = isMenuOpen;
    }

    public boolean getMenuOpenState() {
        return isMenuOpen;
    }
}
