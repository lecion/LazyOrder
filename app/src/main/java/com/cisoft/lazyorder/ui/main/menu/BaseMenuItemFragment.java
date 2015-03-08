package com.cisoft.lazyorder.ui.main.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cisoft.lazyorder.core.common.DrawerOpenStateObserver;
import com.cisoft.lazyorder.core.common.I_DrawerOpenStateObserver;

import org.kymjs.kjframe.ui.FrameFragment;

/**
 * Created by comet on 2014/11/25.
 */
public abstract class BaseMenuItemFragment extends FrameFragment implements I_DrawerOpenStateObserver{

    private boolean isMenuOpen = false;

    public BaseMenuItemFragment() {
        DrawerOpenStateObserver.getInstance().attach(this);
    }

    @Override
    protected abstract View inflaterView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle);

    public void setMenuOpenState(boolean isMenuOpen) {
        this.isMenuOpen = isMenuOpen;
    }

    public boolean getMenuOpenState() {
        return isMenuOpen;
    }

    @Override
    public void onStateChange(boolean isOpenState) {
        isMenuOpen = isOpenState;
    }
}
