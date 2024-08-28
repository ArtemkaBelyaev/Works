interface Filter {
    Object apply(Object o);
}

class ExampleFilter implements Filter {
    @Override
    public Object apply(Object o) {
        if (o instanceof String) {
            return o + "!!";
        }
        return o;
    }
}

public class CollectionWork {
    public static <T> T[] filter(T[] array, Class<? extends Filter> filterClass) {
        T[] newArray = array.clone();
        try {
            Filter filterInstance = filterClass.getDeclaredConstructor().newInstance();
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = (T) filterInstance.apply(newArray[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newArray;
    }

    public static void main(String[] args) {
        String[] array = {"Java", "Code", "OneLove"};

        String[] newArray = filter(array, ExampleFilter.class);

        for (String s : newArray) {
            System.out.println(s);
        }
    }
}