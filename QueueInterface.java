public interface QueueInterface<T> {

    public void enqueue(T newEntry);
        /*
        add a new entry to the back of the queue
         */

    public T dequeue();
        /*
        remove entry from the front of the queue
         */

    public T getFront();
        /*
        return but dont remove the entry from the queue
         */

    public boolean isEmpty();
    /* returns true is the queue is empty, false otherwise
     */

    public void clear();
    /*removes all entries from the queue.
     */

}
