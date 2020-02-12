
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
//   @@ Uploading AJAX Request - file upload
//
// */


    $('html').on('dragover', (e) => {
            e.preventDefault();
            e.stopPropagation();
    });

    $('html').on("drop", (e) => {
            e.preventDefault();
            e.stopPropagation();
    });

    $('.drop-container').on('click', (e) => {
            e.preventDefault();
            $('input[name=files]').click();
    });

    $('.drop-container').on('dragenter', (e) => {
            e.stopPropagation();
            e.preventDefault();
            $('.drop-container').css("background", "#091124");
    });

     $('.drop-container').on('dragover', (e) => {
            e.stopPropagation();
            e.preventDefault();
            $('.drop-container').css("background", "#091124");
     });


    $('.drop-container').on('dragleave', (e) => {
            e.stopPropagation();
            e.preventDefault();
            $('.drop-container').css("background", "grey");
    });

    $('.drop-container').on('drop', (e) => {
            e.stopPropagation();
            e.preventDefault();

            var filesUpload = e.originalEvent.dataTransfer.files;
            var dataForm = new FormData();

            for (var i = 0; i < filesUpload.length; i++) {
                dataForm.append('files', filesUpload[i]);
            }

            uploadFiles(dataForm);
    });



function uploadFiles(uploadData) {

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
            $('.drop-container').css("background", "grey");
            getAllFilesAndRender();
        });

        uploadRequest.fail((error) => {
            $("button[type='submit']").prop('disabled', false);
            alert(error.responseText);

            if(error.status === 500){
                alert('This file already exists');
                $("input[type='file']").val('');
            }

          });
    }


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














