package com.blog.blog.service;

import com.blog.blog.model.User;
import com.blog.blog.repository.BlogPostRepository;
import com.blog.blog.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BlogPostRepository blogPostRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final EmailService emailService;

    private static final String DIGITS = "0123456789"; // Набор символов для токена
    private static final int TOKEN_LENGTH = 6; // Длина токена
    public static final int USERS_PER_PAGE = 10;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param userRepository      Репозиторий для работы с пользователями.
     * @param blogPostRepository  Репозиторий для работы с постами.
     * @param passwordEncoder     Кодировщик паролей.
     * @param accountService      Сервис для работы с учетными записями.
     */
    @Autowired
    public UserService(UserRepository userRepository, BlogPostRepository blogPostRepository,
                       PasswordEncoder passwordEncoder, AccountService accountService,EmailService emailService) {
        this.userRepository = userRepository;
        this.blogPostRepository = blogPostRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.emailService = emailService;
    }

    /**
     * Регистрирует нового пользователя.
     *
     * @param user Пользователь для регистрации.
     * @throws IllegalArgumentException Если пароль не соответствует требованиям.
     */
    public void registerUser(User user) throws IllegalArgumentException {
        logger.info("Регистрация нового пользователя: {}", user.getUsername());
        existsByLogin(user.getUserLogin());

        validatePassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setAvatarUrl("/image/i.webp");
        user.setRole("Пользователь");
        user.setActive(true);

        // Генерация токена подтверждения email
        String token = UUID.randomUUID().toString();
        user.setEmailConfirmationToken(token);
        user.setEmailConfirmed(false);

        userRepository.save(user);

        // Отправка email с токеном
        sendEmail(user.getEmail(), token,  "Подтверждение email",
                "Для подтверждения email перейдите по ссылке: http://yourdomain.com/confirm-email?token=" + token);
        logger.info("Пользователь {} успешно зарегистрирован.", user.getUsername());
    }

    public void loginUser(String password, String userLogin) throws IllegalArgumentException {

       User user = findByUserLogin(userLogin);
       validatePassword(password, user.getPassword());

    }

    /**
     * Проверяет пароль на соответствие требованиям.
     *
     * @param password Пароль для проверки.
     * @throws IllegalArgumentException Если пароль не соответствует требованиям.
     */
    private void validatePassword(String password) {
        if (password.length() < 8) {
            logger.warn("Попытка регистрации с некорректным паролем.");
            throw new IllegalArgumentException("Пароль должен содержать минимум 8 символов.");
        }
    }

    /**
     * Возвращает список пользователей с пагинацией.
     *
     * @param page Номер страницы.
     * @return Список пользователей.
     */
    @Cacheable("users")
    public List<User> getUsers(int page) {
        logger.debug("Получение списка пользователей, страница: {}", page);
        return userRepository.findAll(PageRequest.of(page - 1, USERS_PER_PAGE)).getContent();
    }

    /**
     * Возвращает общее количество пользователей.
     *
     * @return Общее количество пользователей.
     */
    public int getTotalUsers() {
        logger.debug("Получение общего количества пользователей.");
        return (int) userRepository.count();
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param newUsername Новый логин пользователя (опционально).
     * @param newPassword Новый пароль пользователя (опционально).
     * @throws IllegalArgumentException Если пользователь не найден.
     */
    public void updateUser(String newUsername, String newPassword) {
        User user = getUserInfo(accountService.getUserName());
        if (user == null) {
            logger.warn("Попытка обновления данных несуществующего пользователя.");
            throw new IllegalArgumentException("Пользователь не найден.");
        }

        if (newUsername != null) {
            logger.info("Обновление логина пользователя {} на {}", user.getUsername(), newUsername);
            user.setUsername(newUsername);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            logger.info("Обновление пароля пользователя {}", user.getUsername());
            validatePassword(newPassword);
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
        logger.info("Данные пользователя {} успешно обновлены.", user.getUsername());
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param user Пользователь для обновления.
     */
    public void updateUser(User user) {
        logger.info("Обновление данных пользователя: {}", user.getUsername());
        userRepository.save(user);
    }

    /**
     * Возвращает информацию о текущем пользователе.
     *
     * @param currentUserLogin Логин текущего пользователя.
     * @return Информация о пользователе.
     */
    public User getUserInfo(String currentUserLogin) {
        logger.debug("Получение информации о пользователе: {}", currentUserLogin);
        return findByUserLogin(currentUserLogin);
    }

    /**
     * Проверяет, является ли пользователь администратором.
     *
     * @param currentUsername Логин текущего пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdmin(String currentUsername) {
        logger.debug("Проверка, является ли пользователь {} администратором.", currentUsername);
        User currentUser = getUserInfo(currentUsername);
        return currentUser != null && currentUser.isAdmin();
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @throws IllegalArgumentException Если пользователь не найден.
     */
    @Transactional
    public void deleteUser(Long id) {
        logger.info("Удаление пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Попытка удаления несуществующего пользователя с ID: {}", id);
            throw new IllegalArgumentException("Пользователь не найден.");
        }

        blogPostRepository.deleteByAuthorId(id);
        userRepository.deleteById(id);
        logger.info("Пользователь с ID {} успешно удален.", id);
    }

    public void validateUser(User user) {
        logger.debug("Валидация данных пользователя: {}", user.getUsername());
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            logger.warn("Попытка регистрации с пустым логином.");
            throw new IllegalArgumentException("Логин не может быть пустым.");
        }
        if (user.getEmail() == null || !user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            logger.warn("Попытка регистрации с некорректным email: {}", user.getEmail());
            throw new IllegalArgumentException("Некорректный email.");
        }
        validatePassword(user.getPassword());
    }

    public User findByEmail(String email) {
        logger.debug("Поиск пользователя по email: {}", email);
        return userRepository.findByEmail(email);
    }


    public void resetPassword(String email) {
        logger.info("Сброс пароля для пользователя с email: {}", email);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            logger.warn("Попытка сброса пароля для несуществующего email: {}", email);
            throw new IllegalArgumentException("Пользователь с таким email не найден.");
        }

        String temporaryPassword = generateTemporaryPassword();
        user.setPassword(passwordEncoder.encode(temporaryPassword));
        userRepository.save(user);

        //sendEmail(user.getEmail(), "Сброс пароля", "Ваш временный пароль: " + temporaryPassword);
        logger.info("Временный пароль отправлен на email: {}", email);
    }

    private String generateTemporaryPassword() {
        logger.debug("Генерация временного пароля.");
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void sendEmail(String email, String token, String subject, String message) {
        logger.debug("Отправка email на адрес: {}", email);
        emailService.sendEmail(email,token);
        // Логика отправки email
    }



    public void blockUser(Long userId) {
        logger.info("Блокировка пользователя с ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Попытка блокировки несуществующего пользователя с ID: {}", userId);
                    return new IllegalArgumentException("Пользователь не найден.");
                });
        user.setBlocked(true);
        userRepository.save(user);
        logger.info("Пользователь с ID {} успешно заблокирован.", userId);
    }

    public void unblockUser(Long userId) {
        logger.info("Разблокировка пользователя с ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Попытка разблокировки несуществующего пользователя с ID: {}", userId);
                    return new IllegalArgumentException("Пользователь не найден.");
                });
        user.setBlocked(false);
        userRepository.save(user);
        logger.info("Пользователь с ID {} успешно разблокирован.", userId);
    }

    public void assignRole(Long userId, String role) {
        logger.info("Назначение роли {} пользователю с ID: {}", role, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Попытка назначения роли несуществующему пользователю с ID: {}", userId);
                    return new IllegalArgumentException("Пользователь не найден.");
                });
        user.setRole(role);
        userRepository.save(user);
        logger.info("Роль {} успешно назначена пользователю с ID {}.", role, userId);
    }

    public void revokeRole(Long userId, String role) {
        logger.info("Отзыв роли {} у пользователя с ID: {}", role, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Попытка отзыва роли у несуществующего пользователя с ID: {}", userId);
                    return new IllegalArgumentException("Пользователь не найден.");
                });
        user.setRole(null);
        userRepository.save(user);
        logger.info("Роль {} успешно отозвана у пользователя с ID {}.", role, userId);
    }

    public void confirmEmail(String token) {
        logger.info("Подтверждение email с токеном: {}", token);
        User user = userRepository.findByEmailConfirmationToken(token);
        if (user == null) {
            logger.warn("Неверный токен подтверждения: {}", token);
            throw new IllegalArgumentException("Неверный токен подтверждения.");
        }

        // Подтверждаем email и удаляем токен
        user.setEmailConfirmed(true);
        user.setEmailConfirmationToken(null);
        userRepository.save(user);

        logger.info("Email пользователя {} успешно подтвержден.", user.getUsername());
    }

    private String generateConfirmationToken() {
        SecureRandom random = new SecureRandom();
        StringBuilder tokenBuilder = new StringBuilder(TOKEN_LENGTH);
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            int index = random.nextInt(DIGITS.length());
            tokenBuilder.append(DIGITS.charAt(index));
        }
        return tokenBuilder.toString();
    }

    public boolean existsByLogin(String userLogin) {
        Optional<User> user = userRepository.findByUserLogin(userLogin);
        if (user.isPresent()) {
            throw new UserNotFoundException("Пользователь c таким логином уже существеут");
        }
        return true;
    }

    public User findByUserLogin(String userLogin) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUserLogin(userLogin);
        if (user.isEmpty()) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user.orElse(null);
    }

    // Метод для проверки пароля
    public void validatePassword(String rawPassword, String encodedPassword) throws InvalidPasswordException {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new InvalidPasswordException("Неверный пароль");
        }
    }
}