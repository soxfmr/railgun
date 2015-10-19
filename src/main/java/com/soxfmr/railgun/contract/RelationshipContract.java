package com.soxfmr.railgun.contract;

import com.soxfmr.railgun.model.Model;

public interface RelationshipContract<E> {
    Model relyOn(String references, String relation, Class<E> cls);
}
