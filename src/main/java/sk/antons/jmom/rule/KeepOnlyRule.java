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
import sk.antons.json.JsonArray;
import sk.antons.json.JsonAttribute;
import sk.antons.json.JsonObject;
import sk.antons.json.JsonValue;

/**
 * Removes all except specified paths value from given json. 
 *
 * @author antons
 */
public class KeepOnlyRule extends PathRule implements Rule {
    
    public KeepOnlyRule(String... path) {
        super(path);
    }

    public static KeepOnlyRule instance(String... path) {
        return new KeepOnlyRule(path);
    }

    @Override
    public void apply(JsonValue json) {
        List<JsonValue> values = allPathResults(json);
        traverse(values, json);
    }
    
    private void traverse(List<JsonValue> values, JsonValue json) {
        if(json == null) {
            return;
        } else if(json.isLiteral()) {
            return;
        } else if(json.isArray()) {
            JsonArray array = json.asArray();
            int len = array.size();
            List<JsonValue> list = new ArrayList<JsonValue>();
            for(int i = 0; i < len; i++) {
                JsonValue v = array.get(i);
                if(values.contains(v)) {
                } else if(isparentOf(values, v)) {
                    traverse(values, v);
                } else {
                    list.add(v);
                }
            }
            for(JsonValue jsonValue : list) {
                jsonValue.remove();
            }
        } else if(json.isObject()) {
            JsonObject object = json.asObject();
            int len = object.size();
            List<JsonValue> list = new ArrayList<JsonValue>();
            for(int i = 0; i < len; i++) {
                JsonAttribute attr = object.attr(i);
                JsonValue v = attr.value();
                if(values.contains(v)) {
                } else if(isparentOf(values, v)) {
                    traverse(values, v);
                } else {
                    list.add(v);
                }
            }
            for(JsonValue jsonValue : list) {
                jsonValue.remove();
            }
        }    
    }

    private boolean isparentOf(List<JsonValue> values, JsonValue value) {
        if(value == null) return false;
        if(values == null) return false;
        for(JsonValue value1 : values) {
            if(value1.isDescendantOf(value)) return true;
        }
        return false;
    }
    
    
}
