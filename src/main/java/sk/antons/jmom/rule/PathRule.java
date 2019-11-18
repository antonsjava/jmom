/*
 * Copyright 2019 Anton Straka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sk.antons.jmom.rule;

import java.util.ArrayList;
import java.util.List;
import sk.antons.json.JsonValue;
import sk.antons.json.match.wild.WPM;

/**
 *
 * @author antons
 */
public abstract class PathRule {
    
    String[] path;
    
    public PathRule(String[] path) {
        this.path = path;
    }
    
    protected String purePath(String path) {
        if(path == null) return path;
        int pos = path.lastIndexOf('|');
        if(pos < 0) return path;
        return path.substring(0, pos);
    }
    
    protected int hopFromPath(String path) {
        if(path == null) return 0;
        int pos = path.lastIndexOf('|');
        if(pos < 0) return 0;
        String s = path.substring(pos+1);
        try {
            return Integer.parseInt(s);
        } catch(Exception e) {
            return 0;
        }
    }
    
    protected JsonValue hopUp(JsonValue value, int hopCount) {
        while((value != null) && (hopCount-- > 0)) {            
            value = value.parent();
        }
        return value;
    }

    protected List<JsonValue> allPathResults(JsonValue value) {
        List<JsonValue> list = new ArrayList<JsonValue>();
        if(value == null) return list;
        if(path != null) {
            for(String p : path) {
                String pure = purePath(p);
                int hop = hopFromPath(p);
                List<JsonValue> l = value.findAll(WPM.fromPath(pure));
                if(l != null) {
                    for(JsonValue jsonValue : l) {
                        jsonValue = hopUp(jsonValue, hop);
                        if(jsonValue != null) list.add(jsonValue);
                    }
                }
            }
        }
        return list;
    }
    
    protected JsonValue firstPathResult(JsonValue value) {
        if(value == null) return null;
        if(path != null) {
            for(String p : path) {
                String pure = purePath(p);
                int hop = hopFromPath(p);
                JsonValue v = value.findFirst(WPM.fromPath(pure));
                if(v != null) {
                    v = hopUp(v, hop);
                }
                if(v != null) return v;;
            }
        }
        return null;
    }
    
}
