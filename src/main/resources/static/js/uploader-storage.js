
/*
  @@ Getting all files from the server - all files
*/

function getAllFilesAndRender () {
    $.get("/user/all-files", (data, status) => {
        var renderData = "";
        var fileTypeIcon = "";
        for(var i = 0; i < data.length; i++) {
        if(data[i].fileType === "dir")
            fileTypeIcon = "<img src='https://img.icons8.com/officel/30/000000/delete-folder.png'>";
        else
            fileTypeIcon = "<img src='https://img.icons8.com/office/30/000000/delete-file.png'>";

         renderData += "<tr>"+
                            "<td>"+data[i].fileName+"</td>"+
                            "<td>"+
                            "<a class='remove-click' data='"+data[i].fileName+"'>"+
                            fileTypeIcon +
                            "</a>"+
                            "</td>"+
                            "<td>"+
                            "<a download>" + "<img src='https://img.icons8.com/ultraviolet/30/000000/download.png'>" + "</a>" +
                            "</td>"
                        +"</tr>"
        }
        $('.render-files').html(renderData);
        makeFilesRemovable();
    });
}

getAllFilesAndRender();



$('.create-dir').click((e) => {
    e.preventDefault();
    let directoryName = prompt("Directory name: ");

    if(directoryName.length > 0) {
        const createDirRequest = $.post('/user/create-dir/' + directoryName, (response) => {
                console.log(response);
        });

        createDirRequest.done((msg) => {
            console.log("Directory created");
            getAllFilesAndRender();
        });

        createDirRequest.fail((error) => {
            console.log(error);
        });
    }

})


/*
   @@ Uploading AJAX Request - single file upload

 */

$(".upload-click").click((e) => {

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
                  url: '/user/delete-file/',
                  type: 'POST',
                  data: fileName,
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










