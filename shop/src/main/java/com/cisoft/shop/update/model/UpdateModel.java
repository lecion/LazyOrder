package com.cisoft.shop.update.model;

import android.content.Context;

import com.cisoft.shop.http.AbsService;

/**
 * Created by Lecion on 3/24/15.
 */
public class UpdateModel extends AbsService{
    private Context context;

    public UpdateModel(Context context, String moduleName) {
        super(context, moduleName);
        this.context = context;
    }

}
