
# jmom

 Mom is woman, which tries to modify her son to be nicer.

 jmom is java library, which tries to modify json to be nicer.

## Base of usage

 The library is based on antonsjava.json library, which is base for json
 manipulation. Json data are provided as instances of json API.

 Creating json API object from String.
```java
  String jsonvalue = .....
  JsonValue json = JsonParser.parse(jsonvalue);
```
 Creating jmom API object for manipulating json.
```java
  Jmom jmom = Jmom.instance()
            .xxxxx() //a modification rule
            .xxxxx() //a modification rule
            .xxxxx() //a modification rule
            ;
```
 Modifying json using jmom API.
```java
  jmom.apply(json);
```
 Converting json back to String.
```java
  String newjsonvalue = json.toCompactString();
```

## Json data pointers

 Modification rules from Jmom API must address some parts of json to be modified.
 The library uses WildPathMatcher syntax for addressing subparts of json.
 Individual path elements can be
  - simple names like "foo", "bar"
  - wild names like "foo-*", "bar-?" 
  - "**" for representing sequence of path elements	 

 So pointers can look like
  - "/foo/bar/item" represents only "/foo/bar/item"
  - "/foo/*/item" represents paths like "/foo/bar/item", "/foo/nobar/item", ....
  - "/foo/**/item" represents paths like "/foo/item", "/foo/bar/item", "/foo/bar/bar/item"....

 Data pointer can be postfixed with '|{int number}'. It moves path selected jsno node to parent 
 {int number} times. For example "**/config" selects all paths which ends with '/config' but 
 "**/config|1" selects direct parents of previous selections.

## Modification rules

### Add

 Add rule allows to add specified json value to given json. It has following parameters
  - path - exact path where new json must be inserted. (no wilds are allowed here)
  - jsonvalue - value tu be inserted into json
  - replace - true is old value should be replaced is exists. (default: false)

 Examples:
```java
   Jmom jmom = Jmom.instance()
     .add("/menu/name", JsonFactory.stringLiteral("nove mneo"), true)
     .add("/menu/popup/menuitem/2", JsonParser.parse("{\"value\": \"SaveAs\", \"onclick\": \"SaveAs()\"}"), true)
   ;
```

### Remove

 Remove rule allows to remove specified parts from given json. It has following parameters
  - path - list of data pointers exact path where new json must be inserted.

 Examples:
```java
   Jmom jmom = Jmom.instance()
     .remove("/menu/name-?", "**/config|1")
   ;
```

### KeepOnly

 KeepOnly rule allows to remove all parts from given json except specified. It has following parameters
  - path - list of data pointers exact path where new json must be saved.

 Examples:
```java
   Jmom jmom = Jmom.instance()
     .keepOnly("/menu/name-?", "**/config|1")
   ;
```
### Copy

 Copy rule allows to copy specified json part to different place of json. It has following parameters
  - frompath - path which must be copied. (first part is used if more can be identified)
  - topath - exact path where from json must be inserted. (no wilds are allowed here)
  - move - true is old part should be deleted (default: false)

 Examples:
```java
   Jmom jmom = Jmom.instance()
     .copy("/menu/id", "/menu/ctx/id", true)
   ;
```

### Apply

 Apply rule allows to apply a rule on chosen subparts of given json. The rule can be 
 anything. 
  - rule - any rule to be aplied.
  - path - list of data pointers exact path where rule must be applied

 Example: (for each objects containing "street" attribute create new one "streetFull" )
```java
   Jmom jmom = Jmom.instance()
     .apply((json) -> {
                String street = json.asObject().first("street");
                String streetnum = json.asObject().first("streetNo");
                json.asObject().attr("streetFull", street+" "+streetNo);
            }, "**/street|1")
   ;
```

## Maven usage

```
   <dependency>
      <groupId>com.github.antonsjava</groupId>
      <artifactId>jmom</artifactId>
      <version>1.1</version>
   </dependency>
```
