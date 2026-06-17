// импортируем утилитарный класс для работы с объектами
import java.util.Objects;

public class Person {
    // поля класса — имя и возраст
    private String name;
    private int age;

    // конструктор — создаёт объект с именем и возрастом
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // toString — красивый вывод объекта в консоль
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }

    // equals — сравниваем объекты по содержимому, не по адресу
    @Override
    public boolean equals(Object o) {
        // шаг 1: это тот же самый объект в памяти? сразу true
        if (this == o) return true;

        // шаг 2: o это null или другой класс? сразу false
        // getClass() возвращает класс объекта во время выполнения
        if (o == null || getClass() != o.getClass()) return false;

        // шаг 3: кастуем Object → Person чтобы получить доступ к полям
        Person person = (Person) o;

        // шаг 4: сравниваем поля
        // age это примитив int → используем ==
        // name это объект String → используем Objects.equals()
        // Objects.equals() безопасен — не падает если name == null
        return age == person.age && Objects.equals(name, person.name);
    }

    // hashCode — возвращает номер корзины для HashMap
    // ПРАВИЛО: если equals() true → hashCode() обязан быть одинаковым
    // Objects.hash() автоматически считает хэш по всем полям
    @Override
    public int hashCode() {
        // передаём те же поля что и в equals — name и age
        // если поля одинаковые → хэш одинаковый → HashMap найдёт объект
        return Objects.hash(name, age);
    }
}