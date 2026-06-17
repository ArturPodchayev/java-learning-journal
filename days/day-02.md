# День 2 — 17 июня 2026

## что делал
читал Item 10 Effective Java (equals), разбирал контракт по каждому пункту, понял почему наследование ломает equals и как это решается

---

## что я понял

### зачем вообще переопределять equals()
- по умолчанию `equals()` сравнивает адреса в памяти — как `==`
- два объекта с одинаковыми полями дадут `false` без переопределения
- переопределяем когда хотим сравнивать по содержимому, а не по адресу

---

### контракт equals() — 5 правил которые нельзя нарушать

**1. Рефлексивность**
- `a.equals(a)` всегда должно быть `true`
- объект всегда равен самому себе
- нарушить случайно почти невозможно
- но если нарушишь — `list.contains(a)` вернёт `false` даже если `a` есть в списке

**2. Симметричность**
- если `a.equals(b) == true`, то `b.equals(a)` тоже должно быть `true`
- всегда работает в обе стороны
- легко сломать если один класс знает про другой, а тот — нет
- пример из книги: `CaseInsensitiveString` знает про `String`, но `String` не знает про `CaseInsensitiveString` → симметрия сломана

**3. Транзитивность**
- если `a==b` и `b==c`, то `a==c` обязательно
- самое сложное правило
- ломается когда наследуешь класс и добавляешь новое поле
- пример: `Point(1,2)` и `ColorPoint(1,2,RED)` и `ColorPoint(1,2,BLUE)`
  - `p1(RED) == p2(Point)` → `true` (игнорируем цвет)
  - `p2(Point) == p3(BLUE)` → `true` (игнорируем цвет)
  - `p1(RED) == p3(BLUE)` → `FALSE` 😱 (цвета разные)
  - математика сломана: если `p1==p2` и `p2==p3`, то `p1` обязан `== p3`
- вывод из книги: невозможно наследоваться и добавить новое поле не сломав контракт

**4. Консистентность**
- вызываешь `equals` 100 раз — получаешь одинаковый результат если объект не менялся
- нельзя писать `equals` который зависит от сети или БД
- пример плохого кода: `java.net.URL` сравнивает IP адреса через сеть → IP может поменяться → результат меняется
- правило: `equals` работает только с данными в памяти

**5. Non-null**
- `a.equals(null)` всегда возвращает `false`
- не нужно писать явную проверку `if (o == null) return false`
- `instanceof` сам возвращает `false` если аргумент `null`
- правильно: `if (!(o instanceof Person)) return false` — это уже покрывает `null`

---

### рецепт правильного equals — 4 шага

```java
@Override
public boolean equals(Object o) {
    // 1. тот же объект в памяти?
    if (o == this) return true;

    // 2. правильный тип? (null тоже вернёт false автоматически)
    if (!(o instanceof Person)) return false;

    // 3. кастуем
    Person p = (Person) o;

    // 4. сравниваем поля
    // примитивы (int, boolean) → ==
    // объекты (String и тд) → .equals()
    return age == p.age && name.equals(p.name);
}
```

---

### почему наследование ломает equals — три попытки и все три сломались

- **попытка 1:** игнорируем новое поле → теряем смысл (`RED == BLUE`)
- **попытка 2:** учитываем новое поле → ломается симметрия
- **попытка 3:** умный equals → ломается транзитивность
- **вывод:** это математически невозможно решить через наследование

**решение — композиция:**
- не `ColorPoint extends Point`
- а `ColorPoint` содержит `Point` как поле внутри
- два отдельных типа — никакой путаницы

```java
public class ColorPoint {
    private final Point point;  // ← не наследуем, а храним внутри
    private final Color color;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ColorPoint)) return false;
        ColorPoint cp = (ColorPoint) o;
        return cp.point.equals(point) && cp.color.equals(color);
    }
}
```

---

### главные строчки из книги которые запомнил

> *"Once you've violated the equals contract, you simply don't know how other objects will behave when confronted with your object."*

> *"There is no way to extend an instantiable class and add a value component while preserving the equals contract."*

> *"Do not write an equals method that depends on unreliable resources."*
