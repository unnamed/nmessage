package me.yushust.message.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A tree of {@link Class} as keys and
 * {@code T} as value. A relation of Class-T
 * that supports inheritance.
 *
 * <p>This can be util for type registries</p>
 *
 * @author Yushu
 */
public class ClassTreeMap<T> {

  private Node<T> root;

  private Node<T> inspect(Node<T> node, Class<?> clazz) {
    if (node.clazz == clazz) {
      return node;
    } else if (node.clazz.isAssignableFrom(clazz)) {
      for (Node<T> child : node.childs.values()) {
        Node<T> found = inspect(child, clazz);
        if (found != null) {
          return found;
        }
      }
      return node;
    } else {
      return null;
    }
  }

  public T get(Class<?> clazz) {
    Node<T> node = inspect(root, clazz);
    if (node == null) {
      return null;
    } else {
      return node.value;
    }
  }

  public void put(Class<?> clazz, T value) {
    Objects.requireNonNull(clazz);
    if (root == null) {
      root = new Node<>(null, clazz, value);
    } else {
      addNode(root, new Node<>(null, clazz, value));
    }
  }

  private void set(Node<T> parent, Node<T> replace) {
    Node<T> old = parent.addChild(replace);
    if (old != null) {
      // move the child nodes
      replace.childs = old.childs;
    }
  }

  private Node<T> getMostSpecificChild(Node<T> node, Class<?> clazz) {

    Map<Class<?>, Node<T>> childs = node.childs;
    Class<?> nodeClass = node.clazz;
    Class<?> checking = clazz;

    do {
      for (Map.Entry<Class<?>, Node<T>> entry : childs.entrySet()) {
        Class<?> entryType = entry.getKey();
        if (entryType == clazz || entryType.isAssignableFrom(checking)) {
          return getMostSpecificChild(
              entry.getValue(),
              checking
          );
        }
      }
      checking = checking.getSuperclass();
    } while (checking != null && nodeClass.isAssignableFrom(checking));

    return node;
  }

  private void addNode(Node<T> node, Node<T> adding) {
    Class<?> currentClass = node.clazz;
    Class<?> addingClass = adding.clazz;

    if (addingClass.isAssignableFrom(currentClass)) {

      // move the adding child nodes to another more-specific
      // level if they are compatible
      adding.childs.entrySet().removeIf(entry -> {
        Class<?> childType = entry.getKey();
        Node<T> childNode = entry.getValue();

        boolean move = currentClass.isAssignableFrom(childType);
        if (move) {
          node.addChild(childNode);
        }
        return move;
      });

      // current is sub-type of clazz, so. move "current" down
      adding.addChild(node);

      // move the added node to a up level
      adding.unlinkFromParent();
      if (node.parent != null) {
        adding.parent = node.parent;
        addNode(node.parent, adding);
      } else {
        // so the given node is the root
        root = adding;
      }

      node.unlinkFromParent();
      node.parent = adding;
    } else if (currentClass.isAssignableFrom(addingClass)) {
      // current class is a super-class of adding class
      // adding class must be a child of current class
      set(getMostSpecificChild(node, addingClass), adding);
    } else {
      // same level, so, add the node to the parent node
      if (node.parent == null) {
        // no parent, it's the root
        // -> Types aren't similar, we need to create a new
        //    root containing the nodes
        root = new Node<>(null, Object.class, null);
        root.addChild(node);
        root.addChild(adding);
      } else {
        addNode(node.parent, adding);
      }
    }
  }

  /** Represents a tree node */
  private static class Node<T> {

    private final Class<?> clazz;
    private final T value;

    private Node<T> parent;
    private Map<Class<?>, Node<T>> childs
        = new HashMap<>();

    private Node(
        Node<T> parent,
        Class<?> clazz,
        T value
    ) {
      this.parent = parent;
      this.clazz = clazz;
      this.value = value;
    }

    void unlinkFromParent() {
      if (parent != null) {
        parent.childs.remove(clazz);
      }
    }

    Node<T> addChild(Node<T> node) {
      return childs.put(node.clazz, node);
    }

  }

}
