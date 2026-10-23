package com.tagalong.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tagalong.util.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JacksonSerializable {
    
    @Override
    public String toString(){
        return JsonUtil.serialize(this);
    }
}
