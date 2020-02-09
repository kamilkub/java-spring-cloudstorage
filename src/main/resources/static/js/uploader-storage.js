
/*
  @@ Getting all files from the server - all files
*/

function getAllFilesAndRender () {
    $.get("/user/all-files", (data, status) => {
        var renderData = "";
        for(var i = 0; i < data.length; i++) {
         renderData += "<tr>"+
                            "<td>"+data[i].fileName+"</td>"+
                            "<td>"+
                            "<a class='remove-click' data='"+data[i].fileName+"'>"+
                            "<img src='https://img.icons8.com/office/30/000000/delete-file.png'>"+
                            "</a>"+
                            "</td>"+
                            "<td>"+
                            "<a download>"+"<img src='https://img.icons8.com/ultraviolet/30/000000/download.png'>"+"</a>" +
                            "</td>"
                        +"</tr>"
        }
        $('.render-files').html(renderData);
        makeFilesRemovable();
    });
}

getAllFilesAndRender();

/*
   @@ Uploading AJAX Reuqest - single file upload

 */

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
   $("button[type='submit']").prop('disabled', false);
   $("input[type='file']").val('');
   getAllFilesAndRender();
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


 /*
    @@   Delete AJAX Request - By file name
 */

  function makeFilesRemovable() {
    $('.remove-click').click((e) => {
           e.preventDefault();
           let fileName = $(e.currentTarget).attr('data');

          if(confirm("Sure you want to delete this file?") && fileName != undefined){
              const deleteRequest = $.ajax({
                  url: '/user/delete-file/' + fileName,
                  type: 'POST',
                  cache: false,
                  contentType: false,
                  processData: false
               });

              deleteRequest.done((msg) => {
                  getAllFilesAndRender();
                   console.log("File deleted");
              });

              deleteRequest.fail((error) => {
                  alert(error.responseText);

                  if(error.status === 500){
                      alert('This file already exists');
                      $("input[type='file']").val('');
                  }
              });

              }
        });
  }










