import java.util.HashMap;
import java.util.Map;

public class CollectionsCount {
    public static <T> Map<T, Integer> elementCount(T[] array) {
        Map<T, Integer> elementCountMap = new HashMap<>();

        for (T element : array) {
            elementCountMap.put(element, elementCountMap.getOrDefault(element, 0) + 1);
        }
        return elementCountMap;
    }

    public static void main(String[] args) {
        Integer[] number = {1, 2, 3, 4, 5, 4, 4, 2, 3, 4};

        Map<Integer, Integer> result = elementCount(number);

        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}