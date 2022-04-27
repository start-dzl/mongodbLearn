package com.dzl.mongodb.inCharge;

import com.dzl.mongodb.entity.Head;

import java.util.Map;

public interface BaseEInChangeManage {

    Map<String, Object> inChange(Map<String, Object> hashMap, Head value);


}
