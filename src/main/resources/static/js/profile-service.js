
$('#update-button').click((e) => {
    e.preventDefault();
    let email = $('input[name=email]').val();
    let oldPassword = $('input[name=oldPassword]').val();
    let newPassword = $('input[name=newPassword]').val();

    if(oldPassword != newPassword && newPassword.length >= 6){

        let updateObject = {
            "email" : email,
            "oldPassword" : oldPassword,
            "newPassword" : newPassword
        }

        const updateRequest = $.ajax({
            url : '/user/update-profile-data',
            method: 'POST',
            data: JSON.stringify(updateObject),
            contentType: 'application/json',
            processData: false
        });

        updateRequest.done((response) => {
                if(response == 'BAD_REQUEST'){
                    alert("Password you provided is incorrect or the new one doesn't have 6 characters!");
                    $('input[name=oldPassword]').val("");
                    $('input[name=newPassword]').val("");
                } else {
                    alert("Password changed successfully");
                    $('input[name=oldPassword]').attr("disabled", "true");
                    $('input[name=newPassword]').attr("disabled", "true");
                    $('input[name=oldPassword]').val("");
                    $('input[name=newPassword]').val("");
                }
        });

        updateRequest.fail((error) => {
            console.log(error);
        });

    } else {

       alert('Password is to short or the same');

    }

});

 const getUserData = $.ajax({
        url : '/user/profile-data',
        method : 'GET',
        cache: false,
        contentType: false,
        processData: false
 });

 getUserData.done((msg) => {
        $("input[name=email]").val(msg.email);
        let value = getTakenSpacePercentage(msg.diskLimitation, msg.takenSpace);
        $('.real-percentage').text(Math.round(value));
        $('.text').text("Space limit: " + convertSpaceToReadAbleGB(msg.diskLimitation));
        $('.textSec').text("Available space: " + convertSpaceToReadAbleGB(msg.diskLimitation - msg.takenSpace));
        createBar(value);
 });

 getUserData.fail((error) => {
        console.log(error);
 });


 function convertSpaceToReadAbleGB(userDisk) {
    var sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
    if (userDisk == 0) return 'n/a';

    var i = parseInt(Math.floor(Math.log(userDisk) / Math.log(1024)));
    if (i == 0) return userDisk + ' ' + sizes[i];

    return (userDisk / Math.pow(1024, i)).toFixed(1) + ' ' + sizes[i];
 }

 function getTakenSpacePercentage(userDisk, takenSpace) {
    return ((100 * takenSpace) / userDisk);
 }

 $('#edit-button').click((e) => {
        e.preventDefault();
       $('input[name=oldPassword]').removeAttr("disabled");
       $('input[name=newPassword]').removeAttr("disabled");
//       $('input[name=email]').removeAttr("disabled");
       $('#update-button').removeAttr("disabled");

 });



 function createBar(percent) {
    $('#progress-bar').circleProgress({
        value: percent / 100,
        size: 150,
        fill: {
            gradient: ["#900C3F", "#581845"]
        },
        thickness: 30
    });
 }

