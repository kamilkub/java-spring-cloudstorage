
/*
  @@ Getting all files from the server - all files
*/

function getAllFilesAndRender () {
    $.get("/user/all-files", (data, status) => {
        var renderData = "";
        var fileTypeIcon = "";
        var createDirInDir = "";
        for(var i = 0; i < data.length; i++) {
        if(data[i].fileType === "dir"){
            fileTypeIcon = "<img src='https://img.icons8.com/officel/30/000000/delete-folder.png'>";
            createDirInDir = "<img src='https://img.icons8.com/office/30/000000/add-folder.png'>";
        } else {
            fileTypeIcon = "<img src='https://img.icons8.com/office/30/000000/delete-file.png'>";
            createDirInDir = "";
        }
         renderData += "<tr>"+
                            "<td>"+data[i].fileName+"</td>"+
                            "<td>"+
                            "<a class='remove-click' data='"+data[i].fileName+"'>"+
                            fileTypeIcon +
                            "</a>"+
                            "</td>"+
                            "<td>" +
                            "<a class='crt-child' data='"+data[i].fileName+"'>" +
                            createDirInDir +
                            "</a>" +
                            "</td>" +
                            "<td>"+
                            "<a href='/user/download-file?fileName="+data[i].fileName+"'>" +
                            "<img src='https://img.icons8.com/ultraviolet/30/000000/download.png'>" + "</a>" +
                            "</td>"
                        +"</tr>"
        }
        $('.render-files').html(renderData);
        makeFilesRemovable();
        letCreateDirsInDir();
        makeFilesDownloadable();

    });
}

getAllFilesAndRender();

/*
  @@ Create New Child Directory
*/


function letCreateDirsInDir() {

  $('.crt-child').click((e) => {
      e.preventDefault();
      console.log("Clicked");
      let childDirectoryName = prompt("Directory name: ");
      let parentDirectory = $(e.currentTarget).attr('data');
      let fullPath = parentDirectory + childDirectoryName;

      const childDirRequest = $.post('/user/create-dir', {dirName: fullPath}, (response) => {
                  console.log(response);
      });

      childDirRequest.done((msg) => {
                  console.log("Directory created");
                  getAllFilesAndRender();
      });

      childDirRequest.fail((error) => {
                  console.log(error);
      });

  });

 }

 /*
   @@ Download file from the server
 */

 function makeFilesDownloadable() {

     $('.download-click').click((e) => {
      let file = $(e.currentTarget).attr('data');

        if(file.length > 0) {
            const downloadRequest = $.get('/user/download-file', {fileName: file}, (response) => {
                console.log(response);
            });

            downloadRequest.done((msg) => {
                   console.log("File downloaded");
            });

            downloadRequest.fail((error) => {
                    console.log(error);
            });
      }
   });
}




/*
 @@ Create New Parent Directory
*/


$('.create-dir').click((e) => {
    e.preventDefault();
    let directoryName = prompt("Directory name: ");

    if(directoryName.length > 0) {
        const createDirRequest = $.post('/user/create-dir', {dirName: directoryName}, (response) => {
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

});


///*
//   @@ Uploading AJAX Request - single file upload
//
// */

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














