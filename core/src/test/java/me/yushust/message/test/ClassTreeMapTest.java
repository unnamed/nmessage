package me.yushust.message.test;

import me.yushust.message.util.ClassTreeMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClassTreeMapTest {

  @Test
  public void test() {

    ClassTreeMap<String> tree = new ClassTreeMap<>();

    tree.put(String.class, "");
    tree.put(CharSequence.class, "for char sequence");
    tree.put(ClassTreeMap.class, "");
    tree.put(Map.class, "for map");
    tree.put(HashMap.class, "for hash map");
    tree.put(LinkedHashMap.class, "for linked hash map");
    tree.put(Set.class, "");
    tree.put(HashSet.class, "");
    tree.put(Collection.class, "");
    tree.put(List.class, "");
    tree.put(ArrayList.class, "");

    Assertions.assertEquals("for linked hash map", tree.get(LinkedHashMap.class));
    Assertions.assertEquals("for map", tree.get(ConcurrentHashMap.class));
    Assertions.assertEquals("for char sequence", tree.get(StringBuilder.class));
  }

}
