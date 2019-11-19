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
package sk.antons.jmom;

import java.util.ArrayList;
import java.util.List;
import sk.antons.jmom.rule.AddRule;
import sk.antons.jmom.rule.ApplyRule;
import sk.antons.jmom.rule.CopyRule;
import sk.antons.jmom.rule.KeepOnlyRule;
import sk.antons.jmom.rule.RemoveRule;
import sk.antons.jmom.rule.Rule;
import sk.antons.json.JsonValue;

/**
 * Class collects list of rules, which transforms json objects. 
 * @author antons
 */
public class Jmom implements Rule {

    List<Rule> rules = new ArrayList<Rule>();
    
    private Jmom() {}
    
    public static Jmom instance() {
        return new Jmom();
    }

    @Override
    public void apply(JsonValue json) {
        for(Rule rule : rules) {
            rule.apply(json);
        }
    }
    
    /**
     * Add rule to list.
     * @param rule
     * @return this
     */
    public Jmom rule(Rule rule) {
        if(rule != null) rules.add(rule);
        return this;
    }
    
    /**
     * Add apply rule to list.
     * @param rule role to apply
     * @param path list of paths where rule must be applied
     * @return this
     */
    public Jmom apply(Rule rule, String... path) {
        rules.add(ApplyRule.instance(rule, path));
        return this;
    }

    /**
     * Add remove rule to list
     * @param path list od paths which must be removed
     * @return this
     */
    public Jmom remove(String... path) {
        rules.add(RemoveRule.instance(path));
        return this;
    }
    
    /**
     * Add keepOnly rule to list
     * @param path list of paths, which must be saved (other will be removed)
     * @return this
     */
    public Jmom keepOnly(String... path) {
        rules.add(KeepOnlyRule.instance(path));
        return this;
    }
    
    /**
     * Add add rule to list.
     * @param path one path where a value must be added
     * @param value value to be added
     * @param replace true if value must replace existing value on path
     * @return this
     */
    public Jmom add(String path, JsonValue value, boolean replace) {
        rules.add(AddRule.instance(path, value, replace));
        return this;
    }
    
    /**
     * Add copy rule to list 
     * @param frompath one path selecting source value
     * @param topath one exact path selecting target 
     * @param move true if value should be removed from source
     * @return this
     */
    public Jmom copy(String frompath, String topath, boolean move) {
        rules.add(CopyRule.instance(frompath, topath, move));
        return this;
    }

    

    
    
    
}
