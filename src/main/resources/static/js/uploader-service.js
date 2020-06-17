
/*
  @@ Getting all files from the server - all files
*/

function getAllFilesAndRender () {
    $.get("/user/storage/all-files", (data, status) => {
        var renderData = "";
        var fileTypeIcon = "";
        var createDirInDir = "";
        var deleteClass = "";
        var downloadAble = "";
        for(var i = 0; i < data.length; i++) {
        if(data[i].directory){
            fileTypeIcon = "<img src='https://img.icons8.com/officel/30/000000/delete-folder.png'>";
            createDirInDir = "<img src='https://img.icons8.com/office/30/000000/add-folder.png'>";
            deleteClass = 'remove-dir';
            downloadAble = '';
        } else {
            fileTypeIcon = "<img src='https://img.icons8.com/office/30/000000/delete-file.png'>";
            createDirInDir = "";
            deleteClass = 'remove-click';
            downloadAble = "href='/user/storage/download-file?fileName="+data[i].fileName+"'";
        }
         renderData += "<tr>"+
                            "<td>"+data[i].fileName+"</td>"+
                            "<td>"+
                            "<a class='" + deleteClass + "' data='"+data[i].fileName+"'>"+
                            fileTypeIcon +
                            "</a>"+
                            "</td>"+
                            "<td>" +
                            "<a class='crt-child' data='"+data[i].fileName+"'>" +
                            createDirInDir +
                            "</a>" +
                            "</td>" +
                            "<td>"+
                            "<a "+ downloadAble +">" +
                            "<img src='https://img.icons8.com/ultraviolet/30/000000/download.png'>" + "</a>" +
                            "</td>"
                        +"</tr>"
        }
        $('.render-files').html(renderData);
        makeFilesRemovable();
        makesDirectoriesRemovable();
        letCreateDirsInDir();
//        downloadAble();

    });
}

getAllFilesAndRender();


$('.download-file').click((e) => {
console.log('Clicked');
 var fileName = $(e.currentTarget).attr('data');
console.log('fileName '+ fileName);

const childDirRequest = $.get('/user/storage/download-file?fileName='+fileName, (response) => {
                  alert(response);
      });

      childDirRequest.done((msg) => {
      });

      childDirRequest.fail((error) => {
                  console.log(error);
      });

})



function letCreateDirsInDir() {

  $('.crt-child').click((e) => {
      e.preventDefault();
      console.log("Clicked");
      let childDirectoryName = prompt("Directory name: ");
      let parentDirectory = $(e.currentTarget).attr('data');
      let fullPath = parentDirectory + childDirectoryName;

      const childDirRequest = $.post('/user/storage/create-dir', {dirName: fullPath}, (response) => {
                  alert(response);
      });

      childDirRequest.done((msg) => {
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
        const createDirRequest = $.post('/user/storage/create-dir', {dirName: directoryName}, (response) => {
                alert(response);
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
            console.log(msg);
            $("button[type='submit']").prop('disabled', false);
            $("input[type='file']").val('');
            $('.drop-container').css("background", "grey");
            getAllFilesAndRender();
        });

        uploadRequest.fail((error) => {
            $("button[type='submit']").prop('disabled', false);

            if(error.status === 406){
                alert('Not enough space, we only offer 1GB!');
                getAllFilesAndRender();
            }
            if(error.status === 500){
                alert('This file already exists');
                $("input[type='file']").val('');
            }

          });
    }


 /*
    @@   Delete AJAX Request - By file name AND @@ Delete AJAX Request - Entire Directory
 */

 function makesDirectoriesRemovable() {
       $('.remove-dir').click((e) => {
            e.preventDefault();
            let dirName = $(e.currentTarget).attr('data');

            if(confirm("Sure you want to delete this directory and all its files?") && dirName != undefined){
                   const deleteDirRequest = $.ajax({
                        url: '/user/storage/delete-dir',
                        type: 'POST',
                        data: dirName,
                        cache: false,
                        contentType: false,
                        processData: false
                   });

                    deleteDirRequest.done((msg) => {
                        getAllFilesAndRender();
                        console.log("Dir deleted");
                    });

                    deleteDirRequest.fail((error) => {
                        alert(error.responseText);

                    });
            }

       });

 }

  function makeFilesRemovable() {
    $('.remove-click').click((e) => {
           e.preventDefault();
           let fileName = $(e.currentTarget).attr('data');

          if(confirm("Sure you want to delete this file?") && fileName != undefined){
              const deleteRequest = $.ajax({
                  url: '/user/storage/delete-file',
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














