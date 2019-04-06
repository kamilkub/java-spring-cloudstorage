
$("button[type='submit']").click((e) => {

  e.preventDefault();

  $(this).prop("disabled", true);

  var uploadForm = document.forms[0];
  var uploadData = new FormData(uploadForm);

 const uploadRequest = $.ajax({
       url: '/user/storage/upload',
       type: 'POST',
       data: uploadData,
       cache: false,
       contentType: false,
       processData: false
 });


uploadRequest.done((msg) => {
   $("button[type='submit']").prop('disabled', true);
   $("input[type='file']").val('');
   alert("File uploaded");
});

uploadRequest.fail((error) => {
   $("button[type='submit']").prop('disabled', false);
   alert(error.responseText);

   if(error.status === 500){
      alert('This file already exists');
      $("input[type='file']").val('');
   }

});

});





