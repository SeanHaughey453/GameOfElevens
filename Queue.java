public class Queue<T> implements QueueInterface<T> {
    private MyMove<T>front, rear;

    public Queue(){
        front = null;
        rear = null;

    }
    public void enqueue(T newEntry){
        MyMove<T> newMove = new MyMove<T>(newEntry);

        if(front == null){
            front = newMove;
            rear = newMove;
        } else {
            rear.setNext(newMove);
            rear = newMove;
        }
    }


    public T dequeue(){
        if(front == null) return null;
        else{
            T valueToReturn = front.getData();
            front = front.getNext();
            if(front == null) rear = null;
            return  valueToReturn;
        }

    }


    public T getFront(){
        if(front == null)return null;
        else return front.getData();
    }


    public boolean isEmpty(){
        return (front == null);
    }


    public void clear(){

        front = null;
        rear = null;
    }
}
