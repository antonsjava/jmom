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
package sk.antons.jmom.util;

import java.util.ArrayList;
import java.util.List;
import sk.antons.json.JsonArray;
import sk.antons.json.JsonObject;
import sk.antons.json.JsonValue;
import sk.antons.json.impl.JsonObjectImpl;
import static sk.antons.json.match.wild.WPM.path;

/**
 *
 * @author antons
 */
public class PathUtil {

    public static List<String> elements(String path) {
        List<String> list = new ArrayList<String>();
        if(path != null) {
            StringBuilder sb = new StringBuilder();
            int len = path.length();
            for(int i = 0; i < len; i++) {
                char c = path.charAt(i);
                if(c == '\\') {
                } else if(c == '/') {
                    if(sb.length()>0) {
                        list.add(sb.toString());
                    }
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }
            if(sb.length()>0) {
                list.add(sb.toString());
            }
        }
        return list;
    }
    
    public static JsonValue mkdirsParent(List<String> elements, JsonValue value) {
        if(value == null) return null;
        if(value.isLiteral()) return null;
        if(elements == null) return null;
        int len = elements.size();
        if(len == 1) return value;
        len--; // parent
        for(int i = 0; i < len; i++) {
            String element = elements.get(i);
            if(value == null) {
                return null;
            } if(value.isArray()) {
                JsonArray array = value.asArray();
                int index = -1;
                try {
                    index = Integer.parseInt(element);
                } catch(Exception e) {
                    return null;
                }
                if(value.asArray().size() > index) {
                    value = array.get(index);
                } else {
                    JsonObject o = new JsonObjectImpl();
                    array.add(o);
                    value = o;
                }
            } else {
                JsonObject object = value.asObject();
                JsonValue v = object.first(element);
                if(v == null) {
                    JsonObject o = new JsonObjectImpl();
                    object.add(element, o);
                    v = o;
                }
                value = v;
            }
        }
        return value;
    }
    
    public static JsonValue resolveDirect(List<String> elements, JsonValue value) {
        if(value == null) return null;
        if(elements == null) return value;
        int len = elements.size();
        if(len == 1) return value;
        for(int i = 0; i < len; i++) {
            String element = elements.get(i);
            if(value == null) {
                return null;
            } if(value.isArray()) {
                JsonArray array = value.asArray();
                int index = -1;
                try {
                    index = Integer.parseInt(element);
                } catch(Exception e) {
                    if("~".equals(element)) index = array.size()-1;
                    else return null;
                }
                if(value.asArray().size() > index) {
                    value = array.get(index);
                } else {
                    return null;
                }
            } else {
                JsonObject object = value.asObject();
                JsonValue v = object.first(element);
                if(v == null) return null;
                value = v;
            }
        }
        return value;
    }    
}
