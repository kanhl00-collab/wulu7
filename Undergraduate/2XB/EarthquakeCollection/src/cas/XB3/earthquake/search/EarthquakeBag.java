/**
 * Description: Modified Bag to iterate over a certain magnitude class of earthquakes
 * Original Description: The Bag class represents a bag (or multiset) of
 *                       generic items. It supports insertion and iterating over the
 *                       items in arbitrary order.
 * Link: https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Bag.java.html
 */
package cas.XB3.earthquake.search;

import java.util.Iterator;
import java.util.NoSuchElementException;

import cas.XB3.earthquake.ADT.EarthquakeT;

public class EarthquakeBag<Item> implements Iterable<Item>{
    private Node first;    // beginning of bag
    private Node firstpurple;
    private Node firstblue;
    private Node firstgreen;
    private Node firstyellow;
    private Node firstorange;
    private Node firstred;
    private int n;         // number of elements in bag
    private int npur;
    private int nblu;
    private int ngre;
    private int nyel;
    private int nora;
    private int nred;

    private class Node {
        private Item item;
        private Node next;
    }

    /**
     * Initializes an empty bag.
     */
    public EarthquakeBag() {
        first = null;
        firstpurple = null;
        firstblue = null;
        firstgreen = null;
        firstyellow = null;
        firstorange = null;
        firstred = null;

        n = 0;
        npur = 0;
        nblu = 0;
        ngre = 0;
        nyel = 0;
        nora = 0;
        nred = 0;
    }

    /**
     * Adds the item to this bag.
     * @param item the item to add to this bag
     */
    public void add(Item item) {
        Node oldfirst = first;
        EarthquakeT.ColorRating clr = null;
        first = new Node();
        first.item = item;
        first.next = oldfirst;

        try {
            EarthquakeT it = (EarthquakeT) item;
            clr = it.getColor();
            Node old = null;
            switch (clr){
                case PURPLE:
                    old = firstpurple;
                    firstpurple = new Node();
                    firstpurple.item = item;
                    firstpurple.next = old;
                    npur ++;
                    break;
                case BLUE:
                    old = firstblue;
                    firstblue = new Node();
                    firstblue.item = item;
                    firstblue.next = old;
                    nblu ++;
                    break;
                case GREEN:
                    old = firstgreen;
                    firstgreen = new Node();
                    firstgreen.item = item;
                    firstgreen.next = old;
                    ngre ++;
                    break;
                case YELLOW:
                    old = firstyellow;
                    firstyellow = new Node();
                    firstyellow.item = item;
                    firstyellow.next = old;
                    nyel ++;
                    break;
                case ORANGE:
                    old = firstorange;
                    firstorange = new Node();
                    firstorange.item = item;
                    firstorange.next = old;
                    nora ++;
                    break;
                case RED:
                    old = firstred;
                    firstred = new Node();
                    firstred.item = item;
                    firstred.next = old;
                    nred ++;
                    break;
            }
        } catch (NoSuchMethodError error){
            System.out.println("Item does not return colour");
        }
        n++;
    }

    /**
     * Removes an specific item from this bag.
     * @param item the item to be removed from this bag
     */
    public void del(Item item) {
        Iterator i = iterator();
        boolean found = false;
        while (i.hasNext()){
            if(item.equals(i.next())){
                i.remove();
                found = true;
                n--;
            }
        }
        String message = (found) ? "Item is successfully removed" : "Item is not found";
        System.out.println(message);
    }

    /**
     * Is this bag empty?
     * @return true if this bag is empty; false otherwise
     */
    public boolean isEmpty() {
        return first == null;
    }

    public boolean isEmptyC(EarthquakeT.ColorRating cl) {
        Node n1 = null;
        switch (cl) {
            case PURPLE:
                n1 = firstpurple;
                break;
            case BLUE:
                n1 = firstblue;
                break;
            case GREEN:
                n1 = firstgreen;
                break;
            case YELLOW:
                n1 = firstyellow;
                break;
            case ORANGE:
                n1 = firstorange;
                break;
            case RED:
                n1 = firstred;
                break;
        }
        return n1 == null;
    }

    /**
     * Returns the number of items in this bag.
     * @return the number of items in this bag
     */
    public int size() {
        return n;
    }

    public int sizeC(EarthquakeT.ColorRating cl) {
        int size = 0;
        switch (cl) {
            case PURPLE:
                size = npur;
                break;
            case BLUE:
                size = nblu;
                break;
            case GREEN:
                size = ngre;
                break;
            case YELLOW:
                size = nyel;
                break;
            case ORANGE:
                size = nora;
                break;
            case RED:
                size = nred;
                break;
        }
        return size;
    }

    /**
     * Returns an iterator that iterates over the items in the bag.
     */
    public Iterator<Item> iterator()  { return new ListIterator(); }

    public Iterator<Item> iterator(EarthquakeT.ColorRating cl)  {
        Node firstpoint = null;
        switch (cl) {
            case PURPLE:
                firstpoint = firstpurple;
                break;
            case BLUE:
                firstpoint = firstblue;
                break;
            case GREEN:
                firstpoint = firstgreen;
                break;
            case YELLOW:
                firstpoint = firstyellow;
                break;
            case ORANGE:
                firstpoint = firstorange;
                break;
            case RED:
                firstpoint = firstred;
                break;
        }
        return new ListIterator(firstpoint);
    }

    // an iterator over a linked list
    private class ListIterator implements Iterator<Item> {
        private Node current;
        private Node previous;
        private Node previousprevious;
        private boolean hasremoved = false;

        // creates a new iterator
        public ListIterator() {
            current = first;
        }

        public ListIterator(Node firstpt) {
            current = firstpt;
        }

        // is there a next item in the iterator?
        public boolean hasNext() {
            return current != null;
        }

        // this method is optional in Iterator interface
        public void remove() {
            if(hasremoved) throw new IllegalStateException("must call next() before calling remove()");
            if (current == null){
                previousprevious.next = null;
            }
            else {
                previous.item = current.item;
                previous.next = current.next;
            }
            hasremoved = true;
        }

        // returns the next item in the iterator (and advances the iterator)
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            previousprevious = previous;
            previous = current;
            Item item = current.item;
            current = current.next;
            hasremoved = false;
            return item;
        }
    }

}
