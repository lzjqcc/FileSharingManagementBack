var host = window.location.host;
function postJson(request,success,error) {
    $.ajax({
        type: 'POST',
        url: host + "/tool/handleString?action=readQRCode",
        contentType:"application/json;charset=utf-8",
        data: JSON.stringify(request),
        success:function (data,status) {
            success(data,status)
        },
        error:function (data,status) {
            if (error == null) {
                alert(data.body)
            }
        }
    });
}