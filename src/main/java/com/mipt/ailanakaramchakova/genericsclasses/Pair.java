package com.mipt.ailanakaramchakova.genericsclasses;

public class Pair<K, V> {

  private K key;
  private V value;

  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public void setKey(K key) {
    this.key = key;
  }

  public V getValue() {
    return value;
  }

  public void setValue(V value) {
    this.value = value;
  }

  public Pair<V, K> swap() {
    return new Pair<>(value, key);
  }

  @Override
  public String toString() {
    return "Pair{key=" + key + ", value=" + value + "}";
  }
}
