package main.java.com.github.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class KeyValueComboBoxModel<M> extends AbstractListModel<Map.Entry<M, String>> implements ComboBoxModel<Map.Entry<M, String>>, Map<M, String> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1695128908958269551L;
	private TreeMap<M, String> values = new TreeMap<M, String>();
    private Map.Entry<M, String> selectedItem = null;
    
    public Map.Entry<M, String> getSelectedItem() {
        return selectedItem;
    }

    @SuppressWarnings("unchecked")
	public void setSelectedItem(Object anItem) {
    	
        this.selectedItem = (Map.Entry<M, String>) anItem;
        fireContentsChanged(this, -1, -1);
    }

    public void setSelectedItem(M key, String value){
    	Map.Entry<M, String> item = new MyEntry<M, String>(key, value);
        this.selectedItem = item;
    }
    
    public Map.Entry<M, String> getElementAt(int index) {
    	
		ArrayList<Map.Entry<M, String>> arrayList = new ArrayList<Map.Entry<M, String>>(values.entrySet());
		return arrayList.get(index);
		
	}

    public int getSize() {
        return values.size();
    }

    public void clear() {
        values.clear();
    }

    public boolean containsKey(Object key) {
        return values.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return values.containsValue(value);
    }

    public Set<Map.Entry<M, String>> entrySet() {
        return values.entrySet();
    }

    public String get(Object key) {
        return values.get(key);
    }

    public Set<M> keySet() {
        return values.keySet();
    }

    public String put(M key, String value) {
        return values.put(key, value);
    }

    public String remove(Object key) {
        return values.remove(key);
    }

    public int size() {
        return values.size();
    }

    public Collection<String> values() {
        return (Collection<String>) values.values();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void putAll(Map<? extends M, ? extends String>  options) {
        values.putAll(options);
    }

    
    final class MyEntry<K, V> implements Map.Entry<K, V> {
        private final K key;
        private V value;

        public MyEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }
    }
   
}
