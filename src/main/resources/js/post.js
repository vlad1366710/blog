<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
    $('#commentForm').on('submit', function(event) {
        event.preventDefault(); // ������������� ����������� ��������� �����

        var formData = $(this).serialize(); // �������� ������ �����

        $.ajax({
            type: 'POST',
            url: $(this).attr('action'), // URL �� �������� action �����
            data: formData,
            success: function(comment) {
                // ��������� ����� ����������� � ������
                var newComment = '<div class="comment"><p><strong>��</strong>: <span>' + comment.content + '</span></p></div>';
                $('.comments-list').append(newComment); // ��������� ����������� � ������
                $('#commentContent').val(''); // ������� ���� �����
            },
            error: function(xhr, status, error) {
                console.error('������ ��� �������� �����������:', error);
            }
        });
    });
});
</script>
