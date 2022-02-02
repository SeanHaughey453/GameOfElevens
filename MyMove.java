public class MyMove<T> {

    private T data;
    private MyMove<T> next;

    public MyMove(T dataValue){
        data = dataValue;
        next = null;
    }

    public T getData() {
        return data;
    }

    public void setData(T dataValue){
        this.data = dataValue;
    }

    public  MyMove<T> getNext(){
        return next;
    }

    public void setNext(MyMove<T> nextNode) {
        this.next = nextNode;
    }
}
