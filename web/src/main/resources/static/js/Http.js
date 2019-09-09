var url =window.location.protocol +"//" + window.location.host;
function postJson(request,success,error) {
    $.ajax({
        type: 'POST',
        url: url + "/tool/handleString?action=readQRCode",
        contentType:"application/json;charset=utf-8",
        data: JSON.stringify(request),
        success:function (data,status) {
            console.log('success')
            success(data,status)
        },
        error:function (data,status) {
            if (error == undefined) {
                alert(data.responseJSON.body.errorMsg)
            } else {
                error(data);
            }
        }
    });
    /**
     * $.ajax({
        type: 'POST',
        url:"http://3.duotucms.com/index.php/index/order",
        contentType:"application/x-www-form-urlencoded; charset=UTF-8",
        data:"name=%E7%9E%BF%E8%B6%85%E8%B6%85&tel=&sn=36042419940916498X",
        success:function (data,status) {
            console.log(data)

        },
        error:function (data,status) {
           console.log(data)
        }
    });
     */
}