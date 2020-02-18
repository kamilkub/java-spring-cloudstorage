


 const getUserData = $.ajax({
        url : '/user/profile-data',
        method : 'GET',
        cache: false,
        contentType: false,
        processData: false
 });


 getUserData.done((msg) => {
        $("input[name=username]").val(msg.username);
        $("input[name=email]").val(msg.email);
        $("input[name=email-holder]").val(msg.email);
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
       $('input[name=email]').removeAttr("disabled");
       $('input[name=username]').removeAttr("disabled");

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

