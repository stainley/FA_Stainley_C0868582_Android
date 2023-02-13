package com.stainley.fa.android.data;

import androidx.lifecycle.LiveData;

import java.io.Serializable;
import java.util.List;

public interface AbstractDao <T extends Serializable> {

    void save(T t);
    void update(T t);
    void delete(T t);

    LiveData<List<T>> fetchAll();
}
