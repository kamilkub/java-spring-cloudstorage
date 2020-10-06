if(document.body.offsetWidth < 1600) {
    let drives = $('.drives');

    for(var i = 0; i < drives.length; i++){
        drives[i].src = "/image/disk-drive400.png";
    }
}