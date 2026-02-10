public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();

        // Добавление элементов
        map.put("apple", 10);
        map.put("banana", 20);
        map.put("orange", 30);

        // Получение значений
        System.out.println(map.get("apple"));
        System.out.println(map.get("banana"));

        // Обновление значения
        map.put("apple", 15);
        System.out.println(map.get("apple"));

        // Удаление элемента
        map.remove("banana");
        System.out.println(map.get("banana"));

        // Проверка существования ключа
        System.out.println(map.containsKey("orange"));

        // Размер map
        System.out.println(map.size());
    }
}
