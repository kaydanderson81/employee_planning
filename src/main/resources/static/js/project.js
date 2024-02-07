
$(' #deleteButton').on('click', function(event) {
    event.preventDefault();
    var href = $(this).attr('href');
   $('#deleteModal #delRef').attr('href', href);
   $('#deleteModal').modal();

});

setTimeout(function () {
    $("#alert").slideUp(500, function() {
        $(this).remove();
    });
}, 3000);

setTimeout(function () {
    $("#success").slideUp(500, function() {
        $(this).remove();
    });
}, 4000);

setTimeout(function () {
    $("#fail").slideUp(500, function() {
        $(this).remove();
    });
}, 4000);
