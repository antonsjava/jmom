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
public class CopyRule extends PathRule implements Rule {
    
    String topath;
    boolean move = false;
    
    public CopyRule(String frompath, String topath, boolean move) {
        super(new String[]{frompath});
        this.topath = topath;
        this.move = move;
    }

    public static CopyRule instance(String frompath, String topath, boolean move) {
        return new CopyRule(frompath, topath, move);
    }

    private List<String> toelements = null;
    private List<String> toelements() {
        if(toelements == null) toelements = PathUtil.elements(topath);
        return toelements;
    };

    @Override
    public void apply(JsonValue json) {
        JsonValue fromvalue = firstPathResult(json);
        if(fromvalue == null) return;
        List<String> toelems = toelements();
        JsonValue parent = PathUtil.mkdirsParent(toelems, json);
        if(parent == null) return;
        String lastelem = toelems.get(toelems.size()-1);
        if(move) fromvalue.remove();
        if(fromvalue.parent() != null) fromvalue = fromvalue.copy();
        if(parent.isObject()) {
            JsonValue v = parent.asObject().first(lastelem);
            if(v == null) parent.asObject().add(lastelem, fromvalue);
            else v.replaceBy(fromvalue);
        } else if(parent.isArray()) {
            int index = -1;
            try {
                index = Integer.parseInt(lastelem);
            } catch(Exception e) {
                index = parent.asArray().size();
            }
            if(index < 0) index = 0;
            if(index >= parent.asArray().size()) parent.asArray().add(fromvalue);
            else parent.asArray().add(fromvalue, index);
        } 
    }
    
}
