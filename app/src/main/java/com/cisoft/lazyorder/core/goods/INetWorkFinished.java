package com.cisoft.lazyorder.core.goods;

import java.util.List;

/**
 * Created by Lecion on 14/11/13.
 */
public interface INetWorkFinished<T> {
    public void onSuccess(List<T> l);

    public void onFailure(String info);
}
