package com.metanit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMapIterator implements Iterator {
    Iterator it;
    Integer iterations;
    HashMapIterator(HashMap map){
        it =  map.entrySet().iterator();
        iterations = 0;
    }
    public boolean hasNext() {
        return it.hasNext();
    }

    public Object next(){
            iterations++;
            try{
                return it.next();
            }
            catch(NoSuchElementException ex){
                iterations--;
                throw new NoSuchElementException("iteration has no more elements");
            }
    }
    public void remove(){
        try {
            it.remove();
        }
        catch (IllegalStateException x){
            throw new IllegalStateException("next method has not yet been called, or the remove method has already been called after the last call to the next method\n" + "Iterations count: " + iterations);
        }
    }
}
