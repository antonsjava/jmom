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
import sk.antons.json.JsonValue;

/**
 * Apply rule on specified path to given json object. 
 *
 * @author antons
 */
public class ApplyRule extends PathRule implements Rule {
    Rule rule;
    public ApplyRule(Rule rule, String... path) {
        super(path);
        this.rule = rule;
    }

    public static ApplyRule instance(Rule rule, String... path) {
        return new ApplyRule(rule, path);
    }

    @Override
    public void apply(JsonValue json) {
        List<JsonValue> values = allPathResults(json);
        for(JsonValue value : values) {
            rule.apply(value);
        }
    }
    
    
}
