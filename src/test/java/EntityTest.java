
import org.example.Entity;
import org.example.Annotations.ToString;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

// Класс тестирования для метода toString() в Entity
public class EntityTest {

    // Тестируемый класс User, наследующий от Entity
    public static class User extends Entity {
        @ToString("YES")
        private String username;

        @ToString(value = "NO")
        private int age;

        public User(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

    // Тест 1: Проверка правильного отображения полей с аннотацией YES
    @Test
    public void testToStringWithYesAnnotation() {
        User user = new User("Alice", 30);
        String expected = "User{username=Alice}";
        String actual = user.toString();
        assertEquals(expected, actual, "Метод toString() должен возвращать строку с полем username.");
    }

    // Тест 2: Проверка случая, когда все поля помечены NO
    public static class UserNoFields extends Entity {
        @ToString(value = "NO")
        private String username;

        @ToString(value = "NO")
        private int age;

        public UserNoFields(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

    @Test
    public void testToStringWithNoFields() {
        UserNoFields user = new UserNoFields("Bob", 25);
        String expected = "UserNoFields{}";
        String actual = user.toString();
        assertEquals(expected, actual, "Метод toString() должен возвращать пустую строку, если все поля помечены NO.");
    }

    // Тест 3: Проверка работы с полями без аннотации
    public static class UserMixedFields extends Entity {
        @ToString(value = "YES")
        private String username;

        private int age; // Поле без аннотации

        public UserMixedFields(String username, int age) {
            this.username = username;
            this.age = age;
        }
    }

    @Test
    public void testToStringWithMixedFields() {
        UserMixedFields user = new UserMixedFields("Charlie", 40);
        String expected = "UserMixedFields{username=Charlie,age=40}";
        String actual = user.toString();
        assertEquals(expected, actual, "Метод toString() должен включать поля без аннотации и с аннотацией YES.");
    }
}
