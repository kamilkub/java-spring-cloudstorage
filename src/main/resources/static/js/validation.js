
$('.signup-password').on('change', function(){

  const passwordVal = $(this).val();

  if(passwordVal.length >= 6){
     alert('Password must be 6 characters long');
  }


});

