// This abstract class defines a symbol table, such as a hash table. It 
// is pretty close to being an interface, save for a couple of simple methods 
// near the end.

abstract class SymbolTable<K,V> {

    // return key's value
    abstract public Object get(K key);

    // remove key-value pair
    abstract public void delete(K key);	

    // how many pairs?
    abstract public int size();	

    // get *all* the keys
    abstract public Iterable<K> keys();	

    // is table empty?
    public boolean isEmpty() { return (size()==0); }

	public boolean contains(K key) {
		// TODO Auto-generated method stub
		return get(key)!=null;
	}

	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}
}