package com.soxfmr.railgun.contract;

import java.util.List;

public interface BaseDaoContract<E> {
    List<E> all();
    <S, T> List<E> where(S source, T target);
    E get(int index);
    boolean remove(int index);
    boolean remove(E e);
    boolean insert(E e);
    boolean update(E e);
}
