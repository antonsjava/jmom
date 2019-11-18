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
import sk.antons.json.JsonFactory;
import sk.antons.json.JsonObject;
import sk.antons.json.JsonValue;
import sk.antons.json.match.SimplePathMatcher;

/**
 *
 * @author antons
 */
public class AddTest {
	private static Logger log = Logger.getLogger(AddTest.class.getName());
    
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
	public void singleAdd() throws Exception {
        JsonObject o = parse();
        Assert.assertNotNull(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        Jmom jmom = Jmom.instance()
            .add("/menu/name", JsonFactory.stringLiteral("nove mneo"), true)
            .add("/menu/popup/menuitem/2", JsonParser.parse("{\"value\": \"SaveAs\", \"onclick\": \"SaveAs()\"}"), true)
            ;
        jmom.apply(o);
        System.out.println(" ------------------------");
        System.out.println(o.toPrettyString("  "));
        System.out.println(" ------------------------");
        JsonValue v = null;
        List<JsonValue> list = null;
        v = o.findFirst(SimplePathMatcher.instance("menu", "popup", "menuitem"));
        
        Assert.assertNotNull("first menu", v);
        Assert.assertTrue("is array", v.isArray());
        Assert.assertEquals("size ", 4, v.asArray().size());
    }
    
}
