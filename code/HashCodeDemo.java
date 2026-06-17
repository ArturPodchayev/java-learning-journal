// импортируем HashMap из стандартной библиотеки Java
import java.util.HashMap;

public class HashCodeDemo {

    public static void main(String[] args) {

        // создаём два объекта с одинаковыми полями
        // но это РАЗНЫЕ объекты в памяти — разные адреса
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Alice", 30);

        System.out.println("=== Без hashCode ===");

        // equals вернёт true — поля одинаковые
        System.out.println("equals: " + p1.equals(p2));

        // hashCode показывает "номер корзины" в HashMap
        // без переопределения — у каждого объекта свой случайный номер
        System.out.println("hashCode p1: " + p1.hashCode());
        System.out.println("hashCode p2: " + p2.hashCode());

        // создаём HashMap — ключ Person, значение String
        HashMap<Person, String> map = new HashMap<>();

        // кладём p1 в map со значением "engineer"
        // HashMap вычисляет hashCode(p1) и кладёт в эту корзину
        map.put(p1, "engineer");

        // ищем по p2 — HashMap вычисляет hashCode(p2)
        // если hashCode другой — ищет в другой корзине и не находит
        // если hashCode одинаковый — находит корзину, потом проверяет equals
        System.out.println("ищем p2 в map: " + map.get(p2));
    }
}