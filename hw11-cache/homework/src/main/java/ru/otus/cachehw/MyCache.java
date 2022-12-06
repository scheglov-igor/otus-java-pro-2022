package ru.otus.cachehw;


import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final WeakHashMap<K, V> cacheMap;
    private final List<HwListener<K, V>> hwListenerList;

    public MyCache() {
        this.cacheMap = new WeakHashMap<>();
        this.hwListenerList = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        this.cacheMap.put(key, value);
        hwListenerList.forEach(kvHwListener -> kvHwListener.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        this.cacheMap.remove(key);
        hwListenerList.forEach(kvHwListener -> kvHwListener.notify(key, null, "remove"));
    }

    @Override
    public V get(K key) {
        var v = this.cacheMap.get(key);
        hwListenerList.forEach(kvHwListener -> kvHwListener.notify(key, null, "get"));
        return v;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        this.hwListenerList.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        this.hwListenerList.remove(listener);
    }
}
