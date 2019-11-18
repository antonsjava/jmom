/*
 * Copyright 2018 Anton Straka
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
import sk.antons.json.parse.*;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;
import sk.antons.jmom.Jmom;
import sk.antons.json.JsonObject;
import sk.antons.json.JsonValue;
import sk.antons.json.match.SimplePathMatcher;

/**
 *
 * @author antons
 */
public class RemoveTest {
	private static Logger log = Logger.getLogger(RemoveTest.class.getName());
    
    private JsonObject parse() {
        JsonObject o = JsonParser.parse("{\"menu\": {\n" +
"  \"id\": \"file\",\n" +
"  \"value\": \"File\",\n" +
"  \"popup\": {\n" +
"    \"menuitem\": [\n" +
"      {\"value\": \"New\", \"onclick\": \"CreateNewDoc()\"},\n" +
"      {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"},\n" +
"      {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}\n" +
"    ]\n" +
"  }\n" +
"}}").asObject();
        return o;
    }
    
    @Test
	public void singleDelete() throws Exception {
        JsonObject o = parse();
        Assert.assertNotNull(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        Jmom jmom = Jmom.instance().remove("/menu/popup/menuitem/1");
        jmom.apply(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        JsonValue v = null;
        List<JsonValue> list = null;
        v = o.findFirst(SimplePathMatcher.instance("menu", "popup", "menuitem"));
        
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is array", v.isArray());
        Assert.assertEquals("size ", 2, v.asArray().size());
    }
    
    @Test
	public void singleDeleteHop() throws Exception {
        JsonObject o = parse();
        Assert.assertNotNull(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        Jmom jmom = Jmom.instance().remove("/menu/popup/menuitem/1/onclick|1");
        jmom.apply(o);
        System.out.println(" -- singleDeleteHop ----------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        JsonValue v = null;
        List<JsonValue> list = null;
        v = o.findFirst(SimplePathMatcher.instance("menu", "popup", "menuitem"));
        
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is array", v.isArray());
        Assert.assertEquals("size ", 2, v.asArray().size());
    }
    
    @Test
	public void doubleDelete() throws Exception {
        JsonObject o = parse();
        Assert.assertNotNull(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        Jmom jmom = Jmom.instance().remove("/menu/popup/menuitem/1", "/menu/id");
        jmom.apply(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        JsonValue v = null;
        List<JsonValue> list = null;
        v = o.findFirst(SimplePathMatcher.instance("menu", "popup", "menuitem"));
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is array", v.isArray());
        Assert.assertEquals("size ", 2, v.asArray().size());
        v = o.findFirst(SimplePathMatcher.instance("menu"));
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is object", v.isObject());
        Assert.assertNull("no id ",v.asObject().attr("id"));
    }
    
    @Test
	public void multyDelete() throws Exception {
        JsonObject o = parse();
        Assert.assertNotNull(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        Jmom jmom = Jmom.instance().remove("**/value");
        jmom.apply(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        JsonValue v = null;
        List<JsonValue> list = null;
        v = o.findFirst(SimplePathMatcher.instance("menu", "popup", "menuitem", "0"));
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is array", v.isObject());
        Assert.assertNull("no value ",v.asObject().attr("value"));
        v = o.findFirst(SimplePathMatcher.instance("menu"));
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is object", v.isObject());
        Assert.assertNull("no value ",v.asObject().attr("value"));
    }
}
