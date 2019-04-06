

var generatePin = $("input[type='hidden']").val();

generatePin.toString();

new QRCode(document.getElementById('qrcode-container'), generatePin);


$('#qrcode-container').removeAttr("title");
$("input[type='hidden']").removeAttr("value");