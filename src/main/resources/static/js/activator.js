

var generatePin = $("input[type='hidden']").val();

generatePin.toString();

new QRCode(document.getElementById('qrcode-container'), generatePin);

$('form').css("display", "none");
$('#qrcode-container').removeAttr("title");
$("input[type='hidden']").removeAttr("value");