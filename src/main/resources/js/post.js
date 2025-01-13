<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
    $('#commentForm').on('submit', function(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение формы

        var formData = $(this).serialize(); // Собираем данные формы

        $.ajax({
            type: 'POST',
            url: $(this).attr('action'), // URL из атрибута action формы
            data: formData,
            success: function(comment) {
                // Добавляем новый комментарий в список
                var newComment = '<div class="comment"><p><strong>Вы</strong>: <span>' + comment.content + '</span></p></div>';
                $('.comments-list').append(newComment); // Добавляем комментарий в список
                $('#commentContent').val(''); // Очищаем поле ввода
            },
            error: function(xhr, status, error) {
                console.error('Ошибка при отправке комментария:', error);
            }
        });
    });
});
</script>
