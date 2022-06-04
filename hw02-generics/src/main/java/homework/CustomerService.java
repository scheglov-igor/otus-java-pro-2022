package homework;


import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    TreeMap<Customer, String> map = new TreeMap<>(new Comparator<>() {
        @Override
        public int compare(Customer o1, Customer o2) {
            return Long.compare(o1.getScores(), o2.getScores());
        }
    });

    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk;
        return copyOfEntry(map.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyOfEntry(map.higherEntry(customer));
    }

    private Map.Entry copyOfEntry(Map.Entry<Customer, String> entry) {
        Map.Entry<Customer, String> copyOfEntry = null;
        if(entry != null) {
            copyOfEntry = new AbstractMap.SimpleEntry<>(new Customer(entry.getKey()), entry.getValue());
        }
        return copyOfEntry;
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
