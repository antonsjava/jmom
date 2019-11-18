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

import java.util.List;
import sk.antons.jmom.util.PathUtil;
import sk.antons.json.JsonValue;

/**
 *
 * @author antons
 */
public class AddRule implements Rule {
    
    String path;
    JsonValue value;
    boolean replace = false;
    
    public AddRule(String path, JsonValue value, boolean replace) {
        this.path = path;
        this.value = value;
        this.replace = replace;
    }

    public static AddRule instance(String path, JsonValue value, boolean replace) {
        return new AddRule(path, value, replace);
    }

    private List<String> elements = null;
    private List<String> elements() {
        if(elements == null) elements = PathUtil.elements(path);
        return elements;
    };

    @Override
    public void apply(JsonValue json) {
        List<String> elems = elements();
        JsonValue parent = PathUtil.mkdirsParent(elems, json);
        if(parent == null) return;
        String lastelem = elems.get(elems.size()-1);
        if(value.parent() != null) value = value.copy();
        if(parent.isObject()) {
            JsonValue v = parent.asObject().first(lastelem);
            if(v == null) parent.asObject().add(lastelem, value);
            else if(replace) v.replaceBy(value);
        } else if(parent.isArray()) {
            int index = -1;
            try {
                index = Integer.parseInt(lastelem);
            } catch(Exception e) {
                index = parent.asArray().size();
            }
            if(index < 0) index = 0;
            if(index >= parent.asArray().size()) parent.asArray().add(value);
            else parent.asArray().add(value, index);
        } 
    }
    
}
