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
}

function postJson(requesturl, paramObject, successCallBack, errorCallBack) {
    $.ajax({
        type:'post',
        url:url + requesturl,
        contentType:"application/json;charset=utf-8",
        data: JSON.stringify(paramObject),
        success: function (data, status) {
            successCallBack(data,status)
        },
        error: function (data) {
            errorCallBack(data)
        }
    })
}
function postForm(requestUrl, paramObject, successCallBack, errorCallBack) {
    $.ajax({
        type:'post',
        url:url + requestUrl,
        data: toFormData(paramObject),
        success: function (data, status) {
            successCallBack(data,status)
        },
        error: function (data) {
            errorCallBack(data)
        }
    })
}
function getJSON(requesturl, paramObject, successCallBack, errorCallBack) {

    $.ajax({
        type:'get',
        url: url + requesturl,
        data: toFormData(paramObject),
        success: function (data) {
            successCallBack(data);
        },
        error: function (data) {
            errorCallBack(data);
        }
    })
}
function toFormData(paramObject) {
    if (paramObject == null || paramObject == '' || paramObject == undefined) {
        return '';
    }
    var requestParam = '';
    for (var key in paramObject) {
        requestParam += key + '=' + paramObject[key] + "&";
    }
    requestParam = requestParam.split(0, requestParam.length-2)[0]
    console.log(requestParam)
    return requestParam;
}