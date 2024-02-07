document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('percentage').value = '100';
  });

setTimeout(function () {
      $(".alert").slideUp(500, function() {
          $(this).remove();
      });
  }, 3000);






